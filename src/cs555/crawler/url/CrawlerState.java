package cs555.crawler.url;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cs555.crawler.utilities.*;
import cs555.crawler.wireformatsURL.URLResponse;

public class CrawlerState {

	ArrayList<Page> trackingDomains;
	ArrayList<Page> readyList;
	ArrayList<Page> pendingList;
	ArrayList<Page> doneList;

	String linkFile;
	int maxDepth;

	//================================================================================
	// Constructors
	//================================================================================
	public CrawlerState(String lf, int depth){
		linkFile = lf;

		trackingDomains = new ArrayList<Page>();
		readyList = new ArrayList<Page>();
		pendingList = new ArrayList<Page>();
		doneList = new ArrayList<Page>();
		maxDepth = depth;

		buildState();
	}

	public CrawlerState() {
		trackingDomains = new ArrayList<Page>();
		readyList = new ArrayList<Page>();
		pendingList = new ArrayList<Page>();
		doneList = new ArrayList<Page>();
		maxDepth = Constants.Crawl_Depth;	
	}

	public void buildState(){
		try{
			// Open the file that is the first 
			FileInputStream fstream = new FileInputStream(linkFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;

			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				createURLFromLine(strLine);
			}

			//Close the input stream
			in.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	// Turn a line of text into a peer
	public void createURLFromLine(String line){
		String[] lineParts = line.split(" , ");

		String url = lineParts[0];
		String domain = lineParts[1];

		Page p = new Page(url, domain);
		addPage(p);
	}


	//================================================================================
	// Accessor
	//================================================================================
	// Get next ready URL
	public Page getNextReadyPage(){

		if (!readyList.isEmpty()){
			Page url = readyList.get(0);
			url.status = Constants.URL_Pending;
			pendingList.add(url);

			return url;
		}

		return null;
	}

	// Get all ready links
	public ArrayList<Page> getAllReadyLinks() {
		return getNextReadySet(readyList.size());
	}

	// Get multiple pages
	public ArrayList<Page> getNextReadySet(int n){
		ArrayList<Page> readySet= new ArrayList<Page>();

		for (int i=0; i<n; i++){
			readySet.add(getNextReadyPage());
		}

		return readySet;
	}

	//================================================================================
	// Domain Tracking
	//================================================================================
	// Add domain we're tracking
	public void addDomain(String d) {

		if (!isTracking(d)) {
			Page domain = new Page(d, d);
			trackingDomains.add(domain);
		}
	}

	public boolean isTracking(String d) {
		for (Page p : trackingDomains) {
			if (p.urlString.equalsIgnoreCase(d)) {
				return true;
			}
		}

		return false;
	}
	
	public boolean linkIsMine(String url) {
		for (Page  p : trackingDomains) {
			if (url.contains("." + p.domain)) {
				return true;
			}
		}
		
		return false;
	}
	
	// Tally the data
	public void accumulateData(URLResponse response) {
		
		for (Page p : trackingDomains) {
			if (response.domain.equalsIgnoreCase(p.domain)) {
				p.parseResponse(response);
				return;
			}
		}
	}
	
	//================================================================================
	// List manipulation 
	//================================================================================	
	// Add peer
	public boolean addPage(Page u){
		if (!contains(u)){
			if (u.depth < maxDepth){
				readyList.add(u);
				return true;
			}

		}

		return false;
	}

	public Page findReadyUrl(Page u) {
		for (Page url : readyList) {
			if (url.equals(u)){
				return url;
			}
		}

		return null;
	}

	public Page findPendingUrl(Page u){
		for (Page url : pendingList){
			if (url.equals(u)){
				return url;
			}
		}

		return null;
	}

	// Mark complete
	public void markUrlComplete(Page u){
		Page url = findPendingUrl(u);

		if (url != null){
			url.status = Constants.URL_Complete;
			pendingList.remove(url);
			doneList.add(url);
		}
	}

	// Mark pending
	public void markUrlPending(Page u) {
		Page url = findReadyUrl(u);

		if (url != null) {
			url.status = Constants.URL_Pending;
			readyList.remove(url);
			pendingList.add(url);
		}
	}

	//================================================================================
	// Completion methods 
	//================================================================================
	public boolean shouldContinue(){
		return readyLinksRemaining();
	}

	public boolean readyLinksRemaining(){
		return !readyList.isEmpty();
	}

	//================================================================================
	// House Keeping
	//================================================================================
	// Override .contains method
	public boolean contains(Page url) {

		for (Page u : readyList){
			if (url.equals(u)){
				return true;
			}
		}

		for (Page u : pendingList){
			if (url.equals(u)){
				return true;
			}
		}

		for (Page u : doneList){
			if (url.equals(u)){
				return true;
			}
		}

		return false;
	}

	// Override .toString method
	public String toString() {
		String s = "";

		s += "\nReady:\n";
		for (Page u : readyList){
			s += u.toString() + "\n";
		}

		s += "\nPending:\n";

		for (Page u : pendingList){
			s += u.toString() + "\n";
		}

		s += "\nDone:\n";

		for (Page u : doneList){
			s += u.toString() + "\n";
		}

		return s;
	}

}
