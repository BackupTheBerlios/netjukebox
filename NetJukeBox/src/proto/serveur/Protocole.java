package proto.serveur;

import org.apache.log4j.Logger;

/**
 * Protocole
 */
public class Protocole {
	/**
	 * Cr�e le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(Protocole.class);

// ATTRIBUTS
//**********************************
	
	/**
	 * Identifiant
	 */
	private int id;

	/**
	 * Libell�
	 */
	private String libelle;

	/**
	 * 
	 * @poseidon-type CANAL
	 */
	private java.util.Collection cANAL = new java.util.TreeSet();

// CONSTRUCTEUR
//*************************************
	
	public Protocole() {
		
	}

// METHODES STATIQUES
//*************************************
	
	
// METHODES DYNAMIQUES
//*************************************
	
	/**
	 * 
	 */
	public void ajouter() {
		logger.debug("D�marrage: ajout");
		// your code here
		logger.debug("Arr�t: ajout");
	}

	/**
	 * 
	 */
	public void modifier() {
		logger.debug("D�marrage: modifier");
		// your code here
		logger.debug("Arr�t: modifier");
	}
}
