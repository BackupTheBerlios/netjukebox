package client;

import java.io.*;
import javax.media.*;
import javax.media.format.*;
import javax.media.protocol.*;

public class RTPClient implements ControllerListener {
	/**public static void main(String [] args) {
		new RTPClient();
	}*/

	String connect;
	public RTPClient(String connect) {
		this.connect = connect;
		Player p;
		String srcUrl = "rtp://"+connect+"/audio/1";
		System.out.println(srcUrl);
		DataSink sink;
		MediaLocator src = new MediaLocator(srcUrl);
		try {
			p = Manager.createPlayer(src);
			p.addControllerListener(this);
			p.start();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public synchronized void controllerUpdate(ControllerEvent evt) {
		if (evt instanceof EndOfMediaEvent) {
			System.exit(0);
		}
		else {
			System.out.println(evt.toString());
		}
	}
}