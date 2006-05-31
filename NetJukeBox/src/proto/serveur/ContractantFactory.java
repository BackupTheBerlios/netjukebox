package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

public class ContractantFactory {
	/**
	 * Cr�e le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(ContractantFactory.class);
//ATTRIBUTS
//-----------------------------------------

	/**
	 * Instances organis�es par ID
	 */
	private static Hashtable instancesById = new Hashtable();
	
	/**
	 * Instances organis�es par Nom
	 */
	private static Hashtable instancesByNom = new Hashtable();
	
//METHODES STATIQUES
//-----------------------------------------	
	
	/**
	 * V�rifie s'il y a une instance pour l'ID demand�
	 * @param String key
	 * @return boolean
	 */
	public /*pure*/ static boolean containsId(String key) {
		return instancesById.containsKey(key);
	}
	
	/**
	 * V�rifie s'il y a une instance pour le nom demand�
	 * @param String key
	 * @return boolean
	 */
	public /*pure*/ static boolean containsNom(String key) {
		return instancesByNom.containsKey(key);
	}
	
	/**
	 * Renvoie la liste des instances
	 * @return Hashtable
	 */
	public /*pure*/ static Hashtable getInstances() {
		return instancesById;
	}
	
	/**
	 * Cr�ation du contractant en base
	 * @param String nom
	 * @param String adresse
	 * @param String codePostal
	 * @param String ville
	 * @param String telephone
	 * @param String fax
	 * @param String mail
	 * @param String type
	 * @return Contractant
	 */
	public static Contractant create(String nom, String adresse, String codePostal,
			String ville, String telephone, String fax, String mail, String type) /*throws SQLException*/ {
		
		logger.debug("D�marrage: create");
		
		//On cr�e le contractant dans la base
		String requete = "INSERT INTO contractant (nom, adresse, cp, ville, telephone, fax, mail, type) VALUES ('" +
			nom.replace("'", "''") + "', '" + adresse.replace("'", "''") + "', '" + codePostal.replace("'", "''") + "', '" +
			ville.replace("'", "''") + "', '" +telephone.replace("'", "''") + "', '" + fax.replace("'", "''") + "', '" + mail.replace("'", "''") + "', '"+ type + "');"; 
		
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien d�roul�e
		if (nbRows>0) {
			//On retourne ensuite un objet pour ce contractant
			logger.debug("Arr�t: create");
			return getByNom(nom);
		}
		
		//Sinon on retourne un objet vide
		logger.debug("Arr�t: create");
		return null;
	}
	
	/**
	 * Instancie un objet contractant apr�s avoir r�cup�r� ces infos depuis la base � partir de son id
	 * @param String id
	 * @return Contractant
	 */
	public static Contractant getById(String id) /*throws SQLException*/ {
		logger.debug("D�marrage: getById("+id+")");
		
		//Si le contractant est d�j� instanci�
		if (instancesById.containsKey(id)) {
			logger.info("Instance trouv�e pour Contractant "+id);
			logger.debug("Arr�t: getById");
			return (Contractant)instancesById.get(id);
		}
		
		//Sinon, on cr�e l'instance
		else {
			logger.info("Nouvelle instance pour Contractant "+id);
			String requete = "SELECT * FROM contractant WHERE id = '" + id + "';";
	
			Jdbc base = Jdbc.getInstance();
			Vector resultats = base.executeQuery(requete);
			
			//S'il y a un resultat
			if (resultats!=null && resultats.size()>0) {
				
				//On prend le 1er �l�ment
				Dictionary dico = (Dictionary) resultats.firstElement();
				
				//On mappe les champs
				String nom = ((String)dico.get("nom")).replace("''", "'");
				String adresse = ((String)dico.get("adresse")).replace("''", "'");
				String codePostal = ((String)dico.get("cp")).replace("''", "'");
				String ville = ((String)dico.get("ville")).replace("''", "'");
				String telephone = ((String)dico.get("telephone")).replace("''", "'");
				String fax = ((String)dico.get("fax")).replace("''", "'");
				String mail = ((String)dico.get("mail")).replace("''", "'");
				String type = ((String)dico.get("type")).replace("''", "'");
				
				System.out.println("-------- Contractant -----------");
				System.out.println("ID: "+id);
				System.out.println("Nom: "+nom);
				System.out.println("Adresse: "+adresse);
				System.out.println("CP: "+codePostal);
				System.out.println("Ville: "+ville);
				System.out.println("T�l�phone: "+telephone);
				System.out.println("Fax: "+fax);
				System.out.println("eMail: "+mail);
				System.out.println("Type: "+type);
				System.out.println("-----------------------------");
				
				//On retourne l'objet
				Contractant contractant = new Contractant(id, nom, adresse, codePostal, ville, telephone, fax, mail, type);
				instancesById.put(id, contractant);
				instancesByNom.put(nom, contractant);
				contractant.setContratsAssocies();
				logger.debug("Arr�t: getById");
				return contractant;
			}
			
			//Sinon, on retourne un objet vide
			logger.debug("Arr�t: getById");
			return null;
		}
	}
	
