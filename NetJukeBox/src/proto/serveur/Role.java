package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * Rôle d'un utilisateur
 */
public class Role {
	/**
	 * Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(Role.class);

//ATTRIBUTS
//------------------------------------
	
	/**
	 * ID
	 */
	private String id;

	/**
	 * Permissions
	 */
	private Hashtable permissions = new Hashtable();

	
//CONSTRUCTEUR
//------------------------------------
	
	/**
	 * Constructeur
	 * @param String id
	 */
	public Role(String id) {
		this.id = id;
	}
	
	
//METHODES DYNAMIQUES
//------------------------------------
	/**
	 * Détruit le rôle et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() /*throws SQLException*/ {
		
		if (RoleFactory.deleteById(id)) {
		
			//On supprime les associations role/permission
			Enumeration idPerms = permissions.keys();
			while (idPerms.hasMoreElements()) {
				retirerPermission((String)idPerms.nextElement());
			}
			
			return true;
		}
		else return false;
	}
	
	/**
	 * Retourne l'ensemble des attributs sous la forme d'un dictionnaire
	 * @return Dictionary
	 */
	public Dictionary getAttributesDictionary() {
		
		Dictionary dico = new Hashtable();
		dico.put("id", id);
		
		//Liste des permissions
		Vector vPermissions = new Vector();
		Enumeration listePermissions = permissions.elements();
		while (listePermissions.hasMoreElements()) {
			Permission p = (Permission)listePermissions.nextElement();
			Dictionary dicoPermission = new Hashtable();
			dicoPermission.put("id", p.getId());
			dicoPermission.put("libelle", p.getLibelle());
			vPermissions.add(dicoPermission);
		}
		dico.put("permissions", vPermissions);
		
		return dico;
	}
	
	/**
	 * Ajoute une permission au rôle
	 * @param Permission perm
	 */
	public boolean ajouterPermission(Permission perm) {
		logger.debug("Démarrage: ajouterPermission");
		//On ajoute la permission au rôle
		String requete = "INSERT INTO attribuer (login, id_permission) VALUES ('" + id.replace("'", "''") + "', '" + perm.getId()+"');";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien déroulée
		if (nbRows>0) {
			
			//On met à jour le vecteurs d'association
			permissions.put(perm.getId(), perm);
			logger.debug("Arrêt: ajouterPermission");
			return true;
		}
		logger.debug("Arrêt: ajouterPermission");
		return false;
	}

	/**
	 * Retire une permission du rôle
	 * @param String idPerm
	 * @return boolean
	 */
	public boolean retirerPermission(String idPerm) {
		logger.debug("Démarrage: retirerPermission");
		
		// On retire la permission du rôle
		String requete = "DELETE FROM attribuer WHERE login='"+id.replace("'", "''")+"' AND id_permission='"+idPerm+"';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la suppression s'est bien déroulée
		if (nbRows>0) {
			
			//On met à jour le vecteur d'association
			permissions.remove(idPerm);
			logger.debug("Arrêt: retirerPermission");			
			return true;
		}
		//Sinon
		logger.debug("Arrêt: retirerPermission");
		return false;
	}
	
	
	
	//### GETTERS ###
	
	/**
	 * Retourne la liste des permissions
	 * @return Hashtable
	 */
	public /*pure*/ Hashtable getPermissions() {
		return permissions;
	}
	
	/**
	 * Retourne l'ID
	 * @return String
	 */
	public /*pure*/ String getId() {
		return id;
	}
	
	
	//### SETTERS ###
	
	/**
	 * Définit les permssions pour le rôle
	 */
	public void setPermissions() {
		logger.debug("Démarrage: setPermissions");
		
		//On crée un vecteur pour contenir les objets permissions instanciés
		Hashtable perms = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id_permission FROM attribuer WHERE login = '"+id.replace("'", "''")+"';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		logger.info("Role.setPermissions() : "+resultats.size()+" permission(s) trouvée(s)");
			
		// Pour chaque permission, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String idPerm = (String)dico.get("id_permission");
			perms.put(idPerm, PermissionFactory.getById(idPerm));
		}
		
		this.permissions=perms;
		logger.debug("Arrêt: setPermissions");
	}
}