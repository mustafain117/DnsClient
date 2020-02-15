
public class DnsClient {
	public static void main(String[] args) {
		try {
			DnsController controller = new DnsController(args);
			controller.request();
		} catch (Exception e) {
			String error = e.getMessage();
			System.out.println(error);
		}
	}
}
