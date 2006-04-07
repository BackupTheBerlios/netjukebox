package philippe3.client;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DataSink;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;

public class RTPClient3 implements ControllerListener {
	public static void main(String [] args) {
		new RTPClient3(args[0], args[1], args[2]);
	}

	public RTPClient3(String ip, String port, String piste) {
		Player p;
		String srcUrl = "rtp://"+ip+":"+port+"/audio/"+piste;
		System.out.println(srcUrl);
		//DataSink sink;
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