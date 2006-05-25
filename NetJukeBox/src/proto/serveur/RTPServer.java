package proto.serveur;

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
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.rtp.RTPManager;
import javax.media.rtp.SendStream;
import javax.media.rtp.SessionAddress;

import org.apache.log4j.Logger;

/**
 * RTP Server : serveur de streaming pour un canal
 */
public class RTPServer implements ControllerListener {
	/**
	 * Cr�e le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(RTPServer.class);
// ATTRIBUTS	
//***************************************************	
	/**
	 * Etat du processor : REALIZED
	 */
	private boolean pRealized = false;
	
	/**
	 * Etat du processor : CONFIGURED
	 */
	private boolean pConfigured = false;
	
	/**
	 * Etat du canal : DIFFUSE
	 */
	private boolean cDiffuse = false;

	/**
	 * Adresse IP de diffusion
	 */
	private String ip;
	
	/**
	 * Port de diffusion
	 */
	private int port;
	
	/**
	 * Nom/Num�ro de la piste RTP
	 */
	private String piste;
	
	/**
	 * Nom du fichier de publicit�
	 */
	private String publicite;
	
	/**
	 * Adresse RTP de diffusion (avec DataSink)
	 */
	private MediaLocator OutputLocator;
	
	/**
	 * Gestionnaire du flux (avec DataSink)
	 */
	private DataSink OutputSink = null;
	
	/**
	 * Gestionnaire du flux (avec RTPManager)
	 */
	private SendStream OutputStream = null;
	
	/**
	 * Liste des noms de fichiers � diffuser
	 */
	private Vector medias;
	
	/**
	 * Indice du m�dia en cours de diffusion
	 */
	private int mediaEnCours=0;

// CONSTRUCTEURS
//***************************************************	
	
	/**
	 * Constructeur
	 * @param ip
	 * @param port
	 * @param piste
	 */
	public RTPServer(String ip, int port, String publicite) {
		logger.info("Cr�ation d'un serveur RTP");
		this.ip = ip;
		this.port = port;
		this.piste = "1";
		this.publicite = publicite;
		//this.publicite = "file://home/philippe/njb/pub.mp3";
		this.medias = new Vector();
		
		// Creation du MediaLocator pour l'Adresse de destination
		OutputLocator = new MediaLocator("rtp://"+ip+":"+port+"/audio/"+piste);
		logger.info("Serveur RTP cr��");

	}

	/**
	 * Constructeur �tendu (avec publicit�)
	 * @param ip
	 * @param port
	 * @param piste
	 * @param publicite
	 */
/*	public RTPServer(String ip, int port, String piste, String publicite) {

		logger.info("Cr�ation d'un serveur RTP");
		this.ip = ip;
		this.port = port;
		this.piste = piste;
		this.publicite = publicite;
		this.medias = new Vector();
		
		// Creation du MediaLocator pour l'Adresse de destination
		OutputLocator = new MediaLocator("rtp://"+ip+":"+port+"/audio/"+piste);
		logger.info("Serveur RTP cr��");

	}
*/

// METHODES STATIQUES
//***************************************************	
	
// METHODES DYNAMIQUES	
//***************************************************
	
	/**
	 * Retourne l'URL
	 */
	public /*pure*/ String getUrl() {
		try {
			String ipServeur = InetAddress.getLocalHost().getHostAddress();
			//return "rtp://"+ipServeur+":"+port+"/audio/"+piste;
			return ipServeur+"/"+port;
		} catch (Exception e) {
			logger.error("ERREUR: "+e);
			return null;
		}
		
	}
	
	/**
	 * Programmer une liste de m�dias � diffuser
	 * @param medias
	 */
	public void programmer(Vector medias) {
		logger.debug("D�marrage: programmer");
		//Si on a encore des m�dias � diffuser
		if (cDiffuse && medias.size()>0 && mediaEnCours<medias.size()) {
			
			//On ajoute les nouveaux m�dias � la liste actuelle
			this.medias.addAll(medias);
			logger.debug("Arr�t: programmer");
		}
		
		//Si on ne diffuse rien actuellement
		else {
			//On stoppe la pub
			stop();
						
			//On d�finit la liste de m�dias � diffuser
			this.medias = medias;
			this.mediaEnCours = 0;
			
			//On lance la diffusion
			diffuser();
			logger.debug("Arr�t: programmer");
		}
	}
	
	/**
	 * Arr�ter la diffusion
	 */
	public void stop() {
		logger.debug("D�marrage: stop");
		logger.info("Diffusion stopp�e");
		cDiffuse=false;
		if (OutputSink != null) OutputSink.close();
		//if (OutputStream != null) OutputStream.close();
		logger.debug("Arr�t: stop");
	}
	
