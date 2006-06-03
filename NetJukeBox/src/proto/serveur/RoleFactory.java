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
	 * Cr�e le logger de la classe
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
	 * Cr�ation de la role en base
	 * @param String id
	 * @return Permission
	 */
	public static Role create(String id) {
		logger.debug("D�marrage: create");
		
		//On cr�e dans LDAP un nouveau role
		Ldap ldap = Ldap.getInstance();
		ldap.CreationGroupe(id);		
		
		//On retourne ensuite un objet pour ce role
		logger.debug("Arr�t: create");
		return getById(id);
		
		//Sinon on retourne un objet vide
		//logger.debug("Arr�t: create");
		//return null;
	}
	
	/**
	 * Instancie un objet Role apr�s avoir r�cup�r� ces infos depuis la base � partir de son id
	 * @param String id
	 * @return Permission
	 */
	public static Role getById(String id) /*throws SQLException*/ {
		logger.debug("D�marrage: getById("+id+")");
		
		//Si le r�le est d�j� instanci�
		if (instances.containsKey(id)) {
			logger.info("Instance trouv�e pour Role "+id);
			logger.debug("Arr�t: getById");
			return (Role)instances.get(id);
		}
		
		//Sinon, on cr�e l'instance
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
				logger.debug("Arr�t: getById");
				return role;
			
			//Sinon, on retourne un objet vide
			//logger.debug("Arr�t: getById");
			//return null;
		}
	}
	/**
	 * Retourne un vecteur d'objets permission instanci�s � partir de toutes les infos de la base
	 * @return Hashtable
	 * @throws SQLException 
	 */

	public static Hashtable getAll() /*throws SQLException*/ {
		logger.debug("D�marrage: getAll");
		
		//On cr�e un vecteur pour contenir les objets roles instanci�s
		Hashtable roles = new Hashtable();
		
		//On va chercher dans la liste des id de tous les r�les
		Ldap ldap = Ldap.getInstance();
		ldap.ListeGroupe();	
		Vector vect= ldap.ListeGroupe();
     	 
		 for(int i=0; i < vect.size(); i++)
        	if(vect.elementAt(i) != null){
	            String r=(String) vect.elementAt(i);
	           	//Pour chaque role, on instancie un objet que l'on stocke dans le vecteur
	         	roles.put(r.substring(3), getById(r.substring(3)));
	         	
        	} else logger.info("Il n'y a pas de r�le dans l'annuaire LDAP!");
         
		
		//	roles.put("usager", getById("usager"));
		//	roles.put("respprog", getById("respprog"));
		//	roles.put("admin", getById("admin"));
		
		//On retourne le vecteur contenant les objets permissions instanci�s
		logger.debug("Arr�t: getAll");
		return roles;
	}
	
	/**
	 * D�truit les infos d'un r�le contenues dans la base
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) /*throws SQLException*/ {
		logger.debug("D�marrage: deleteById");
		//On supprime le r�le de LDAP
		Ldap ldap = Ldap.getInstance();
		return ldap.SupprimerGroupe(id);
		
		/*
		//On supprime les droits de la base, en partant d'un id
		String requete = "DELETE FROM attribuer WHERE login = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la ligne est bien supprim�e de la base
		if (nbRows>0) {			
			
			//On retire l'instance
			instances.remove(id);
			logger.debug("Arr�t: deleteById");
			return true;
		}
		
		//Sinon, suppression invalide
		logger.debug("Arr�t: deleteById");
		return false;*/
	}
}