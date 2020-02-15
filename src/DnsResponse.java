import java.nio.ByteBuffer;

public class DnsResponse {
	private int length, QR, RCODE,responeTime, anCount, arCount, startIndex, type, classData, TTL, addressLength, AA, nsCount;
	private String ip = "";
	private byte[] buffer;
	private DnsRecord[] ansRecords;
	private DnsRecord[] additionalRecords;


	
	public DnsResponse(byte[] buffer, int startIndex) {
		this.startIndex = startIndex;
		this.buffer = buffer;
	}
	
	public void parseResponse() {		
		parseHeader();
		
		ansRecords = new DnsRecord[this.anCount];
		int index = this.startIndex;
		for(int i = 0 ; i < this.anCount; i++) {
			ansRecords[i] = parseAnswer(index);
			index += ansRecords[i].getRecordLength();
		}
		if(this.arCount > 0) {
			for(int i = 0 ; i < this.nsCount ; i++) {
				index += parseAnswer(index).getRecordLength();
			}
		
			additionalRecords = new DnsRecord[this.arCount];
			for(int i = 0 ; i < this.anCount; i++) {
				additionalRecords[i] = parseAnswer(index);
				index += additionalRecords[i].getRecordLength();
			}
		}
	}
	
	private void parseHeader() {
		
		this.QR = ((this.buffer[2] & (0x80)) % 127);
		
		this.AA = (this.buffer[2] & (0x04));
				
		this.RCODE = ((this.buffer[3]) & (0x0f));
		
		switch(this.RCODE) {
		case 0: 
			break;
		case 1: 
			throw new RuntimeException("Error\tFormat error: the name server was unable to interpret the query.");
		case 2:
			throw new RuntimeException("Error\tServer failure: the name server was unable to process this query due to a problem with the name server.");
		case 3:
			throw new RuntimeException("NOTFOUND");
		case 4:
			throw new RuntimeException("Error\tNot implemented: the name server does not support the requested kind of query.");
		case 5:
			throw new RuntimeException("Error\tRefused: the name server refuses to perform the requested operation for policy reasons");
		}

		this.anCount = (this.buffer[6] & 0xff) + (this.buffer[7] & 0xff);
		
		this.nsCount = (this.buffer[8] & 0xff) + (this.buffer[9] & 0xff);
		
		this.arCount = (this.buffer[10] & 0xff) + (this.buffer[11] & 0xff);
		
	}
	
	private DnsRecord parseAnswer(int index) {
		
		int position = index;
		RecordData data = getDomainFromIndex(position);
		position += data.getCount();
		String domain = data.getDomain();
	
		
		this.type = (this.buffer[position] & 0xff) + (this.buffer[position+1] & 0xff);
		
		position+=2;
		
		this.classData = (this.buffer[position] & 0xff) + (this.buffer[position+1] & 0xff);
		
		if(this.classData != 1) {
			throw new RuntimeException("ERROR\tUnexpected class code.");
		}
		
		position+=2;
		
		this.TTL = (this.buffer[position] & 0xff) + (this.buffer[position+1] & 0xff) +
				(this.buffer[position+2] & 0xff) + (this.buffer[position+3] & 0xff);
		
		position+=4;
		
		this.addressLength = (this.buffer[position] & 0xff) + (this.buffer[position+1] & 0xff);
		
		position+=2;
		
		DnsRecord record = new DnsRecord(domain, this.type, this.TTL, this.addressLength, this.AA);
		
		record.setDomain(parseRDdata(position, record.getType(), record.getRDLength(), record));
		
		record.setRecordLength(position + record.getRDLength() - index);
		
		return record;
	}
	
	private String parseRDdata(int position, int type, int rdDataLength, DnsRecord record) {
		String result = "";
		if(type==1) {
			int addr [] = new int [rdDataLength];
			for(int i = 0; i < rdDataLength; i++) {	
				addr[i] = (this.buffer[position + i] & 0xff);
				if( i< rdDataLength-1) {
					result = result + addr[i] + ".";
				}else {
					result = result + addr[i];
				}
			}
		}else if(type==2) {
			RecordData data = getDomainFromIndex(position);
			result = data.getDomain();
		}else if(type==5) {
			RecordData data = getDomainFromIndex(position);
			result = data.getDomain();
		}else if(type== 15) {
			record.setPreference((this.buffer[position] & 0xff) + (this.buffer[position+1] & 0xff));
			RecordData data = getDomainFromIndex(position+2);
			result = data.getDomain();
		}
		return result;
	}

	private RecordData getDomainFromIndex(int index){
    		int wordSize = buffer[index];
    		StringBuilder domain = new StringBuilder();
    		boolean start = true;
    		int count = 0;
    		while(wordSize != 0){
			if (!start){
				domain.append(".");
			}
			if ((wordSize & 0xC0) == 0xC0) {
	    			byte[] offset = { (byte) (buffer[index] & 0x3F), buffer[index + 1] };
	            ByteBuffer wrapped = ByteBuffer.wrap(offset);
	            domain.append(getDomainFromIndex(wrapped.getShort()).getDomain());
	            index += 2;
	            count +=2;
	            wordSize = 0;
	    		}else{
	    			domain.append(getWordFromIndex(index));
	    			index += wordSize + 1;
	    			count += wordSize + 1;
	    			wordSize = buffer[index];
	    		}
			start = false;     
    	}
        return new RecordData(domain.toString(), count);
    }
	private String getWordFromIndex(int index){
		StringBuilder word = new StringBuilder();
		int wordSize = buffer[index];
		for(int i =0; i < wordSize; i++){
    			word.append((char) buffer[index + i + 1]);
		}
		return word.toString();
    }
	
	public int getRcode() {
		return this.RCODE;
	}
	
	public void DisplayResponse() {
	
		System.out.println("**Answer Section (" + this.anCount+ " records)**");
		
		for(int i = 0 ; i < this.ansRecords.length ; i++) {
			ansRecords[i].DisplayResponse();
		}
		
		if(this.arCount > 0) {
			System.out.println("**Additional Section ("+ this.arCount +" records)**");
			for(int i = 0 ; i < this.additionalRecords.length ; i++) {
				additionalRecords[i].DisplayResponse();
			}
		}
	}
}