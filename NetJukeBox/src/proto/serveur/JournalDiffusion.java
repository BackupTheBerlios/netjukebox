package proto.serveur;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Journal de diffusion
 */
public class JournalDiffusion {
	
	/**
	 * Cr�e le logger de la classe
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
	 * Cr�ation
	 */
	public void creer() {
		logger.debug("D�marrage: creer");
		// your code here
		logger.debug("Arr�t: creer");
	}

	/**
	 * Archivage
	 */
	public void archiver() {
		logger.debug("D�marrage: archiver");
		// your code here
		logger.debug("Arr�t: archiver");
	}

	/**
	 * Consultation
	 */
	public void consulter() {
		logger.debug("D�marrage: consulter");
		// your code here
		logger.debug("Arr�t: consulter");
	}
}