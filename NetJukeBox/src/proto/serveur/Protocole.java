package proto.serveur;

import org.apache.log4j.Logger;

/**
 * Protocole
 */
public class Protocole {
	/**
	 * Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(Protocole.class);

// ATTRIBUTS
//**********************************
	
	/**
	 * Identifiant
	 */
	private int id;

	/**
	 * Libellé
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
		logger.debug("Démarrage: ajout");
		// your code here
		logger.debug("Arrêt: ajout");
	}

	/**
	 * 
	 */
	public void modifier() {
		logger.debug("Démarrage: modifier");
		// your code here
		logger.debug("Arrêt: modifier");
	}
}
