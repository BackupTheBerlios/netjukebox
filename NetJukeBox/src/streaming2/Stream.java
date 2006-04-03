package streaming2;

import java.net.InetAddress;
import java.util.Vector;

import javax.media.ConfigureCompleteEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;
import javax.media.control.TrackControl;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.rtp.RTPManager;
import javax.media.rtp.SendStream;
import javax.media.rtp.SessionAddress;
import javax.media.rtp.rtcp.SourceDescription;

/**
 * Flux de diffusion
 * @author Philippe Bollard
 */
public class Stream implements ControllerListener {

	private String nomFlux;
	private int portBase;
	private Vector auditeurs;
	private RTPManager rtpManager=null;
	
	private boolean realized = false;
	private boolean configured = false;
	
	private Processor processor = null;
	private DataSource dataOutput = null;

	/**
	 * Crée un flux de diffusion
	 * @param nomFlux
	 * @param portBase
	 */
	public Stream(String nomFlux, int portBase) {

		this.nomFlux = nomFlux;
		this.portBase = portBase;
		
		//on ajoute un premier auditeur local
		this.ajouterAuditeur("127.0.0.1");
	}
	
	/**
	 * Retourne le nom du canal
	 * @return
	 */
	public String getNomFlux() {
		return nomFlux;
	}
	
	/**
	 * Ajoute un auditeur et lance la session correspondante si diffusion en cours
	 * @param ip
	 */
	public void ajouterAuditeur(String ip) {
		Auditeur auditeur = new Auditeur(ip);

		//Si le flux est en diffusion, on lance la session pour le nouvel auditeur
		if (rtpManager!=null) {
			try {
				SessionAddress dest = new SessionAddress(InetAddress.getByName(ip),this.portBase+2);
				rtpManager.addTarget(dest);
				auditeur.setFlux(rtpManager.createSendStream(dataOutput,0));
				auditeur.startFlux();
				auditeurs.add(auditeur);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		auditeurs.addElement(auditeur);
	}
	
	/**
	 * Retire un auditeur et stoppe la session si diffusion en cours
	 * @param ip
	 */
	public void retirerAuditeur(String ip) {
		
		//On cherche l'auditeur
		int indice=0;
		while (indice<auditeurs.size() && ((Auditeur)auditeurs.elementAt(indice)).getIp()!=ip) indice++;
		
		//Si l'auditeur a été trouvé
		if (indice<auditeurs.size()) {
			((Auditeur)auditeurs.elementAt(indice)).stopFlux();
			auditeurs.removeElementAt(indice);
		}
		else {
			System.out.println("ERREUR: Auditeur "+ip+" introuvable. Retrait impossible.");
		}
		
	}

	/**
	 * Diffuse le fichier donné sur le flux
	 * @param filename
	 */
	public void diffuser(String filename) {
		MediaLocator src = new MediaLocator(filename);

		try {
			Processor p = Manager.createProcessor(src);
			p.addControllerListener(this);
			p.configure();
			while (! configured) {
				try {
					Thread.currentThread().sleep(100L);;
				} catch (InterruptedException e) {
					// ignore
				}
			}
			setTrackFormat(p);
			p.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW_RTP));
			p.realize();
			while (! realized) {
				try {
					Thread.currentThread().sleep(100L);;
					}
				catch (InterruptedException e) {
					System.out.println("erreur : " + e);
				}
			}
			this.transmit(p);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	* It doesn't seem to be enough to set the processor output format.
	* This code sets the format for each track within the stream to a 
	* supported format. On my system this a 4-bit format which sounds
	* crappy, but is recognised by the client. 
	* Without this track setting, the client barfs because the default
	* track format is a 16-bit mpeg stream which the client doesn't
	* understand
	*/
	private void setTrackFormat(Processor p) {
		// Get the tracks from the processor
		TrackControl [] tracks = p.getTrackControls();
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
					System.err.println("Track " + i + " is set to transmit as:");
					System.err.println("  " + chosen);
					atLeastOneTrack = true;
				}
				else
					tracks[i].setEnabled(false);
			}
				else
					tracks[i].setEnabled(false);
		}
	}

	/**
	 * This code uses the RTP Manager to handle a session
	 * stream. It is more complex than the simpler version
	 * below, but can also handle more complex streams
	 */
	private void transmit(Processor p) {
		try {
			DataSource output = p.getDataOutput();
			PushBufferDataSource pbds = (PushBufferDataSource) output;
			rtpManager = RTPManager.newInstance();
			SessionAddress localAddr, destAddr;
			SendStream sendStream;
			SourceDescription srcDesList[];

			localAddr = new SessionAddress( InetAddress.getLocalHost(), portBase);
			rtpManager.initialize(localAddr);
			
			for(int i=0; i<auditeurs.size(); i++) {
				Auditeur auditeur = (Auditeur)auditeurs.elementAt(i);
				destAddr = new SessionAddress( InetAddress.getByName(auditeur.getIp()), portBase+2);
				rtpManager.addTarget(destAddr);
				System.err.println( "Created RTP session: " + auditeur.getIp() + " " + (portBase+2));
				auditeur.setFlux(rtpManager.createSendStream(output, 0));		
				auditeur.startFlux();
				auditeurs.setElementAt(auditeur, i);
			}
			p.start();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
 
	public synchronized void controllerUpdate(ControllerEvent evt) {
		if (evt instanceof RealizeCompleteEvent) {
			realized = true;
		} 
		else
			if (evt instanceof ConfigureCompleteEvent) {
				configured = true;
			}
			else
				if (evt instanceof EndOfMediaEvent) {
					System.exit(0);
				}
				else {
					System.out.println(evt.toString());
				}
	}

	/**
	 * Stoppe la diffusion du flux
	 */
	public void stopper() {
	}
}