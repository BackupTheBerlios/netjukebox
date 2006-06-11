package proto.serveur;

import java.net.InetAddress;
import java.util.Enumeration;
import java.util.Hashtable;
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
import javax.media.format.AudioFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.rtp.RTPManager;
import javax.media.rtp.SendStream;
import javax.media.rtp.SessionAddress;

/**
 * RTP Server : serveur de streaming pour un canal
 */
public class RTPServer implements ControllerListener {
	
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
	 * Etat du canal : DIFFUSE PUBLICITE
	 */
	private boolean cDiffusePub = false;

	/**
	 * Adresse IP de diffusion
	 */
	private String ip;
	
	/**
	 * Port de diffusion
	 */
	private int port;
	
	/**
	 * Nom/Numéro de la piste RTP
	 */
	private String piste;
	
	/**
	 * Nom du fichier de publicité
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
	 * Liste des noms de fichiers à diffuser
	 */
	private Vector medias;
	
	/**
	 * Indice du média en cours de diffusion
	 */
	private int mediaEnCours=0;
	
	/**
	 * Canal associé
	 */
	private Canal canal;
	
	/**
	 * Processor RTP
	 */
	private Processor processor;
	
	/**
	 * RTPManager
	 */
	private RTPManager rtpMgr= null;
	
	/**
	 * Auditeurs
	 */
	private Hashtable auditeurs = new Hashtable();
	
	/**
	 * OutputSinks
	 */
	private Hashtable outputSinks = new Hashtable();

// CONSTRUCTEURS
//***************************************************	
	
	/**
	 * Constructeur
	 * @param ip
	 * @param port
	 * @param piste
	 */
	public RTPServer(String ip, int port, String publicite, Canal canal) {

		System.out.println("Création d'un serveur RTP");
		this.ip = ip;
		this.port = port;
		this.piste = "1";
		this.publicite = publicite;
		this.medias = new Vector();
		this.canal = canal;
		this.auditeurs = canal.getAuditeurs();
		
		// Creation du MediaLocator pour l'Adresse de destination
		OutputLocator = new MediaLocator("rtp://"+ip+":"+port+"/audio/"+piste);
		System.out.println("Serveur RTP créé");

	}

// METHODES STATIQUES
//***************************************************	
	
// METHODES DYNAMIQUES	
//***************************************************
	
	/**
	 * Retourne l'URL
	 */
	public String getUrl() {
		try {
			String ipServeur = InetAddress.getLocalHost().getHostAddress();
			//return "rtp://"+ipServeur+":"+port+"/audio/"+piste;
			return ipServeur+"/"+port;
		} catch (Exception e) {
			System.err.println("ERREUR: "+e);
			return null;
		}
		
	}
	
	/**
	 * Programmer une liste de médias à diffuser
	 * @param medias
	 */
	public void programmer(Vector medias) {
		
		//Si on a encore des médias à diffuser
		if (cDiffuse /*&& !cDiffusePub*/ && medias.size()>0 && mediaEnCours<medias.size()) {
			
			//On ajoute les nouveaux médias à la liste actuelle
			this.medias.addAll(medias);
		}
		
		//Si on ne diffuse rien actuellement
		else {
			
			//On stoppe la pub
			stop();
			
			//On définit la liste de médias à diffuser
			this.medias = medias;
			this.mediaEnCours = 0;
			
			//On lance la diffusion
			diffuser();
		}
	}
	
	/**
	 * Arrêter la diffusion
	 */
	public void stop() {
		System.out.println("Diffusion stoppée");
		
		if (OutputSink != null) OutputSink.close();
		Enumeration logins = outputSinks.keys();
		while (logins.hasMoreElements()) {
			String login = (String)logins.nextElement();
			((DataSink)outputSinks.get(login)).close();
			outputSinks.remove(login);
		}
		
		//if (OutputStream != null) OutputStream.close();
		
		if (processor!=null) {
			processor.stop();
			processor.close();
			processor = null;
			pRealized = false;
			pConfigured = false;
		}
		
		cDiffuse=false;
	}
	
