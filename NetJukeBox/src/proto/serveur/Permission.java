package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.Hashtable;

/**
 * Permission
 */
public class Permission {

// ATTRIBUTS
//**********************************
	
	/**
	 * Identifiant
	 */
	private String id;

	/**
	 * Libell�
	 */
	private String libelle;


// CONSTRUCTEUR
//**********************************
	
	/**
	 * Constructeur
	 * @param String id
	 * @param String libelle
	 */
	public Permission(String id, String libelle) {
		this.id = id;
		this.libelle = libelle;
	}
	
// METHODES DYNAMIQUES
//**********************************
	
	/**
	 * D�truit la permission et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() /*throws SQLException*/ {
		return PermissionFactory.deleteById(id);
	}
	
	/**
	 * Retourne l'ensemble des attributs sous la forme d'un dictionnaire
	 * @return Dictionary
	 */
	public Dictionary getAttributesDictionary() {
		
		Dictionary dico = new Hashtable();
		dico.put("id", id);
		dico.put("libelle", libelle);
		return dico;
	}
	
	//### GETTERS ###
	
	/**
	 * Retourne l'ID
	 * @return String
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Retourne le libell�
	 * @return String
	 */
	public String getLibelle() {
		return libelle;
	}
	
	
	//### SETTERS ###
	
	/**
	 * D�finit le libell�
	 * @param String libelle
	 */
	public boolean setLibelle(String libelle) {
		
		String requete = "UPDATE permission SET libelle = '" + libelle  + "' WHERE id = '" + id + "';";
		
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.libelle = libelle;
		}
		return nbRows>0;
	}
}