	/**
	 * Lancer la diffusion
	 */
	public void diffuser() {
		logger.debug("D�marrage: diffuser");
		logger.info("Diffusion lanc�e");
		cDiffuse=true;
		
		//Si on a des medias � diffuser
		if (medias.size()>0 && mediaEnCours<medias.size()) {
			sendStream((String)medias.elementAt(mediaEnCours++));
			logger.debug("Arr�t: diffuser");
		}
		
		//Sinon, on diffuse de la pub
		else {
			sendStream(publicite);
			logger.debug("Arr�t: diffuser");
		}
	}
	
	/**
	 * Pr�raration du processor
	 * @param filename
	 */
	private void sendStream(String filename) {
		logger.debug("D�marrage: sendStream");
		logger.info("Diffusion de "+filename);
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
					logger.error("ERREUR:", e);
					logger.debug("Arr�t: sendStream");
				}
			}
			setTrackFormat(p);
			p.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW/*_RTP*/));
			p.realize();
			while (!pRealized) {
				try {
					Thread.currentThread().sleep(100L);
				} catch (InterruptedException e) {
					logger.error("ERREUR:", e);
					logger.debug("Arr�t: sendStream");
				}
			}
			transmitSink(p);
			logger.debug("Arr�t: sendStream");
			//transmitManager(p);
		} catch (Exception e) {
			logger.error("ERREUR:", e);
			logger.debug("Arr�t: sendStream");
			System.exit(1);
			
		}
	}

	/**
	 * Transmet un flux audio avec un DataSink
	 * @param p
	 */
	private void transmitSink(Processor p) {
		logger.debug("D�marrage: transmitSink");
		logger.info("Transmission d'un flux audio avec un DataSink");
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
			logger.info("Transmission lanc�e!");
			logger.debug("Arr�t: transmitSink");
			
		}

		catch (Exception e) {
			logger.error("ERREUR:", e);
			logger.debug("Arr�t: transmitSink");
		}
	}
	
	/**
	 * Transmet un flux audio avec un RTPManager
	 * @param p
	 */
	private void transmitManager(Processor p) {
		logger.debug("D�marrage: transmitManager");
		logger.info("Transmission d'un flux audio avec un RTPManager");
		try {

			DataSource output = p.getDataOutput();
			
			PushBufferDataSource pbds = (PushBufferDataSource) output;
			RTPManager rtpMgr = RTPManager.newInstance();

			SessionAddress canal = new SessionAddress(InetAddress.getLocalHost(), port);
			SessionAddress destAddr = new SessionAddress(InetAddress.getByName(ip), port);
			
			rtpMgr.initialize(canal);
			rtpMgr.addTarget(destAddr);
			
			logger.error("Created RTP session: " + ip + " " + port);
			
			OutputStream = rtpMgr.createSendStream(output, 0);		
			OutputStream.start();
			p.start();
			logger.info("Transmission lanc�e!");
			logger.debug("Arr�t: transmitManager");
		}
		catch(Exception e) {
			logger.error("ERREUR:", e);
			logger.debug("Arr�t: transmitManager");
		}
	}

	/**
	 * S�lection du format des pistes du m�dia
	 * @param p
	 */
	private void setTrackFormat(Processor p) {
		logger.debug("D�marrage: setTrackFormat");
		// Get the tracks from the processor
		TrackControl[] tracks = p.getTrackControls();
		// Do we have atleast one track?
		if (tracks == null || tracks.length < 1) {
			logger.info("Couldn't find tracks in processor");
			logger.debug("Arr�t: setTrackFormat");
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
			logger.info("File Format: "+format);
			if (tracks[i].isEnabled()) {
				supported = tracks[i].getSupportedFormats();
				for (int n = 0; n < supported.length; n++)
					logger.info("Supported format: " + supported[n]);
				// We've set the output content to the RAW_RTP.
				// So all the supported formats should work with RTP.
				// We'll just pick the first one.
				if (supported.length > 0) {
					chosen = supported[0];
					tracks[i].setFormat(chosen);
					logger.error("Track " + i + " is set to transmit as:");
					logger.error("  " + chosen);
					logger.debug("Arr�t: setTrackFormat");
					atLeastOneTrack = true;
				} else
					logger.debug("Arr�t: setTrackFormat");
					tracks[i].setEnabled(false);
			} else
				logger.debug("Arr�t: setTrackFormat");
				tracks[i].setEnabled(false);
		}
	}

	/**
	 * Gestion des �venements
	 * @param evt
	 */
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
				
				//OutputStream.close();
				//OutputStream = null;
				
				diffuser();
			}
			else {
				//System.exit(0);
			}
		} else {
			logger.info(evt.toString());
		}
	}
}