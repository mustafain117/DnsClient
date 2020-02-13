
public class DnsResponse {
	private int length;
	private int QR;
	private int RCODE;
	private int responeTime;
	private int anCount; 
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
		
	}
}
