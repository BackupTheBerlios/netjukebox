package proto.client;

import java.io.*;
import javax.media.*;
import javax.media.format.*;
import javax.media.protocol.*;

import proto.serveur.Jdbc;

/**
 * Classe du Lecteur audio RTP pour le client
 */
public class RTPClient implements ControllerListener {

	
// ATTRIBUTS
//*******************************************************
	
	/**
	 * Singleton
	 */
	private static RTPClient instance;
	
	/**
	 * Lecteur RTP
	 */
	private Player p = null;
	
	
// CONSTRUCTEUR
//*******************************************************
	
	/**
	 * Constructeur
	 */
	public RTPClient() {
	}
	
	
// METHODES STATIQUES
//*******************************************************
	
	/**
     *	Retourne l'instance du Singleton
     */
	public static synchronized RTPClient getInstance(){
		if (instance == null)
			instance = new RTPClient();
		return instance;
	}
	
// METHODES DYNAMIQUES
//*******************************************************
	
	/**
	 * Lance l'écoute d'une URL
	 * @param String url
	 */
	public void start(String url) {
		
		//On arrête le player s'il écoute déjà une URL
		this.stop();
		
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
	
	/**
	 * Arrête l'écoute d'une URL
	 */
	public void stop() {
		if (p!=null) {
			p.stop();
			p.close();
			p = null;
		}
	}

	/**
	 * Gestionnaire d'évènements
	 */
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