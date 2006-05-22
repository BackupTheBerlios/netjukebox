package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Permission Factory
 */
public class PermissionFactory {
	
//ATTRIBUTS
//-----------------------------------
	
	/**
	 * Instances
	 */
	private static Hashtable instances = new Hashtable();
	
	
//METHODES STATIQUES
//-----------------------------------

	/**
	 * V�rifie s'il y a une instance pour l'ID demand�
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
	 * Cr�ation de la permission en base
	 * @param String id
	 * @param String libelle
	 * @return Permission
	 */
	public static Permission create(String id, String libelle) {
		
		System.out.println("PermissionFactory.create()");
		
		//On cr�e le contractant dans la base
		String requete = "INSERT INTO permission (id, libelle) VALUES ('" + id + "', '"+ libelle + "');"; 
		
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien d�roul�e
		if (nbRows>0) {
			//On retourne ensuite un objet pour ce contractant
			return getById(id);
		}
		
		//Sinon on retourne un objet vide
		return null;
	}
	
	/**
	 * Instancie un objet Permission apr�s avoir r�cup�r� ces infos depuis la base � partir de son id
	 * @param String id
	 * @return Permission
	 */
	public static Permission getById(String id) /*throws SQLException*/ {
		
		System.out.println("PermissionFactory.getById("+id+")");
		
		//Si le contractant est d�j� instanci�
		if (instances.containsKey(id)) {
			System.out.println("Instance trouv�e pour Permission "+id);
			return (Permission)instances.get(id);
		}
		
		//Sinon, on cr�e l'instance
		else {
			System.out.println("Nouvelle instance pour Permission "+id);
			String requete = "SELECT * FROM permission WHERE id = '" + id + "';";
	
			Jdbc base = Jdbc.getInstance();
			Vector resultats = base.executeQuery(requete);
			
			//S'il y a un resultat
			if (resultats!=null && resultats.size()>0) {
				
				//On prend le 1er �l�ment
				Dictionary dico = (Dictionary) resultats.firstElement();
				
				//On mappe les champs
				String libelle = (String)dico.get("libelle");
				
				System.out.println("-------- Permission ---------");
				System.out.println("ID: "+id);
				System.out.println("Libell�: "+libelle);
				System.out.println("-----------------------------");
				
				//On retourne l'objet
				Permission permission = new Permission(id, libelle);
				instances.put(id, permission);
				return permission;
			}
			
			//Sinon, on retourne un objet vide
			return null;
		}
	}
	/**
	 * Retourne un vecteur d'objets permission instanci�s � partir de toutes les infos de la base
	 * @return Hashtable
	 * @throws SQLException 
	 */

	public static Hashtable getAll() /*throws SQLException*/ {
		
		System.out.println("PermissionFactory.getAll()");
		
		//On cr�e un vecteur pour contenir les objets documents instanci�s
		Hashtable permissions = new Hashtable();
		
		//On va chercher dans la liste des id de tous les permissions
		String requete = "SELECT id FROM permission;";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		System.out.println("Permission.getAll() : "+resultats.size()+" permission(s) trouv�(s)");
		
		//Pour chaque document, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = (String)dico.get("id");
			permissions.put(id, PermissionFactory.getById(id));
		}
		
		//On retourne le vecteur contenant les objets permissions instanci�s
		return permissions;
	}
	
	/**
	 * D�truit les infos d'un permission contenues dans la base
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) /*throws SQLException*/ {
		
		//On supprime le contractant de la base, en partant d'un id
		String requete = "DELETE FROM permission WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la ligne est bien supprim�e de la base
		if (nbRows>0) {
			
			//On retire l'instance
			instances.remove(id);
			return true;
		}
		
		//Sinon, suppression invalide
		return false;
	}
}
