
public class RecordData {
	private String domain;
	private int count;

	public RecordData(String domain, int count) {
		this.setDomain(domain);
		this.setCount(count);
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
