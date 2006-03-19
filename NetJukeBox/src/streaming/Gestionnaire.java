package streaming;

import java.util.*;

public class Gestionnaire {

	//private String ipAdress;
	private int portBase;
	private Vector streams;

	public Gestionnaire(/*String ipAdress,*/ int port) {

		//this.ipAddress = ipAddress;
		this.portBase = port;
		this.streams = new Vector();
	}

	public boolean startDiffusionDocument(String filename, String ipAdress) {
		Stream stream = new Stream(filename, ipAdress, this.portBase);
		stream.start();
		this.streams.add(stream);
		return true;
	}
	
	public boolean stopDiffusionDocument(String filename, String ipAdress) {
		int i =0;
		Stream stream;
		
		do {
			stream = (Stream)this.streams.get(i++);
		} while (stream.getFilename()!=filename || stream.getIpAdress()!=ipAdress);
		stream.stop();
		this.streams.remove(stream);
		return true;
	}
	
	public boolean stopAll() {
		this.streams.clear();
		return true;
	}
}