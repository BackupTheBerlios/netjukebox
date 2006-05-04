package proto.client;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;

/**
 * Classe du Lecteur audio RTP pour le client
 */
public class RTPClient  implements ControllerListener {

	// ATTRIBUTS
	// *******************************************************

	/**
	 * Singleton
	 */
	private static RTPClient instance;

	/**
	 * Lecteur RTP
	 */
	private Player p = null;

	/**
	 * URL �cout�e
	 */
	private String url;

	// CONSTRUCTEUR
	// *******************************************************

	/**
	 * Constructeur
	 */
	public RTPClient() {
	}

	// METHODES STATIQUES
	// *******************************************************

	/**
	 * Retourne l'instance du Singleton
	 */
	public static synchronized RTPClient getInstance() {
		if (instance == null)
			instance = new RTPClient();
		return instance;
	}

	// METHODES DYNAMIQUES
	// *******************************************************

	/**
	 * Lance l'�coute d'une URL
	 * 
	 * @param String
	 *            url
	 */
	public void start(String url) {

		// On arr�te le player s'il �coute d�j� une URL
		this.stop();

		// On met � jour l'URL
		this.url = url;

		System.out.println(url);

		try {
			MediaLocator src = new MediaLocator(url);
			 p = Manager.createPlayer(src);
			//p = Manager.createRealizedPlayer(src);
			 p.addControllerListener(this);
			p.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Arr�te l'�coute d'une URL
	 */
	public void stop() {
		if (p != null) {
			p.stop();
			p.close();
			p = null;
		}
	}

	/**
	 * Gestionnaire d'�v�nements
	 */
	public synchronized void controllerUpdate(ControllerEvent evt) {
		if (evt instanceof EndOfMediaEvent) {
			System.out.println("Fin du m�dia");
			// start(url);
		} else {
			System.out.println(evt.toString());
		}
	}
}