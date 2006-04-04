package philippe1.streaming;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoProcessorException;
import javax.media.Processor;
import javax.media.control.TrackControl;
import javax.media.format.UnsupportedFormatException;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.rtp.InvalidSessionAddressException;
import javax.media.rtp.RTPManager;
import javax.media.rtp.SendStream;
import javax.media.rtp.SessionAddress;
//import com.sun.media.protocol.avi.DataSource;


public class Stream2 {

	// Attributs
	private String filename;
	private MediaLocator locator;
	private String ipAddress;
	private int portBase;
	private Processor processor = null;
	private RTPManager rtpMgrs[];
	private DataSource dataOutput = null;
	private Processor FichierCessor;

	public Stream2(String filename, String ipAddress, int portBase) {

		this.filename = filename;
		this.locator = new MediaLocator(filename);
		this.ipAddress = ipAddress;
		this.portBase = portBase;
		
		//Declaration du Processor
		this.FichierCessor = null;
		try {
			// Creation du Processor à partir du Medialocator
			this.FichierCessor = Manager.createProcessor(this.locator);
			this.configure(this.FichierCessor);
			this.SetSupportedFormat(this.FichierCessor);
			this.realize(this.FichierCessor);
			this.Demarre(this.FichierCessor);
		}

		catch (IOException e) {
			System.out.println("Erreur : " + e.getMessage());
		} catch (NoProcessorException e) {
			System.out.println("Erreur : " + e.getMessage());
		}
	}

	public void debut() {
		// Entrée de l'adresse du fichier
		String FichierAdresse = "file://file.mp3";

		// Création du MédiaLocator à partir de ce fichier
		MediaLocator FichierLocator = new MediaLocator(FichierAdresse);

		// Declaration du Processor
		Processor FichierCessor = null;
		try {
			// Creation du Processor à partir du Medialocator
			FichierCessor = Manager.createProcessor(FichierLocator);
		}

		catch (IOException e) {
			System.out.println("Erreur : " + e.getMessage());
		} catch (NoProcessorException e) {
			System.out.println("Erreur : " + e.getMessage());
		}
	}

	public Processor configure(Processor p) {

		// Attendre tant que le Processor n'est pas configuré.
		while (p.getState() < Processor.Configured) {
			// Configuration du Processor
			p.configure();
		}
		return p;
	}

	public void SetSupportedFormat(Processor p) {

		// On met la description du contenu de sortie à RAW_RTP
		// pour limiter les formats supportés
		ContentDescriptor cd = new ContentDescriptor(ContentDescriptor.RAW_RTP);
		p.setContentDescriptor(cd);

		// On obtient les différentes pistes du processor
		TrackControl track[] = p.getTrackControls();

		for (int i = 0; i < track.length; i++) {

			// on obtient les formats supportés pour cette piste
			Format suppFormats[] = track[i].getSupportedFormats();

			// Si il y a au moins un format supporté
			// alors on met le premier

			if (suppFormats.length > 0)
				track[i].setFormat(suppFormats[0]);
			else
				track[i].setEnabled(false);
		}
	}

	public Processor realize(Processor p) {

		// Attendre tant que le Processor n'est pas réalisé.
		while (p.getState() < Processor.Realized) {
			// Réalisation du Processor
			p.realize();
		}
		return p;
	}

	public void Demarre(Processor p) {
		// Demarrage du Processor
		p.start();
	}

	public void createRTPManager(Processor p) {

		// Creation du DataSource correspondant au Processor
		DataSource OutputSource = p.getDataOutput();

		// Nouvelle Instance d'un RTPManager
		RTPManager rtpm = RTPManager.newInstance();

		try {

			// Création d'une SessionAddress
			// correspondant à l'adresse locale

			SessionAddress localaddr = new SessionAddress(InetAddress
					.getLocalHost(), 40000);

			// Initialisation du RTPManager
			// à partir de la SessionAddresse locale

			rtpm.initialize(localaddr);

			// Création d'une SessionAddress
			// correspondant à l'adresse de destination

			SessionAddress destaddr = new SessionAddress(InetAddress
					.getByName("127.0.0.1"), 22224);

			// Ajout de cette SessionAddress dans le RTPManager
			rtpm.addTarget(destaddr);

			// Creation d'un SendStream à partir du DataSource
			SendStream ss2 = rtpm.createSendStream(OutputSource, 0);

			// Demarrage du SendStream
			ss2.start();
			System.out.println("Started");

		} catch (UnknownHostException e) {
		} catch (IOException e) {
		} catch (InvalidSessionAddressException e) {
		}

		catch (UnsupportedFormatException e) {
		}

	}

	public void createRTPManager2(Processor p) {

		// Creation du DataSource correspondant au Processor
		DataSource OutputSource = p.getDataOutput();

		// Nouvelle Instance d'un RTPManager
		RTPManager rtpm = RTPManager.newInstance();

		try {

			// Création d'une SessionAddress
			// correspondant à l'adresse locale
			SessionAddress localaddr = new SessionAddress(InetAddress
					.getLocalHost(), 40000);
			rtpm.initialize(localaddr);

			// Création d'une SessionAddress
			// correspondant à la première adresse de destination
			SessionAddress destaddr1 = new SessionAddress(InetAddress
					.getByName("192.168.3.6"), 22224);

			// Ajout de la première SessionAddress dans le RTPManager
			rtpm.addTarget(destaddr1);

			// Creation d'un premier SendStream à partir du DataSource
			// Ce SendStream enverra le flux
			// à la première adresse de destination
			SendStream ss = rtpm.createSendStream(OutputSource, 0);

			// Demarrage du premier SendStream
			ss.start();

			// Création d'une SessionAddress
			// correspondant à la seconde adresse de destination
			SessionAddress destaddr2 = new SessionAddress(InetAddress
					.getByName("192.168.3.2"), 22224);

			// Ajout de la seconde SessionAddress dans le RTPManager
			rtpm.addTarget(destaddr2);

			// Creation d'un second SendStream à partir du DataSource
			// Ce SendStream enverra le flux
			// à la première adresse de destination
			SendStream ss2 = rtpm.createSendStream(OutputSource, 0);

			// Demarrage du second SendStream
			ss2.start();
			System.out.println("Started");

		} catch (UnknownHostException e) {
		} catch (IOException e) {
		} catch (InvalidSessionAddressException e) {
		} catch (UnsupportedFormatException e) {
		}

	}
}
