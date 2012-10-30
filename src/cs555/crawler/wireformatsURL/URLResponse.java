package cs555.crawler.wireformatsURL;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import cs555.crawler.utilities.Constants;
import cs555.crawler.utilities.Tools;

public class URLResponse extends URLRequest{

	public int linkCount; // 4
	public ArrayList<String> links;
	public int filesLengh; // 4
	public String fileString; //filesLength
	
	//================================================================================
	// Overridden Constructors
	//================================================================================
	public URLResponse(String h, int p, String hi, int pi, int i, int r, String d, String u, int dep, ArrayList<String> l, String f){
		super.init(h, p, hi, pi, i, r, d, u, dep);
		init(l, f);
		
	}
	
	public URLResponse(){
		super.init("", 0, "", 0, -1,-1, "", "", 0);
		init(new ArrayList<String>(), "");
	}
	
	public URLResponse(URLRequest req, ArrayList<String> l, String f) {
		String rh = req.requesterHostName;
		int rp = req.requesterPort;
		String ih = req.intermediaryHostName;
		int ip = req.intermediaryPort;
		int id = req.id;
		int ri = req.resolveID;
		String dn = req.domain;
		String url = req.url;
		int de = req.depth;
		
		super.init(rh, rp, ih, ip, id, ri, dn, url, de);
		init(l, f);
	}
	
	public void init(ArrayList<String> l, String f) {
		type = Constants.URL_Response;
		
		linkCount = l.size();
		links = l;
		filesLengh = f.length();
		fileString = f;
		
		size += 4 + 4 + filesLengh;
		
		for (String s : links) {
			size += 4;
			size += s.length();
		}
	}
	
	//================================================================================
	// Marshall
	//================================================================================
	public byte[] marshall(){
		byte[] bytes = super.marshall();
		ByteBuffer bbuff = ByteBuffer.wrap(bytes);
		
		// Number of links
		bbuff.putInt(linkCount);
		
		// Each link
		for (int i=0; i<linkCount; i++) {
			bbuff.putInt(links.get(i).length());
			bbuff.put(Tools.convertToBytes(links.get(i)));
		}
		
		// filestring
		bbuff.putInt(filesLengh);
		bbuff.put(Tools.convertToBytes(fileString));
		
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
		
		// Number of links
		linkCount = bbuff.getInt();
		
		// Each link
		linkCount = bbuff.getInt();
		for (int i=0; i<linkCount; i++){			
			int strLen = bbuff.getInt();
			byte[] stringBytes = new byte[strLen];
			bbuff.get(stringBytes);
			links.add(new String(stringBytes,0,strLen));
		}
		
		// filestring
		filesLengh = bbuff.getInt();
		byte[] fileBytes = new byte[filesLengh];
		bbuff.get(fileBytes);
		fileString = new String(fileBytes,0,filesLengh);
	}
}
