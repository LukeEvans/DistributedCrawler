package cs555.crawler.wireformatsURL;

import java.nio.ByteBuffer;

import cs555.crawler.utilities.Constants;
import cs555.crawler.utilities.Tools;

public class URLRequest{

	public int size;
	
	public int type; // 4
	
	public int hopCount; // 4
	
	public int requesterHostLength; // 4 
	public String requesterHostName; // requesterHostLength
	public int requesterPort; // 4
	
	public int intermediaryHostLength; // 4
	public String intermediaryHostName; // intermediaryHostLength
	public int intermediaryPort; // 4
	
	public int id; // 4
	
	public int resolveID; // 4
	
	public int domainLen; // 4
	public String domain; // domainLen
	
	public int urlLen; // 4
	public String url; // urlLen
	
	public int depth; // 4;
	
	//================================================================================
	// Constructors
	//================================================================================
	public URLRequest(String h, int p, String h2, int p2, int i, int r, String d, String u, int dep){
		init(h, p, h2, p2, i, r, d, u, dep);
	}
	
	public URLRequest(){
		init("",0, "", 0, -1,-1, "", "", 0);
	}
	
	public void init(String h, int p, String h2, int p2, int i, int r, String d, String u, int dep){
		type = Constants.URL_Request;
		hopCount = 0;
		
		requesterHostLength = h.length();
		requesterHostName = h;
		requesterPort = p;
		
		intermediaryHostLength = h2.length();
		intermediaryHostName = h2;
		intermediaryPort = p2;
		
		id = i;
		
		resolveID = r;
		
		domainLen = d.length();
		urlLen = u.length();
		domain = d;
		url = u;
		
		depth = dep;
		
		size = 4 + 4 + 4 + requesterHostLength + 4 + 4 + 4 + 4 + 4 + domainLen + 4 + urlLen + 4 + 4 + intermediaryHostLength + 4;
	}
	
	
	//================================================================================
	// Marshall
	//================================================================================
	public byte[] marshall(){
		byte[] bytes = new byte[size + 4];
		ByteBuffer bbuff = ByteBuffer.wrap(bytes);
		
		// Size
		bbuff.putInt(size);
		
		// type
		bbuff.putInt(type);
		
		// Hop count 
		bbuff.putInt(hopCount);
		
		// Requester host length and hostname
		bbuff.putInt(requesterHostLength);
		bbuff.put(Tools.convertToBytes(requesterHostName));
		bbuff.putInt(requesterPort);
		
		// Intermediary host length and hostname
		bbuff.putInt(intermediaryHostLength);
		bbuff.put(Tools.convertToBytes(intermediaryHostName));
		bbuff.putInt(intermediaryPort);
		
		// ID
		bbuff.putInt(id);
		
		// Resolve ID
		bbuff.putInt(resolveID);
		
		// Domain
		bbuff.putInt(domainLen);
		bbuff.put(Tools.convertToBytes(domain));
		
		// URL
		bbuff.putInt(urlLen);
		bbuff.put(Tools.convertToBytes(url));
		
		// depth
		bbuff.putInt(depth);
		
		return bytes;
	}
	
	
	//================================================================================
	// Unmarshall
	//================================================================================
	public void unmarshall(byte[] bytes){
		ByteBuffer bbuff = ByteBuffer.wrap(bytes);
		
		// Size
		size = bbuff.getInt();
		
		// type
		type = bbuff.getInt();
		
		// Hopcount 
		hopCount = bbuff.getInt();
		
		// Requester
		// Host length and hostname
		requesterHostLength = bbuff.getInt();
		byte[] hostBytes = new byte[requesterHostLength];
		bbuff.get(hostBytes);
		requesterHostName = new String(hostBytes,0,requesterHostLength);
		// Port
		requesterPort = bbuff.getInt();
		
		// Intermediary
		// host length and hostname
		intermediaryHostLength = bbuff.getInt();
		byte[] intHostBytes = new byte[intermediaryHostLength];
		bbuff.get(intHostBytes);
		intermediaryHostName = new String(intHostBytes,0,intermediaryHostLength);
		// Port
		intermediaryPort = bbuff.getInt();
		
		// ID
		id = bbuff.getInt();
		
		// Resolve ID
		resolveID = bbuff.getInt();
		
		// Domain
		domainLen = bbuff.getInt();
		byte[] domainBytes = new byte[domainLen];
		bbuff.get(domainBytes);
		domain = new String(domainBytes,0,domainLen);
		
		// URL
		urlLen = bbuff.getInt();
		byte[] urlBytes = new byte[urlLen];
		bbuff.get(urlBytes);
		url = new String(urlBytes,0,urlLen);
		
		// depth
		depth = bbuff.getInt();
	}
	
	//================================================================================
	// House Keeping
	//================================================================================
	// Override the toString method
	public String toString(){
		String s = "";
		
		s += "node: " + id + " resolving: " + resolveID + " Domain: " + domain + " Url: " + url + "\n";
		
		return s;
	}
	
	// Override the equals method
	public boolean equals(URLRequest other) {
		if (this.requesterHostName.equalsIgnoreCase(other.requesterHostName)){
			if (this.requesterPort == other.requesterPort) {
				if (this.resolveID == other.resolveID) {
					return true;
				}
			}
			
		}
		
		return false;
	}
	
}
