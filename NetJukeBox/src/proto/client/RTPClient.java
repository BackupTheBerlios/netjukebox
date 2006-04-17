package proto.client;

import java.io.*;
import javax.media.*;
import javax.media.format.*;
import javax.media.protocol.*;

public class RTPClient implements ControllerListener {

	private Player p;
	
	public RTPClient(String url) {
		
		System.out.println(url);
		try {
			MediaLocator src = new MediaLocator(url);
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
			System.out.println("Fin du média");
			p.stop();
		}
		else {
			System.out.println(evt.toString());
		}
	}
}