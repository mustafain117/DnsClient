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
	
	public byte[] createRequestPacket() {
		//create byteBuffer
		
		//create header
		
		//create question
		
		
		return null;
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
		
		return header.array();
	}
}
