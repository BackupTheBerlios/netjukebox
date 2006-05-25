package proto.serveur;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Journal de diffusion
 */
public class JournalDiffusion {
	
	/**
	 * Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(JournalDiffusion.class);

// ATTRIBUTS
//**********************************
	
	/**
	 * Identifiant
	 */
	private int id;

	/**
	 * Date
	 */
	private Date date;

// CONSTRUCTEUR
//***********************************
	
	public JournalDiffusion() {
		
	}
	
// METHODES STATIQUES
//***********************************
	
// METHODES DYNAMIQUES
//***********************************
	
	/**
	 * Création
	 */
	public void creer() {
		logger.debug("Démarrage: creer");
		// your code here
		logger.debug("Arrêt: creer");
	}

	/**
	 * Archivage
	 */
	public void archiver() {
		logger.debug("Démarrage: archiver");
		// your code here
		logger.debug("Arrêt: archiver");
	}

	/**
	 * Consultation
	 */
	public void consulter() {
		logger.debug("Démarrage: consulter");
		// your code here
		logger.debug("Arrêt: consulter");
	}
}