	/**
	 * Lancer la diffusion
	 */
	public void diffuser() {
		System.out.println("Diffusion lancée");
		cDiffuse=true;
		
		//Si on a des medias à diffuser
		if (medias.size()>0 && mediaEnCours<medias.size()) {
			cDiffusePub=false;
			Document d = (Document)medias.elementAt(mediaEnCours++);
			d.startDiffusion(canal.getId());
			sendStream(d.getFichier());
		}
		
		//Sinon, on diffuse de la pub
		else {
			cDiffusePub=true;
			sendStream(publicite);
		}
	}
	
	/**
	 * Préraration du processor
	 * @param filename
	 */
	private void sendStream(String filename) {
		System.out.println("Diffusion de "+filename);
		pRealized = false;
		pConfigured = false;
		try {
			MediaLocator src;
			if (!filename.startsWith("file:/")) {
				src = new MediaLocator("file:/"+filename);
			}
			else {
				src = new MediaLocator(filename);
			}
			
			processor = Manager.createProcessor(src);
			processor.addControllerListener(this);
			processor.configure();
			while (!pConfigured) {
				try {
					Thread.currentThread().sleep(100L);
				} catch (InterruptedException e) {
					System.out.println("erreur : " + e);
				}
			}
			setTrackFormat();
			processor.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW_RTP));
			processor.realize();
			while (!pRealized) {
				try {
					Thread.currentThread().sleep(100L);
				} catch (InterruptedException e) {
					System.out.println("erreur : " + e);
				}
			}
			transmitSink();
			//transmitManager();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Transmet un flux audio avec un DataSink
	 * @param p
	 */
	private void transmitSink() {
		System.out.println("Transmission");
		processor.start();
		try {
			
			// Creation du DataSink
			OutputSink = Manager.createDataSink(processor.getDataOutput(), OutputLocator);

			//Ajout d'un Listener
			//OutputSink.addDataSinkListener(this);
			
			// Ouverture du DataSink
			OutputSink.open();

			// Demarrage du DataSink
			OutputSink.start();
			
			
			Enumeration logins = auditeurs.keys();
			while (logins.hasMoreElements()) {
				String login = (String)logins.nextElement();
				MediaLocator ml = new MediaLocator("rtp://"+(String)auditeurs.get(login)+":"+port+"/audio/"+piste);
				DataSink ds = Manager.createDataSink(processor.getDataOutput(), ml);
				ds.open();
				ds.start();
				outputSinks.put(login, ds);
			}

			System.out.println("Started");
		}

		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Transmet un flux audio avec un RTPManager
	 * @param p
	 */
	private void transmitManager() {
		try {
			processor.start();
			DataSource output = processor.getDataOutput();
			
			PushBufferDataSource pbds = (PushBufferDataSource) output;
			
			if (rtpMgr==null) {
				rtpMgr = RTPManager.newInstance();
	
				SessionAddress canal = new SessionAddress(InetAddress.getByName(ip), port);
				rtpMgr.initialize(canal);
				
				if (auditeurs.size()>0) {
					Enumeration ipAuditeurs = auditeurs.elements();
					while (ipAuditeurs.hasMoreElements()) {
						SessionAddress destAddr = new SessionAddress(InetAddress.getByName((String)ipAuditeurs.nextElement()), port);
						rtpMgr.addTarget(destAddr);
					}
				} else {
					SessionAddress destAddr = new SessionAddress(InetAddress.getByName(ip), port-1);
					rtpMgr.addTarget(destAddr);
				}
				
				SessionAddress destAddr = new SessionAddress(InetAddress.getByName("83.156.77.248"), port);
				rtpMgr.addTarget(destAddr);
				
				System.err.println("Created RTP session: " + ip + " " + port);
			}			
			
			OutputStream = rtpMgr.createSendStream(output, 0);		
			OutputStream.start();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void connecterAuditeur(String login, String ip) {
		auditeurs.put(login, ip);
		if (cDiffuse) {
			try {
				MediaLocator ml = new MediaLocator("rtp://"+ip+":"+port+"/audio/"+piste);
				DataSink ds = Manager.createDataSink(processor.getDataOutput(), ml);
				ds.open();
				ds.start();
				outputSinks.put(login, ds);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			/*//VERSION RTPMANAGER 
			try {
				SessionAddress destAddr = new SessionAddress(InetAddress.getByName(ip), port);
				rtpMgr.addTarget(destAddr);
			}
			catch(Exception e) {
				e.printStackTrace();
			}*/
		}
	}
	
	public void deconnecterAuditeur(String login) {
		if (auditeurs.containsKey(login)) {
			if (cDiffuse) {
				DataSink ds = (DataSink)outputSinks.get(login);
				ds.close();
				outputSinks.remove(login);
				
				
				/*//VERSION RTPMANAGER 
				try {
					SessionAddress destAddr = new SessionAddress(InetAddress.getByName((String)auditeurs.get(login)), port);
					rtpMgr.removeTarget(destAddr, "CONNEXION CLOSED");
				}
				catch(Exception e) {
					e.printStackTrace();
				}*/
			}
			auditeurs.remove(login);
		}
	}

	/**
	 * Sélection du format des pistes du média
	 * @param p
	 */
	private void setTrackFormat() {
		// Get the tracks from the processor
		TrackControl[] tracks = processor.getTrackControls();
		// Do we have atleast one track?
		if (tracks == null || tracks.length < 1) {
			System.out.println("Couldn't find tracks in processor");
			System.exit(1);
		}

		// Set the output content descriptor to RAW_RTP
		// This will limit the supported formats reported from
		// Track.getSupportedFormats to only valid RTP formats.
		ContentDescriptor cd = new ContentDescriptor(ContentDescriptor.RAW_RTP);
		processor.setContentDescriptor(cd);
		Format supported[];
		Format chosen;
		boolean atLeastOneTrack = false;
		// Program the tracks.
		for (int i = 0; i < tracks.length; i++) {
			AudioFormat format = (AudioFormat)tracks[i].getFormat();
			System.out.println("File Format: "+format);
			
			if (tracks[i].isEnabled()) {
				supported = tracks[i].getSupportedFormats();
				
				int indiceFormat = 0;
				for (int n = 0; n < supported.length; n++) {
					AudioFormat f = (AudioFormat)supported[n];
					System.out.println("Supported format: " + f);
					
					//-- PATCH MEILLEUR FORMAT POSSIBLE --
					//if (indiceFormat==0 && f.getSampleRate()==format.getSampleRate()) indiceFormat = n;
					//-------------- PATCH ---------------
				}
				// We've set the output content to the RAW_RTP.
				// So all the supported formats should work with RTP.
				// We'll just pick the first one.
				if (supported.length > 0) {
					chosen = supported[indiceFormat];
					tracks[i].setFormat(chosen);
					System.err.println("Track " + i + " is set to transmit as:");
					System.err.println("  " + chosen);
					atLeastOneTrack = true;
				} else
					tracks[i].setEnabled(false);
			} else
				tracks[i].setEnabled(false);
		}
	}

	/**
	 * Gestion des évenements
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
				
				//On ferme le flux (version outputSink)
				OutputSink.close();
				OutputSink = null;
				Enumeration logins = outputSinks.keys();
				while (logins.hasMoreElements()) {
					String login = (String)logins.nextElement();
					((DataSink)outputSinks.get(login)).close();
					outputSinks.remove(login);
				}
				
				//On ferme le flux (version outputStream)
				//OutputStream.close();
				//OutputStream = null;
				
				//Si on diffuse autre chose que la pub
				if (!cDiffusePub && medias.size()>0 && (mediaEnCours-1)<medias.size()) {
					
					//On signale la fin de diffusion au document
					Document d = (Document)medias.elementAt(mediaEnCours-1);
					d.stopDiffusion(canal.getId(), canal.getAudimat());
				}
				
				//On actualise le compteur d'audimat du canal
				canal.updateAudimat();
				
				//On passe au média suivant
				diffuser();
			}
			else {
				//System.exit(0);
			}
		} else {
			System.out.println(evt.toString());
		}
	}
}