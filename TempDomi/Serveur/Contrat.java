package Serveur;


/**
 * <p></p>
 * 
 * {durée_contrat=date_expiration-date_signature}
 */
public abstract class Contrat {

/**
 * <p>Represents ...</p>
 * 
 */
    public java.util.Date date_signature;

/**
 * <p>Represents ...</p>
 * 
 */
    public java.util.Date date_expiration;

/**
 * <p>Represents ...</p>
 * 
 */
    public String signataire;

/**
 * <p>Represents ...</p>
 * 
 */
    public String Mode_Reglement;

/**
 * <p>Represents ...</p>
 * 
 */
    public String type_contrat;

/**
 * <p>Does ...</p>
 * 
 */
    public void durée_contrat() {        
        // your code here
    } 
/**
 * <p></p>
 * 
 */
    public DOCUMENT dOCUMENT;
/**
 * <p></p>
 * 
 */
    public CONTRACTANT cONTRACTANT;
 }
