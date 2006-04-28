package proto.client;

import java.net.InetAddress;

import javax.media.ControllerErrorEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.RealizeCompleteEvent;
import javax.media.control.BufferControl;
import javax.media.protocol.DataSource;
import javax.media.rtp.RTPControl;
import javax.media.rtp.RTPManager;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.ReceiveStreamListener;
import javax.media.rtp.SessionAddress;
import javax.media.rtp.SessionListener;
import javax.media.rtp.event.NewReceiveStreamEvent;
import javax.media.rtp.event.ReceiveStreamEvent;
import javax.media.rtp.event.StreamMappedEvent;

import com.sun.media.codec.audio.mp3.*;

/**
 * Classe du Lecteur audio RTP pour le client
 */
public class RTPClient2 implements ReceiveStreamListener, ControllerListener {

	// ATTRIBUTS
	// *******************************************************

	/**
	 * Singleton
	 */
	private static RTPClient2 instance;

	/**
	 * Lecteur RTP
	 */
	private Player p = null;

	/**
	 * URL écoutée
	 */
	private String url;

	// AVRECEIVE
	private String sessions = null;

	private RTPManager mgrs = null;

	private boolean dataReceived = false;

	private Object dataSync = new Object();

	// CONSTRUCTEUR
	// *******************************************************

	/**
	 * Constructeur
	 */
	public RTPClient2() {
	}

	// METHODES STATIQUES
	// *******************************************************

	/**
	 * Retourne l'instance du Singleton
	 */
	public static synchronized RTPClient2 getInstance() {
		if (instance == null)
			instance = new RTPClient2();
		return instance;
	}

	// METHODES DYNAMIQUES
	// *******************************************************

	/**
	 * Lance l'écoute d'une URL
	 * @param String url
	 */
	public void start(String url) {

		stop();
		sessions = url;
		initialize();
	}

	/**
	 * Arrête l'écoute d'une URL
	 */
	public void stop() {
//		 close the RTP session.
		if (mgrs != null) {
			mgrs.removeTargets("Closing session from AVReceive2");
			mgrs.dispose();
			mgrs = null;
		}
	}

	protected boolean initialize() {

		try {
			InetAddress ipAddr;
			SessionAddress localAddr = new SessionAddress();
			SessionAddress destAddr;

			SessionLabel session;

				// Parse the session addresses.
				try {
					session = new SessionLabel(sessions);
				} catch (IllegalArgumentException e) {
					System.err.println("Failed to parse the session address given: "+ sessions);
					return false;
				}

				/*System.err.println("  - Open RTP session for: addr: "
						+ session.addr + " port: " + session.port + " ttl: "
						+ session.ttl);*/

				mgrs = (RTPManager) RTPManager.newInstance();
				mgrs.addReceiveStreamListener(this);

				ipAddr = InetAddress.getByName(session.addr);

				if (ipAddr.isMulticastAddress()) {
					// local and remote address pairs are identical:
					localAddr = new SessionAddress(ipAddr, session.port,
							session.ttl);
					destAddr = new SessionAddress(ipAddr, session.port,
							session.ttl);
				} else {
					localAddr = new SessionAddress(InetAddress.getLocalHost(),
							session.port);
					destAddr = new SessionAddress(ipAddr, session.port);
				}

				mgrs.initialize(localAddr);

				// You can try out some other buffer size to see
				// if you can get better smoothness.
				BufferControl bc = (BufferControl) mgrs.getControl("javax.media.control.BufferControl");
				if (bc != null) bc.setBufferLength(350);

				mgrs.addTarget(destAddr);

		} catch (Exception e) {
			System.err.println("Cannot create the RTP Session: " + e.getMessage());
			return false;
		}

		// Wait for data to arrive before moving on.

		long then = System.currentTimeMillis();
		long waitingPeriod = 30000; // wait for a maximum of 30 secs.

		try {
			synchronized (dataSync) {
				while (!dataReceived && System.currentTimeMillis() - then < waitingPeriod) {
					/*if (!dataReceived) System.err.println("  - Waiting for RTP data to arrive...");*/
					dataSync.wait(1000);
				}
			}
		} catch (Exception e) {
		}

		if (!dataReceived) {
			System.err.println("No RTP data was received.");
			stop();
			return false;
		}

		return true;
	}

