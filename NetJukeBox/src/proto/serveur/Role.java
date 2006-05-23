package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

/**
 * R�le d'un utilisateur
 */
public class Role {

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
	 * D�truit le r�le et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() /*throws SQLException*/ {
		return RoleFactory.deleteById(id);
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
	 * Ajoute une permission au r�le
	 * @param Permission perm
	 */
	public void ajouterPermission(Permission perm) {
		
		//On ajoute la permission au r�le
		String requete = "INSERT INTO attribuer (login, id_permission) VALUES ('" + id + "', '" + perm.getId()+"');";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien d�roul�e
		if (nbRows>0) {
			
			//On met � jour le vecteurs d'association
			permissions.put(perm.getId(), perm);
		}
	}

	/**
	 * Retire une permission du r�le
	 * @param String idPerm
	 * @return boolean
	 */
	public boolean retirerPermission(String idPerm) {
		//logger.debug("D�marrage: retirerPermission");
		
		// On retire la permission du r�le
		String requete = "DELETE FROM attribuer WHERE login='"+id+"' AND id_permission='"+idPerm+"';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la suppression s'est bien d�roul�e
		if (nbRows>0) {
			
			//On met � jour le vecteur d'association
			permissions.remove(idPerm);
			
			//logger.debug("Arr�t: retirerPermission");			
			return true;
		}
		
		//Sinon
		//logger.debug("Arr�t: retirerPermission");
		return false;
	}
	
	
	
	//### GETTERS ###
	
	/**
	 * Retourne la liste des permissions
	 * @return Hashtable
	 */
	public Hashtable getPermissions() {
		return permissions;
	}
	
	/**
	 * Retourne l'ID
	 * @return String
	 */
	public String getId() {
		return id;
	}
	
	
	//### SETTERS ###
	
	/**
	 * D�finit les permssions pour le r�le
	 */
	public void setPermissions() {
		//logger.info("Role.setPermissions()");
		
		//On cr�e un vecteur pour contenir les objets permissions instanci�s
		Hashtable perms = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id_permission FROM attribuer WHERE login = '"+id+"';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		System.out.println("Role.setPermissions() : "+resultats.size()+" permission(s) trouv�e(s)");
		//logger.info("Role.setPermissions() : "+resultats.size()+" permission(s) trouv�e(s)");
		
		// Pour chaque permission, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String idPerm = (String)dico.get("id_permission");
			perms.put(idPerm, PermissionFactory.getById(idPerm));
		}
		
		this.permissions=perms;
	}
}