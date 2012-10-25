package cs555.crawler.wireformatsURL;

import java.nio.ByteBuffer;

import cs555.crawler.utilities.Constants;
import cs555.crawler.utilities.Tools;

public class URLRequest{

	public int size;
	
	public int type; // 4
	
	public int hopCount; // 4
	
	public int hostLength; // 4 
	public String hostName; // hostLength
	
	public int port; // 4
	
	public int id; // 4
	
	public int resolveID; // 4
	
	public int domainLen; // 4
	public String domain; // domainLen
	
	public int urlLen; // 4
	public String url; // urlLen
	
	//================================================================================
	// Constructors
	//================================================================================
	public URLRequest(String h, int p, int i, int r, String d, String u){
		init(h, p, i, r, d, u);
	}
	
	public URLRequest(){
		init("",0,-1,-1, "", "");
	}
	
	public void init(String h, int p, int i, int r, String d, String u){
		type = Constants.URL_Request;
		hopCount = 0;
		
		hostLength = h.length();
		hostName = h;
		
		port = p;
		
		id = i;
		
		resolveID = r;
		
		domainLen = d.length();
		urlLen = u.length();
		domain = d;
		url = u;
		
		size = 4 + 4 + 4 + hostLength + 4 + 4 + 4 + 4 + 4 + domainLen + 4 + urlLen;
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
		
		// Host length and hostname
		bbuff.putInt(hostLength);
		bbuff.put(Tools.convertToBytes(hostName));
		
		// Port 
		bbuff.putInt(port);
		
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
		
		// Host length and hostname
		hostLength = bbuff.getInt();
		byte[] hostBytes = new byte[hostLength];
		bbuff.get(hostBytes);
		hostName = new String(hostBytes,0,hostLength);
		
		// Port
		port = bbuff.getInt();
		
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
		if (this.hostName.equalsIgnoreCase(other.hostName)){
			if (this.port == other.port) {
				if (this.resolveID == other.resolveID) {
					return true;
				}
			}
			
		}
		
		return false;
	}
	
}
