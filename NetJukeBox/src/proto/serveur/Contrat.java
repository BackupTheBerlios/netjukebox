package proto.serveur;

import java.util.Date;

/**
 * Contrat {dur�e_contrat=date_expiration-date_signature}
 */
public abstract class Contrat {

	/**
	 * Date de signature
	 */
	private Date dateSignature;

	/**
	 * Date d'expiration
	 */
	private Date dateExpiration;

	/**
	 * Signataire
	 */
	private String signataire;

	/**
	 * Mode de r�glement
	 */
	private String modeReglement;

	/**
	 * Type de contrat
	 */
	private String type;

	/**
	 * Document
	 */
	private Document document;

	/**
	 * Contractant
	 */
	private Contractant contractant;

// CONSTRUCTEUR
//***************************************
	
	public Contrat() {
		
	}
	
// METHODES STATIQUES
//***************************************
	
// METHODES DYNAMIQUES
//***************************************
	
	/**
	 * Calcule la dur�e du contrat
	 */
	public void dureeContrat() {
		// your code here
	}

}