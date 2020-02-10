
public class Main {
	public static void main(String[] args) {
		try {
			DnsClient dnsClient = new DnsClient(args);
			dnsClient.request();
		} catch (Exception e) {
			String error = e.getMessage();
		}
	}
}
