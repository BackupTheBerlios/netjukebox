package streaming2;

import java.awt.Dimension;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.media.Codec;
import javax.media.Control;
import javax.media.Controller;
import javax.media.ControllerClosedEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Format;
import javax.media.MediaLocator;
import javax.media.NoProcessorException;
import javax.media.Owned;
import javax.media.Player;
import javax.media.Processor;
import javax.media.control.QualityControl;
import javax.media.control.TrackControl;
import javax.media.format.UnsupportedFormatException;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;
import javax.media.rtp.InvalidSessionAddressException;
import javax.media.rtp.RTPManager;
import javax.media.rtp.SendStream;
import javax.media.rtp.SessionAddress;
import javax.media.rtp.rtcp.SourceDescription;

/**
 * Flux de diffusion
 * @author Philippe Bollard
 */
public class Stream {

	private String nomFlux;
	private int portBase;
	private Vector auditeurs;
	private RTPManager rtpManager=null;
	
	private Processor processor = null;
	private RTPManager rtpMgrs[];
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
		MediaLocator locator = new MediaLocator(filename);
		
		/* A COMPLETER AVEC LE CODE DE SUPINFO*/
	}

	/**
	 * Stoppe la diffusion du flux
	 */
	public void stopper() {
	}
}