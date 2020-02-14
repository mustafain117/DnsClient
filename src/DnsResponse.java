import java.nio.ByteBuffer;

public class DnsResponse {
	private int length;
	private int QR;
	private int RCODE;
	private int responeTime;
	private int anCount;
	private int arCount;
	private String ip = "";
	private String aliasName = "";
	private byte[] buffer;
	private int startIndex;
	private int type ;
	private int classData;
	private int TTL;
	private int addressLength;
	private int AA;
	private String nameServer = "";
	private DnsRecord[] ansRecords;

	
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
		}
	}
	
	private void parseHeader() {
		
		this.QR = ((this.buffer[2] & (0x80)) % 127);
		
		this.AA = (this.buffer[2] & (0x04));
				
		this.RCODE = ((this.buffer[3]) & (0x0f));

		this.anCount = (this.buffer[6] & 0xff) + (this.buffer[7] & 0xff);
		
		this.arCount = (this.buffer[10] & 0xff) + (this.buffer[11] & 0xff);
		
	}
	
	private void parseAnswer(int index) {
		
		int position = index;
		RecordData data = getDomainFromIndex(position);
		position += data.getCount();
		String domain = data.getDomain();
	
		
		this.type = (this.buffer[startIndex + 2] & 0xff) + (this.buffer[startIndex + 3] & 0xff);
		
		
		this.classData = (this.buffer[startIndex + 4] & 0xff) + (this.buffer[startIndex + 5] & 0xff);
		
		if(this.classData != 1) {
			throw new RuntimeException("ERROR\tUnexpected class code.");
		}
		
		this.TTL = (this.buffer[startIndex + 6] & 0xff) + (this.buffer[startIndex + 7] & 0xff) +
				(this.buffer[startIndex + 8] & 0xff) + (this.buffer[startIndex + 9] & 0xff);
		
		this.addressLength = (this.buffer[startIndex + 10] & 0xff) + (this.buffer[startIndex + 11] & 0xff);
		
		DnsRecord record = new DnsRecord(domain, this.type, this.TTL, this.addressLength);
		
		
//		parseRData(this.type, startIndex + 12, this.addressLength);
	}

	private void parseRData(int recordType, int index, int rdDataLength) {
		//Type A
		if(recordType == 1) {
			
			int addr [] = new int [rdDataLength];
			for(int i = 0; i < rdDataLength; i++) {	
				addr[i] = (this.buffer[index + i] & 0xff);
				if( i< rdDataLength-1) {
					ip = ip + addr[i] + ".";
				}else {
					ip = ip + addr[i];
				}
			}		
		}
		//Type CNAME
		else if(recordType == 5) {
			System.out.println("CNAME: " + getDomainFromIndex(index) );	
		}
		// Type NS
		else if(recordType == 2) {
			for(int i = index ; i < index + rdDataLength - 1 ; i++) {
				char c = (char) this.buffer[i];
				this.nameServer  = this.nameServer + c;
			}
			System.out.println("NS: " + this.nameServer);
		}
		//Type MX
		else if(recordType == 15){
//			byte[] preference = { this.buffer[index], this.buffer[index+1]};
//			index += 2;
//			String mxName = "";
//			for(int i = index; i < index + rdDataLength ; i++) {
//				mxName = mxName + (char)this.buffer[i];
//			}
//			
//			System.out.println(mxName);
			System.out.println("mx: " + getDomainFromIndex(index) );
		}
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
	            domain.append(getDomainFromIndex(wrapped.getShort()));
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
	public int getRcode() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void DisplayResponse() {
		String auth="";
		System.out.println("**Answer Section (" + this.anCount+ " records)**");
		if(this.AA == 0) {
			auth ="nonauth";
		}else {
			auth ="auth";
		}
		System.out.println("IP\t"+this.ip+"\t"+this.TTL+"\t"+auth);
		/*
		 * CNAME <tab> [alias] <tab> [seconds can cache] <tab> [auth | nonauth]
		 * MX <tab> [alias] <tab> [pref] <tab> [seconds can cache] <tab> [auth | nonauth]
		 * NS <tab> [alias] <tab> [seconds can cache] <tab> [auth | nonauth]
		 */
		System.out.println("**Additional Section ("+ this.arCount +" records)**");
		
	}
	
	private String getWordFromIndex(int index){
    	StringBuilder word = new StringBuilder();
    	int wordSize = buffer[index];
    	for(int i =0; i < wordSize; i++){
    		word.append((char) buffer[index + i + 1]);
		}
    	return word.toString();
    }
	
		
}