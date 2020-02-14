import java.nio.ByteBuffer;
import java.util.Random;

public class DnsPacket {
	//Packet:
		//Header 
		//Question
		//Authority
		//Additional
	private String domainName;
	private String queryType;
	
	public DnsPacket(String domainName, String queryType) {
		this.domainName = domainName;
		this.queryType = queryType;
	}
	
	public DnsPacket() {

	}
	
	public byte[] createRequestPacket() {
		
		//create header
		byte[] header = packetHeader();
		//create question
		byte[] question = getQuestion(this.domainName, this.queryType);
		
		int packetLength = header.length + question.length;
		
		ByteBuffer packet = ByteBuffer.allocate(packetLength);
		
		packet.put(header);
		packet.put(question);
		
		return packet.array();
		
	}
	private byte[] packetHeader() {
		
		//Header Structure:
		//6 rows x 16 bits per row = 12 bytes
		/*
		0  1	  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 	
	    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	    |                      ID                       |
	    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	    |QR|   Opcode  |AA|TC|RD|RA|   Z    |   RCODE   |
	    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	    |                    QDCOUNT                    |
	    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	    |                    ANCOUNT                    |
	    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	    |                    NSCOUNT                    |
	    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	    |                    ARCOUNT                    |
	    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	    */
		
		ByteBuffer header = ByteBuffer.allocate(12);
		
		byte[] id = new byte[2];
		Random rand = new Random();
		rand.nextBytes(id);
		//add random id to header
		header.put(id);
		//  |QR| = 0   Opcode = 0 |AA = 0|TC = 0 |RD = 1 
		header.put((byte)0x01);
		//  RA = 0 |   Z  = 0  |   RCODE = 0  |
		header.put((byte)0x00);
		// QDCOUNT = 1
		header.put((byte)0x00);
		header.put((byte)0x01);
		
	//MAYBE NEED TO ADD ANCOUNT, NSCOUNT and ARCOUNT bytes as well.
	//16 bits = 2 bytes for each.
		
		return header.array();
	}
	
	public byte[] getQuestion(String domainName, String queryType) {

		//calculate bytes needed for QNAME
		int nbBytes = 0;
		
		String[] tokens = domainName.split("\\.");
		//1 byte per label
		nbBytes = tokens.length;
		for(int i = 0 ; i < tokens.length; i++) {
			// 1 byte for each character in the domain name
			nbBytes += tokens[i].length();
		}
		
		//buffer with size of QNAME + 5 bytes for End of name,  QTYPE and QCLASS
		ByteBuffer question = ByteBuffer.allocate(nbBytes + 5);
		
		//add QNAME to buffer
		for(int i = 0 ; i < tokens.length ; i++) {
			int label = tokens[i].length();
			question.put((byte)label);
			for(int j = 0 ; j < tokens[i].length() ; j++) {
				question.put((byte)tokens[i].charAt(j));
			}
		}		
		//end of Qname
		question.put((byte)0x00);
		
		byte[] QTYPE = new byte[2];
		// A  = 1 , NS = 2, MX = 15
		if(queryType.equals("A")) {
			QTYPE[0] = (byte)(0x00);
			QTYPE[1] = (byte)(0x01);
		}else if(queryType.equals("-ns")) {
			QTYPE[0] = (byte)(0x00);
			QTYPE[1] = (byte)(0x02);
		}else {
			QTYPE[0] = (byte)(0x00);
			QTYPE[1] = (byte)(0x0f);
		}
		//add QTYPE to buffer
		question.put(QTYPE);
		//QCLASS
		question.put((byte)0x00);
		question.put((byte)0x01);
		
		return question.array();
	}

	
}
