package philippe3.serveur;

import java.net.InetAddress;
import java.util.Vector;

import javax.media.ConfigureCompleteEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DataSink;
import javax.media.EndOfMediaEvent;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;
import javax.media.control.TrackControl;
import javax.media.datasink.DataSinkEvent;
import javax.media.datasink.DataSinkListener;
import javax.media.datasink.EndOfStreamEvent;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.rtp.RTPManager;
import javax.media.rtp.SendStream;
import javax.media.rtp.SessionAddress;
import javax.media.rtp.rtcp.SourceDescription;

public class RTPServer implements ControllerListener/*, DataSinkListener*/ {
	private boolean pRealized = false;
	private boolean pConfigured = false;
	private boolean cDiffuse = false;

	private String ip;
	private int port;
	private String piste;
	private String publicite;
	
	private MediaLocator OutputLocator;
	private DataSink OutputSink = null;
	private SendStream OutputStream = null;
	
	private Vector medias;
	private int mediaEnCours=0;

	public RTPServer(String ip, int port, String piste, String publicite) {

		System.out.println("Création d'un serveur RTP");
		this.ip = ip;
		this.port = port;
		this.piste = piste;
		this.publicite = publicite;
		this.medias = new Vector();
		
		// Creation du MediaLocator pour l'Adresse de destination
		OutputLocator = new MediaLocator("rtp://"+ip+":"+port+"/audio/"+piste);
		System.out.println("Serveur RTP créé");

	}

	public void programmer(Vector medias) {
		this.medias = medias;
	}
	
	public void stop() {
		cDiffuse=false;
		if (OutputSink != null) OutputSink.close();
	}
	
	
	public void diffuser() {
		
		cDiffuse=true;
		
		//Si on a des medias à diffuser
		if (medias.size()>0 && mediaEnCours<medias.size()) {
			sendStream((String)medias.elementAt(mediaEnCours++));
		}
		
		//Sinon, on diffuse de la pub
		else {
			sendStream(publicite);
		}
	}
		
	private void sendStream(String filename) {
		pRealized = false;
		pConfigured = false;
		try {
			MediaLocator src = new MediaLocator(filename);
			Processor p;
			p = Manager.createProcessor(src);
			p.addControllerListener(this);
			p.configure();
			while (!pConfigured) {
				try {
					Thread.currentThread().sleep(100L);
				} catch (InterruptedException e) {
					System.out.println("erreur : " + e);
				}
			}
			setTrackFormat(p);
			p.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW_RTP));
			p.realize();
			while (!pRealized) {
				try {
					Thread.currentThread().sleep(100L);
				} catch (InterruptedException e) {
					System.out.println("erreur : " + e);
				}
			}
			transmitSink(p);
			//transmitManager(p);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Transmet un flux audio avec un DataSink
	 */
	private void transmitSink(Processor p) {
		System.out.println("Transmission");
		p.start();
		try {
			// Creation du DataSink
			OutputSink = Manager.createDataSink(p.getDataOutput(), OutputLocator);

			//Ajout d'un Listener
			//OutputSink.addDataSinkListener(this);
			
			// Ouverture du DataSink
			OutputSink.open();

			// Demarrage du DataSink
			OutputSink.start();
			

			System.out.println("Started");
		}

		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Transmet un flux audio avec un RTPManager
	 * @param p
	 * @param port
	 */
	private void transmitManager(Processor p) {
		try {

			DataSource output = p.getDataOutput();
			
			PushBufferDataSource pbds = (PushBufferDataSource) output;
			RTPManager rtpMgr = RTPManager.newInstance();

			SessionAddress Canal = new SessionAddress(InetAddress.getLocalHost(), port);
			SessionAddress destAddr = new SessionAddress(InetAddress.getByName(ip), port);
			
			rtpMgr.initialize(Canal);
			rtpMgr.addTarget(destAddr);
			
			System.err.println("Created RTP session: " + ip + " " + port);
			
			OutputStream = rtpMgr.createSendStream(output, 0);		
			OutputStream.start();
			p.start();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * It doesn't seem to be enough to set the processor output format. This
	 * code sets the format for each track within the stream to a supported
	 * format. On my system this a 4-bit format which sounds crappy, but is
	 * recognised by the client. Without this track setting, the client barfs
	 * because the default track format is a 16-bit mpeg stream which the client
	 * doesn't understand
	 */
	private void setTrackFormat(Processor p) {
		// Get the tracks from the processor
		TrackControl[] tracks = p.getTrackControls();
		// Do we have atleast one track?
		if (tracks == null || tracks.length < 1) {
			System.out.println("Couldn't find tracks in processor");
			System.exit(1);
		}

		// Set the output content descriptor to RAW_RTP
		// This will limit the supported formats reported from
		// Track.getSupportedFormats to only valid RTP formats.
		ContentDescriptor cd = new ContentDescriptor(ContentDescriptor.RAW_RTP);
		p.setContentDescriptor(cd);
		Format supported[];
		Format chosen;
		boolean atLeastOneTrack = false;
		// Program the tracks.
		for (int i = 0; i < tracks.length; i++) {
			Format format = tracks[i].getFormat();
			if (tracks[i].isEnabled()) {
				supported = tracks[i].getSupportedFormats();
				for (int n = 0; n < supported.length; n++)
					System.out.println("Supported format: " + supported[n]);
				// We've set the output content to the RAW_RTP.
				// So all the supported formats should work with RTP.
				// We'll just pick the first one.
				if (supported.length > 0) {
					chosen = supported[0];
					tracks[i].setFormat(chosen);
					System.err
							.println("Track " + i + " is set to transmit as:");
					System.err.println("  " + chosen);
					atLeastOneTrack = true;
				} else
					tracks[i].setEnabled(false);
			} else
				tracks[i].setEnabled(false);
		}
	}

	public void controllerUpdate(ControllerEvent evt) {
		if (evt instanceof RealizeCompleteEvent) {
			pRealized = true;
		} else if (evt instanceof ConfigureCompleteEvent) {
			pConfigured = true;
		} else if (evt instanceof EndOfMediaEvent) {
			if (cDiffuse) {
				System.out.println("EVT: EndOfMedia");
				OutputSink.close();
				OutputSink = null;
				/*
				OutputStream.close();
				OutputStream = null;
				 */
				diffuser();
			}
			else {
				//System.exit(0);
			}
		} else {
			System.out.println(evt.toString());
		}
	}
	
	/*public void dataSinkUpdate(DataSinkEvent evt) {
		if (evt instanceof EndOfStreamEvent) {
			if (cDiffuse) {
				System.out.println("EVT: EndOfStream");
				OutputSink.close();
				OutputSink = null;
				diffuser();
			}
		} else {
			System.out.println(evt.toString());
		}
	}*/
}