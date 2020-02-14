public class DnsResponse {
	private int length;
	private int QR;
	private int RCODE;
	private int responeTime;
	private int anCount;
	private int arCount;
	private String ip = "";
	private byte[] buffer;
	private int startIndex;
	private int type ;
	private int classData;
	private int TTL;
	private int addressLength;
	private int AA;

	
	public DnsResponse(byte[] buffer, int startIndex) {
		this.startIndex = startIndex;
		this.buffer = buffer;
	}
	
	public void parseResponse() {		
		parseHeader();
		parseAnswer();
	}
	
	private void parseHeader() {
		
		this.QR = ((this.buffer[2] & (0x80)) % 127);
		
		this.AA = (this.buffer[2] & (0x04));
				
		this.RCODE = ((this.buffer[3]) & (0x0f));

		this.anCount = (this.buffer[6] & 0xff) + (this.buffer[7] & 0xff);
		
		this.arCount = (this.buffer[10] & 0xff) + (this.buffer[11] & 0xff);
		
	}
	
	private void parseAnswer() {
		//this.type = this.buffer[startIndex + 2] & (0xff);

		this.type = (this.buffer[startIndex + 2] & 0xff) + (this.buffer[startIndex + 3] & 0xff);
		
		this.classData = (this.buffer[startIndex + 4] & 0xff) + (this.buffer[startIndex + 5] & 0xff);
		
		this.TTL = (this.buffer[startIndex + 6] & 0xff) + (this.buffer[startIndex + 7] & 0xff) +
				(this.buffer[startIndex + 8] & 0xff) + (this.buffer[startIndex + 9] & 0xff);
		
		this.addressLength = (this.buffer[startIndex + 10] & 0xff) + (this.buffer[startIndex + 11] & 0xff);
		
		int addr [] = new int [addressLength];
		for(int i = 0; i<this.addressLength; i++) {	
			addr[i] = (this.buffer[startIndex + 12 + i] & 0xff);
			if(i<addressLength-1) {
				ip = ip + addr[i] + ".";
			}else {
				ip = ip + addr[i];
			}
		}		
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
		
}