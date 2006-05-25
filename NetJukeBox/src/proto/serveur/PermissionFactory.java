package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * Permission Factory
 */
public class PermissionFactory {
	/**
	 * Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(PermissionFactory.class);
	
//ATTRIBUTS
//-----------------------------------
	
	/**
	 * Instances
	 */
	private static Hashtable instances = new Hashtable();
	
	
//METHODES STATIQUES
//-----------------------------------

	/**
	 * Vérifie s'il y a une instance pour l'ID demandé
	 * @param String key
	 * @return boolean
	 */
	public static boolean containsId(String key) {
		return instances.containsKey(key);
	}
	
	/**
	 * Renvoie la liste des instances
	 * @return Hashtable
	 */
	public static Hashtable getInstances() {
		return instances;
	}

	/**
	 * Création de la permission en base
	 * @param String id
	 * @param String libelle
	 * @return Permission
	 */
	public static Permission create(String id, String libelle) {
		logger.debug("Démarrage: create");
		
		//On crée le contractant dans la base
		String requete = "INSERT INTO permission (id, libelle) VALUES ('" + id + "', '"+ libelle + "');"; 
		
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien déroulée
		if (nbRows>0) {
			//On retourne ensuite un objet pour ce contractant
			logger.debug("Arrêt: create");
			return getById(id);
		}
		
		//Sinon on retourne un objet vide
		logger.debug("Arrêt: create");
		return null;
	}
	
	/**
	 * Instancie un objet Permission après avoir récupéré ces infos depuis la base à partir de son id
	 * @param String id
	 * @return Permission
	 */
	public static Permission getById(String id) /*throws SQLException*/ {
		logger.debug("Démarrage: getById("+id+")");
		
		//Si le contractant est déjà instancié
		if (instances.containsKey(id)) {
			logger.info("Instance trouvée pour Permission "+id);
			logger.debug("Arrêt: getById");
			return (Permission)instances.get(id);
		}
		
		//Sinon, on crée l'instance
		else {
			System.out.println("Nouvelle instance pour Permission "+id);
			String requete = "SELECT * FROM permission WHERE id = '" + id + "';";
	
			Jdbc base = Jdbc.getInstance();
			Vector resultats = base.executeQuery(requete);
			
			//S'il y a un resultat
			if (resultats!=null && resultats.size()>0) {
				
				//On prend le 1er élément
				Dictionary dico = (Dictionary) resultats.firstElement();
				
				//On mappe les champs
				String libelle = (String)dico.get("libelle");
				
				System.out.println("-------- Permission ---------");
				System.out.println("ID: "+id);
				System.out.println("Libellé: "+libelle);
				System.out.println("-----------------------------");
				
				//On retourne l'objet
				Permission permission = new Permission(id, libelle);
				instances.put(id, permission);
				logger.debug("Arrêt: getById");
				return permission;
			}
			
			//Sinon, on retourne un objet vide
			logger.debug("Arrêt: getById");
			return null;
		}
	}
	/**
	 * Retourne un vecteur d'objets permission instanciés à partir de toutes les infos de la base
	 * @return Hashtable
	 * @throws SQLException 
	 */

	public static Hashtable getAll() /*throws SQLException*/ {
		logger.debug("Démarrage: getAll");
		
		//On crée un vecteur pour contenir les objets documents instanciés
		Hashtable permissions = new Hashtable();
		
		//On va chercher dans la liste des id de tous les permissions
		String requete = "SELECT id FROM permission;";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		logger.info("Permission.getAll() : "+resultats.size()+" permission(s) trouvé(s)");
		
		//Pour chaque document, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = (String)dico.get("id");
			permissions.put(id, PermissionFactory.getById(id));
		}
		
		//On retourne le vecteur contenant les objets permissions instanciés
		logger.debug("Arrêt: getAll");
		return permissions;
	}
	
	/**
	 * Détruit les infos d'un permission contenues dans la base
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) /*throws SQLException*/ {
		logger.debug("Démarrage: deleteById");
		//On supprime le contractant de la base, en partant d'un id
		String requete = "DELETE FROM permission WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la ligne est bien supprimée de la base
		if (nbRows>0) {
			
			//On retire l'instance
			instances.remove(id);
			logger.debug("Arrêt: deleteById");
			return true;
		}
		
		//Sinon, suppression invalide
		logger.debug("Arrêt: deleteById");
		return false;
	}
}
