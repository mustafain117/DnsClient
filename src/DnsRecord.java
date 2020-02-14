
public class DnsRecord {
	private String domainName;
	private int type;
	private int TTL;
	private int RDLength;
	public DnsRecord(String domain, int type, int TTL, int RDLength) {
		this.domainName = domain;
		this.type = type;
		this.TTL = TTL;
		this.RDLength = RDLength;
	}	
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
