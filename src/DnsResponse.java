
public class DnsResponse {
	private int length;
	private int QR;
	private int RCODE;
	private int responeTime;
	private int anCount;
	private int arCount;
	private String ip;
	
	private byte[] buffer;
	
	public DnsResponse(byte[] buffer) {
		this.buffer = buffer;
	}
	
	private void parseResponse() {
		
	}

	public int getRcode() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private void DisplayResponse() {
		System.out.println("***Answer Section (" + this.anCount+ " records)***");
		System.out.println("IP\t"+this.ip+"\t[seconds can cache]\t"+"[auth | nonauth]");
		/*
		 * CNAME <tab> [alias] <tab> [seconds can cache] <tab> [auth | nonauth]
		 * MX <tab> [alias] <tab> [pref] <tab> [seconds can cache] <tab> [auth | nonauth]
		 * NS <tab> [alias] <tab> [seconds can cache] <tab> [auth | nonauth]
		 */
		System.out.println("***Additional Section ("+ this.arCount +" records)***");
		
	}
}
