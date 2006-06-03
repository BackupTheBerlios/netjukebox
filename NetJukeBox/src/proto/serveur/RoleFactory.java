package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * Role Factory
 */
public class RoleFactory {
	/**
	 * Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(RoleFactory.class);
	
//ATTRIBUTS
//-----------------------------------------
	
	/**
	 * Instances
	 */
	private static Hashtable instances = new Hashtable();
	
	
//METHODES STATIQUES
//------------------------------------------
	
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
	 * Création de la role en base
	 * @param String id
	 * @return Permission
	 */
	public static Role create(String id) {
		logger.debug("Démarrage: create");
		
		//On crée dans LDAP un nouveau role
		Ldap ldap = Ldap.getInstance();
		ldap.CreationGroupe(id);		
		
		//On retourne ensuite un objet pour ce role
		logger.debug("Arrêt: create");
		return getById(id);
		
		//Sinon on retourne un objet vide
		//logger.debug("Arrêt: create");
		//return null;
	}
	
	/**
	 * Instancie un objet Role après avoir récupéré ces infos depuis la base à partir de son id
	 * @param String id
	 * @return Permission
	 */
	public static Role getById(String id) /*throws SQLException*/ {
		logger.debug("Démarrage: getById("+id+")");
		
		//Si le rôle est déjà instancié
		if (instances.containsKey(id)) {
			logger.info("Instance trouvée pour Role "+id);
			logger.debug("Arrêt: getById");
			return (Role)instances.get(id);
		}
		
		//Sinon, on crée l'instance
		else {
			logger.info("Nouvelle instance pour Role "+id);
			
			Ldap ldap = Ldap.getInstance();
			ldap.CreationGroupe(id);
				
				System.out.println("----------- Role ------------");
				System.out.println("ID: "+id);
				System.out.println("-----------------------------");
				
				//On retourne l'objet
				Role role = new Role(id);
				instances.put(id, role);
				role.setPermissions();
				logger.debug("Arrêt: getById");
				return role;
			
			//Sinon, on retourne un objet vide
			//logger.debug("Arrêt: getById");
			//return null;
		}
	}
	/**
	 * Retourne un vecteur d'objets permission instanciés à partir de toutes les infos de la base
	 * @return Hashtable
	 * @throws SQLException 
	 */

	public static Hashtable getAll() /*throws SQLException*/ {
		logger.debug("Démarrage: getAll");
		
		//On crée un vecteur pour contenir les objets roles instanciés
		Hashtable roles = new Hashtable();
		
		//On va chercher dans la liste des id de tous les rôles
		Ldap ldap = Ldap.getInstance();
		ldap.ListeGroupe();	
		Vector vect= ldap.ListeGroupe();
     	 
		 for(int i=0; i < vect.size(); i++)
        	if(vect.elementAt(i) != null){
	            String r=(String) vect.elementAt(i);
	           	//Pour chaque role, on instancie un objet que l'on stocke dans le vecteur
	         	roles.put(r.substring(3), getById(r.substring(3)));
	         	
        	} else logger.info("Il n'y a pas de rôle dans l'annuaire LDAP!");
         
		
		//	roles.put("usager", getById("usager"));
		//	roles.put("respprog", getById("respprog"));
		//	roles.put("admin", getById("admin"));
		
		//On retourne le vecteur contenant les objets permissions instanciés
		logger.debug("Arrêt: getAll");
		return roles;
	}
	
	/**
	 * Détruit les infos d'un rôle contenues dans la base
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) /*throws SQLException*/ {
		logger.debug("Démarrage: deleteById");
		//On supprime le rôle de LDAP
		Ldap ldap = Ldap.getInstance();
		return ldap.SupprimerGroupe(id);
		
		/*
		//On supprime les droits de la base, en partant d'un id
		String requete = "DELETE FROM attribuer WHERE login = '" + id + "';";
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
		return false;*/
	}
}