	/**
	 * ReceiveStreamListener
	 */
	public synchronized void update(ReceiveStreamEvent evt) {

		RTPManager mgr = (RTPManager) evt.getSource();
		ReceiveStream stream = evt.getReceiveStream(); // could be null.

		if (evt instanceof NewReceiveStreamEvent) {

			try {
				stream = ((NewReceiveStreamEvent) evt).getReceiveStream();
				DataSource ds = stream.getDataSource();

				// Find out the formats.
				RTPControl ctl = (RTPControl) ds.getControl("javax.media.rtp.RTPControl");

				/*if (ctl != null) {
					System.err.println("  - Recevied new RTP stream: " + ctl.getFormat());
				} else
					System.err.println("  - Recevied new RTP stream");*/

				// create a player by passing datasource to the Media Manager
				Player p = javax.media.Manager.createPlayer(ds);
				if (p == null)
					return;

				p.addControllerListener(this);
				p.realize();

				// Notify intialize() that a new stream had arrived.
				synchronized (dataSync) {
					dataReceived = true;
					dataSync.notifyAll();
				}

			} catch (Exception e) {
				/*System.err.println("NewReceiveStreamEvent exception "
						+ e.getMessage());
				return;*/
			}

		}

		else if (evt instanceof StreamMappedEvent) {

			if (stream != null && stream.getDataSource() != null) {
				DataSource ds = stream.getDataSource();
				// Find out the formats.
				RTPControl ctl = (RTPControl) ds.getControl("javax.media.rtp.RTPControl");
				
				//System.err.println("  - The previously unidentified stream ");
				//if (ctl != null) System.err.println("      " + ctl.getFormat());
			}
		}
	}

	/**
	 * ControllerListener for the Players.
	 */
	public synchronized void controllerUpdate(ControllerEvent ce) {

		Player p = (Player) ce.getSourceController();

		if (p != null) {
			// Get this when the internal players are realized.
			if (ce instanceof RealizeCompleteEvent) {
				p.start();
			}

			if (ce instanceof ControllerErrorEvent) {
				p.removeControllerListener(this);
			}
			//System.err.println("AVReceive2 internal error: " + ce);
		}

	}
}

/**
 * A utility class to parse the session addresses.
 */
class SessionLabel {

	public String addr = null;

	public int port;

	public int ttl = 1;

	public SessionLabel(String session) throws IllegalArgumentException {

		int off;
		String portStr = null, ttlStr = null;

		if (session != null && session.length() > 0) {
			while (session.length() > 1 && session.charAt(0) == '/')
				session = session.substring(1);

			// Now see if there's a addr specified.
			off = session.indexOf('/');
			if (off == -1) {
				if (!session.equals(""))
					addr = session;
			} else {
				addr = session.substring(0, off);
				session = session.substring(off + 1);
				// Now see if there's a port specified
				off = session.indexOf('/');
				if (off == -1) {
					if (!session.equals(""))
						portStr = session;
				} else {
					portStr = session.substring(0, off);
					session = session.substring(off + 1);
					// Now see if there's a ttl specified
					off = session.indexOf('/');
					if (off == -1) {
						if (!session.equals(""))
							ttlStr = session;
					} else {
						ttlStr = session.substring(0, off);
					}
				}
			}
		}

		if (addr == null)
			throw new IllegalArgumentException();

		if (portStr != null) {
			try {
				Integer integer = Integer.valueOf(portStr);
				if (integer != null)
					port = integer.intValue();
			} catch (Throwable t) {
				throw new IllegalArgumentException();
			}
		} else
			throw new IllegalArgumentException();

		if (ttlStr != null) {
			try {
				Integer integer = Integer.valueOf(ttlStr);
				if (integer != null)
					ttl = integer.intValue();
			} catch (Throwable t) {
				throw new IllegalArgumentException();
			}
		}
	}
}