	/**
	 * Instancie un objet contractant apr�s avoir r�cup�r� ces infos depuis la base � partir de son nom
	 * @param String nom
	 * @return Contractant
	 */
	public static Contractant getByNom(String nom) /*throws SQLException*/ {
		logger.debug("D�marrage: getByNom("+nom+")");
		
		//Si le contractant est d�j� instanci�
		if (instancesById.containsKey(nom)) {
			logger.info("Instance trouv�e pour Contractant "+nom);
			logger.debug("Arr�t: getByNom");
			return (Contractant)instancesById.get(nom);
		}
		
		//Sinon, on cr�e l'instance
		else {
			String nomSQL = nom.replace("'", "''");
			String requete = "SELECT * FROM contractant WHERE nom = '" + nom + "';";
	
			Jdbc base = Jdbc.getInstance();
			Vector resultats = base.executeQuery(requete);
			
			//S'il y a un resultat
			if (resultats!=null && resultats.size()>0) {
				
				//On prend le 1er �l�ment
				Dictionary dico = (Dictionary) resultats.firstElement();
				
				//On mappe les champs
				String id = String.valueOf((Integer)dico.get("id"));
				String adresse = ((String)dico.get("adresse")).replace("''", "'");
				String codePostal = ((String)dico.get("cp")).replace("''", "'");
				String ville = ((String)dico.get("ville")).replace("''", "'");
				String telephone = ((String)dico.get("telephone")).replace("''", "'");
				String fax = ((String)dico.get("fax")).replace("''", "'");
				String mail = ((String)dico.get("mail")).replace("''", "'");
				String type = ((String)dico.get("type")).replace("''", "'");

				System.out.println("-------- Contractant -----------");
				System.out.println("ID: "+id);
				System.out.println("Nom: "+nom);
				System.out.println("Adresse: "+adresse);
				System.out.println("CP: "+codePostal);
				System.out.println("Ville: "+ville);
				System.out.println("T�l�phone: "+telephone);
				System.out.println("Fax: "+fax);
				System.out.println("eMail: "+mail);
				System.out.println("Type: "+type);
				System.out.println("-----------------------------");
				
				//On retourne l'objet
				Contractant contractant = new Contractant(id, nom, adresse, codePostal, ville, telephone, fax, mail, type);
				instancesByNom.put(nom, contractant);
				instancesById.put(id, contractant);
				contractant.setContratsAssocies();
				logger.debug("Arr�t: getByNom");
				return contractant;
			}
			
			//Sinon, on retourne un objet vide
			logger.debug("Arr�t: getByNom");
			return null;
		}
	}
	
	/**
	 * Retourne un vecteur d'objets contractants instanci�s � partir de toutes les infos de la base
	 * @return Hashtable
	 * @throws SQLException 
	 */

	public static Hashtable getAll() /*throws SQLException*/ {
		
		logger.debug("D�marrage: getAll");
		
		//On cr�e un vecteur pour contenir les objets documents instanci�s
		Hashtable contractants = new Hashtable();
		
		//On va chercher dans la liste des id de tous les documents
		String requete = "SELECT id FROM contractant;";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		logger.info("Contractant.getAll() : "+resultats.size()+" contractant(s) trouv�(s)");
		
		//Pour chaque document, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id"));
			contractants.put(id, ContractantFactory.getById(id));
		}
		
		//On retourne le vecteur contenant les objets contractants instanci�s
		logger.debug("Arr�t: getAll");
		return contractants;
	}
	
	/**
	 * D�truit les infos d'un contractant contenues dans la base
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) /*throws SQLException*/ {
		logger.debug("D�marrage: deleteById");
		//On supprime le contractant de la base, en partant d'un id
		String requete = "DELETE FROM contractant WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la ligne est bien supprim�e de la base
		if (nbRows>0) {
			
			//On retire l'instance
			Contractant c = getById(id);
			instancesByNom.remove(c.getNom());
			instancesById.remove(id);
			logger.debug("Arr�t: deleteById");
			return true;
		}
		
		//Sinon, suppression invalide
		logger.debug("Arr�t: deleteById");
		return false;
	}
}