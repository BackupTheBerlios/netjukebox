package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Role Factory
 */
public class RoleFactory {
	
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
		
		System.out.println("RoleFactory.create()");
		
		//------------
		// LDAP
		//------------
		
		//On retourne ensuite un objet pour ce contractant
		return getById(id);
		
		//Sinon on retourne un objet vide
		//return null;
	}
	
	/**
	 * Instancie un objet Role après avoir récupéré ces infos depuis la base à partir de son id
	 * @param String id
	 * @return Permission
	 */
	public static Role getById(String id) /*throws SQLException*/ {
		
		System.out.println("RoleFactory.getById("+id+")");
		
		//Si le contractant est déjà instancié
		if (instances.containsKey(id)) {
			System.out.println("Instance trouvée pour Role "+id);
			return (Role)instances.get(id);
		}
		
		//Sinon, on crée l'instance
		else {
			System.out.println("Nouvelle instance pour Role "+id);
			
			//------------
			// LDAP
			//------------
				
				System.out.println("----------- Role ------------");
				System.out.println("ID: "+id);
				System.out.println("-----------------------------");
				
				//On retourne l'objet
				Role role = new Role(id);
				instances.put(id, role);
				role.setPermissions();
				return role;
			
			//Sinon, on retourne un objet vide
			//return null;
		}
	}
	/**
	 * Retourne un vecteur d'objets permission instanciés à partir de toutes les infos de la base
	 * @return Hashtable
	 * @throws SQLException 
	 */

	public static Hashtable getAll() /*throws SQLException*/ {
		
		System.out.println("RoleFactory.getAll()");
		
		//On crée un vecteur pour contenir les objets roles instanciés
		Hashtable roles = new Hashtable();
		
		//On va chercher dans la liste des id de tous les rôles
		
		//---------------
		// LDAP
		//---------------
		
		//Pour chaque document, on instancie un objet que l'on stocke dans le vecteur
		
			roles.put("usager", getById("usager"));
			roles.put("respprog", getById("respprog"));
			roles.put("admin", getById("admin"));
		
		//On retourne le vecteur contenant les objets permissions instanciés
		return roles;
	}
	
	/**
	 * Détruit les infos d'un rôle contenues dans la base
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) /*throws SQLException*/ {
		
		//On supprime le rôle de LDAP
		//--------------
		// LDAP
		//--------------
		
		//On supprime les droits de la base, en partant d'un id
		String requete = "DELETE FROM attribuer WHERE login = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la ligne est bien supprimée de la base
		if (nbRows>0) {			
			
			//On retire l'instance
			instances.remove(id);
			return true;
		}
		
		//Sinon, suppression invalide
		return false;
	}
}