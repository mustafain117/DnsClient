public class DnsResponse {
	private int length;
	private int QR;
	private int RCODE;
	private int responeTime;
	private int anCount;
	private int arCount;
	private String ip;
	private byte[] buffer;
	private int startIndex;
	private int type ;

	
	public DnsResponse(byte[] buffer, int startIndex) {
		this.startIndex = startIndex;
		this.buffer = buffer;
	}
	
	public void parseResponse() {
		
		
		
		int startIndex = this.startIndex;
		System.out.println("The startIndex is: " + startIndex);
		parseHeader();
		parseAnswer();
		
	}
	
	private void parseHeader() {
		
		this.QR = ((this.buffer[2] & (0x80)) % 127);
		
		this.RCODE = ((this.buffer[3]) & (0x0f));

		this.anCount = (this.buffer[6] & 0xff) + (this.buffer[7] & 0xff);
		
		this.arCount = (this.buffer[10] & 0xff) + (this.buffer[11] & 0xff);
		
		System.out.println(startIndex);
		
		
	}
	
	private void parseAnswer() {
		//this.type = this.buffer[startIndex + 2] & (0xff);
		
		this.type = (this.buffer[startIndex + 2] & 0xff) + (this.buffer[startIndex + 3] & 0xff);
		
		System.out.println("The value of type is: " + this.type);
		
		
	}

	public int getRcode() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private void DisplayResponse() {
		System.out.println("**Answer Section (" + this.anCount+ " records)**");
		System.out.println("IP\t"+this.ip+"\t[seconds can cache]\t"+"[auth | nonauth]");
		/*
		 * CNAME <tab> [alias] <tab> [seconds can cache] <tab> [auth | nonauth]
		 * MX <tab> [alias] <tab> [pref] <tab> [seconds can cache] <tab> [auth | nonauth]
		 * NS <tab> [alias] <tab> [seconds can cache] <tab> [auth | nonauth]
		 */
		System.out.println("**Additional Section ("+ this.arCount +" records)**");
		
	}
	
	//private
	
}