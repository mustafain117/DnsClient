
public class DnsRecord {
	private String name;
	private int type, TTL, RDLength, AA, length;
	private String domain;
	private int preference;
	
	public DnsRecord(String name, int type, int TTL, int RDLength, int AA) {
		this.name = name;
		this.type = type;
		this.TTL = TTL;
		this.setRDLength(RDLength);
		this.AA = AA;
	}	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public int getRDLength() {
		return RDLength;
	}
	public void setRDLength(int rDLength) {
		RDLength = rDLength;
	}
	
	public void DisplayResponse() {
		String auth="";
		if(this.AA == 0) {
			auth ="nonauth";
		}else {
			auth ="auth";
		}
		
		if(type==1) {
			System.out.println("IP\t"+  this.domain+  "\t" + this.TTL + "\t"+auth);
		}else if(type==2) {
			System.out.println("NS\t" + this.domain + "\t" + this.TTL + "\t" + auth);
		}else if(type==5) {
			System.out.println("CNAME\t" + this.domain + "\t" + this.TTL + "\t" + auth);
		}else if(type== 15) {
			System.out.println("MX\t" + this.domain + "\t" + this.preference + "\t" + this.TTL+ "\t" + auth);
		}
		
	}
	public int getPreference() {
		return preference;
	}
	public void setPreference(int preference) {
		this.preference = preference;
	}
	public int getRecordLength() {
		return length;
	}
	public void setRecordLength(int length) {
		this.length = length;
	}
}
