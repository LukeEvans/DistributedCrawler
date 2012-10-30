package cs555.crawler.utilities;

// Constants for message types
public class Constants {

	// Message types
	public static final int Payload = 1;
	public static final int Verification = 2;
	public static final int Predesessor_Request = 3;
	public static final int Predesessor_Response = 4;
	public static final int Predessessor_Election = 5;
	public static final int Deregister_Request = 6;
	public static final int Deregister_Response = 7;
	public static final int Registration_Request = 8;
	public static final int Registration_Reply = 9;
	public static final int lookup_request = 10;
	public static final int lookup_reply = 11;
	public static final int store_request = 12;
	public static final int Null_Peer = 13;
	public static final int Successor_Request = 14;
	public static final int Successor_Leaving = 15;
	public static final int Predessesor_Leaving = 16;
	public static final int RandomPeer_Requst = 17;
	public static final int RandomPeer_Response = 18;
	public static final int Domain_Request = 19;
	public static final int URL_Request = 20;
	public static final int URL_Response = 21;
	public static final int DHT_Scan = 22;
	public static final int Seed_Node = 23;
	public static final int Domain_Response = 24;
	public static final int Handoff_Request = 25;
	
	// Flags
	public static final int Undefined = -1;
	public static final int URL_Pending = 94;
	public static final int URL_Complete = 95;
	public static final int URL_Ready = 96;
	public static final int Store_Lookup = 97;
	public static final int Continue = 98;
	public static final int Failure = 99;
	public static final int Success = 100;
	
	//================================================================================
	// Config data
	//================================================================================
	// Max message sizes
	public static final int LEN_BYTES = 6022386;
	
	// ID space of DHT
	public static final int Id_Space = 4;
	
	// Base path for data to be stored
	public static final String base_path = "/tmp/evansl/crawlData/";
	
	// Crawl depth
	public static final int Crawl_Depth = 5;
	
	// Default Thread pool size
	public static final int Threads = 3;
	
	// Refresh time
	public static final int Refresh_Time = 30;
}
