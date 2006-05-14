package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

public class CanalFactory {

//ATTRIBUTS
//-----------------------------------
	
	/**
	 * Instances organis�es par ID
	 */
	private static Hashtable instancesById = new Hashtable();
	
	/**
	 * Instances organis�es par Titre
	 */
	private static Hashtable instancesByNom = new Hashtable();
	
	
//METHODES STATIQUES
//-----------------------------------
	
	/**
	 * Renvoie la liste des instances
	 * @return Hashtable
	 */
	public static Hashtable getInstances() {
		return instancesById;
	}
	
	/**
	 * V�rifie s'il y a une instance pour l'ID demand�
	 * @param String key
	 * @return boolean
	 */
	public static boolean containsId(String key) {
		return instancesById.containsKey(key);
	}
	
	/**
	 * V�rifie s'il y a une instance pour le nom demand�
	 * @param String key
	 * @return boolean
	 */
	public static boolean containsNom(String key) {
		return instancesByNom.containsKey(key);
	}
	
	/**
	 * Cr�ation du canal en base
	 * @param String nom
	 * @param int utilmax
	 * @return Canal
	 * @throws SQLException 
	 */
	public static Canal create(String nom, int utilmax) /*throws SQLException*/ {
		
		System.out.println("Canal.create()");
		
		String requete = "INSERT INTO canal (nom, utilmax) VALUES ('" + nom + "', '" + utilmax + "');";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien d�roul�e
		if (nbRows>0) {
			//On retourne ensuite un objet pour ce canal
			return getByNom(nom);
		}
		
		//Sinon, retourne un objet vide
		return null;
	}
	
	/**
	 * Cr�� et remplit un objet avec les infos issues de la base, � partir d'un nom
	 * @param String nom
	 * @return Canal
	 * @throws SQLException 
	 */
	public static Canal getByNom(String nom) /*throws SQLException*/ {
		
		System.out.println("Canal.getByNom("+nom+")");
		
		//Si le programme est d�j� instanci�
		if (instancesByNom.containsKey(nom)) {
			System.out.println("Instance trouv�e pour Canal "+nom);
			return (Canal)instancesByNom.get(nom);
		}
		
		//Sinon, on cr�e l'instance
		else {
		
			//On va chercher les infos depuis la base, en partant d'un nom
			String requete = "SELECT * FROM canal WHERE nom = '" + nom + "';";
			Jdbc base = Jdbc.getInstance();
			Vector resultats = base.executeQuery(requete);
			
			// S'il y a un resultat
			if (resultats != null && resultats.size()>0) {
				
				//On prend le 1er �l�ment
				Dictionary dico = (Dictionary) resultats.firstElement();
				
				//On mappe les champs
				String id = String.valueOf((Integer)dico.get("id"));
				int utilMax = (int)(Integer)dico.get("utilmax");
				
				System.out.println("-------- Canal -----------");
				System.out.println("Nb de champs: "+dico.size());
				System.out.println("ID: "+id);
				System.out.println("Nom: "+nom);
				System.out.println("Nb max d'auditeurs: "+utilMax);
				System.out.println("-----------------------------");
				
				//On retourne l'objet
				Canal c = new Canal(id, nom, utilMax);
				instancesById.put(c.getId(), c);
				instancesByNom.put(c.getNom(), c);
				c.setProgrammesPlanifies();
				return c;
			}
			
			//Sinon, on retourne un objet vide
			return null;
		}
		
	}
	
	/**
	 * Cr�� et remplit un objet avec les infos issues de la base, � partir d'un id
	 * @param String id
	 * @return Canal
	 * @throws SQLException 
	 */
	public static Canal getById(String id) /*throws SQLException*/ {
		
		System.out.println("Canal.getById("+id+")");
		
		//Si le canal est d�j� instanci�
		if (instancesById.containsKey(id)) {
			System.out.println("Instance trouv�e pour Canal "+id);
			return (Canal)instancesById.get(id);
		}
		
		//Sinon, on cr�e l'instance
		else {
		
			//On va chercher les infos depuis la base, en partant d'un id
			String requete = "SELECT * FROM canal WHERE id = '" + id + "';";
			Jdbc base = Jdbc.getInstance();
			Vector resultats = base.executeQuery(requete);
			
			// S'il y a un resultat
			if (resultats != null && resultats.size()>0) {
				
				//On prend le 1er �l�ment
				Dictionary dico = (Dictionary) resultats.firstElement();
				
				//On mappe les champs
				String nom = (String)dico.get("nom");
				int utilMax = (int)(Integer)dico.get("utilmax");
				
				System.out.println("-------- Canal -----------");
				System.out.println("Nb de champs: "+dico.size());
				System.out.println("ID: "+id);
				System.out.println("Nom: "+nom);
				System.out.println("Nb max d'auditeurs: "+utilMax);
				System.out.println("-----------------------------");
				
				//On retourne l'objet
				Canal c = new Canal(id, nom, utilMax);
				instancesById.put(c.getId(), c);
				instancesByNom.put(c.getNom(), c);
				c.setProgrammesPlanifies();
				return c;
			}
			
			//Sinon, on retourne un objet vide
			return null;
		}
	}
	
	/**
	 * Retourne un vecteur d'objets canaux instanci�s � partir de toutes les infos de la base 
	 * @return Hashtable
	 * @throws SQLException 
	 */
	public static Hashtable getAll() /*throws SQLException*/ {
		
		System.out.println("Canal.getAll()");
		
		//On cr�e un vecteur pour contenir les objets canaux instanci�s
		Hashtable canaux = new Hashtable();
		
		String requete = "SELECT * FROM canal;";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		System.out.println("Canal.getAll() : "+resultats.size()+" canal(canaux) trouv�(s)");
		
		// Pour chaque canal, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id"));
			canaux.put(id, getById(id));
		}
		
		//On retourne le vecteur contenant les objets canaux instanci�s
		return canaux;
	}
	
	/**
	 * D�truit les infos d'un canal contenues dans la base
	 * @param String id
	 * @return boolean
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) /*throws SQLException*/ {
		
		//On supprime le canal de la base, en partant d'un id
		String requete = "DELETE FROM canal WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la ligne est bien supprim�e de la base
		if (nbRows>0) {
			
			//On retire l'instance
			Canal c = getById(id);
			instancesByNom.remove(c.getNom());
			instancesById.remove(id);
			return true;
		}
		
		//Sinon, suppression invalide
		return false;
	}
	
}
