package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

public class CanalFactory {
	/**
	 * Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(CanalFactory.class);

//ATTRIBUTS
//-----------------------------------
	
	/**
	 * Instances organisées par ID
	 */
	private static Hashtable instancesById = new Hashtable();
	
	/**
	 * Instances organisées par Titre
	 */
	private static Hashtable instancesByNom = new Hashtable();
	
	
//METHODES STATIQUES
//-----------------------------------
	
	/**
	 * Renvoie la liste des instances
	 * @return Hashtable
	 */
	public /*pure*/ static Hashtable getInstances() {
		return instancesById;
	}
	
	/**
	 * Vérifie s'il y a une instance pour l'ID demandé
	 * @param String key
	 * @return boolean
	 */
	public /*pure*/ static boolean containsId(String key) {
		return instancesById.containsKey(key);
	}
	
	/**
	 * Vérifie s'il y a une instance pour le nom demandé
	 * @param String key
	 * @return boolean
	 */
	public /*pure*/ static boolean containsNom(String key) {
		return instancesByNom.containsKey(key);
	}
	
	/**
	 * Création du canal en base
	 * @param String nom
	 * @param int utilmax
	 * @return Canal
	 * @throws SQLException 
	 */
	public static Canal create(String nom, int utilmax) /*throws SQLException*/ {
		logger.debug("Démarrage: create");
				
		String requete = "INSERT INTO canal (nom, utilmax) VALUES ('" + nom + "', '" + utilmax + "');";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien déroulée
		if (nbRows>0) {
			//On retourne ensuite un objet pour ce canal
			logger.debug("Arrêt: create");
			return getByNom(nom);
		}
		
		//Sinon, retourne un objet vide
		logger.debug("Arrêt: create");
		return null;
	}
	
	/**
	 * Créé et remplit un objet avec les infos issues de la base, à partir d'un nom
	 * @param String nom
	 * @return Canal
	 * @throws SQLException 
	 */
	public static Canal getByNom(String nom) /*throws SQLException*/ {
		logger.debug("Démarrage: getByNom("+nom+")");
		
		//Si le programme est déjà instancié
		if (instancesByNom.containsKey(nom)) {
			logger.info("Instance trouvée pour Canal "+nom);
			logger.debug("Arrêt: getByNom");
			return (Canal)instancesByNom.get(nom);
		}
		
		//Sinon, on crée l'instance
		else {
		
			//On va chercher les infos depuis la base, en partant d'un nom
			String requete = "SELECT * FROM canal WHERE nom = '" + nom + "';";
			Jdbc base = Jdbc.getInstance();
			Vector resultats = base.executeQuery(requete);
			
			// S'il y a un resultat
			if (resultats != null && resultats.size()>0) {
				
				//On prend le 1er élément
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
				logger.debug("Arrêt: getByNom");
				return c;
			}
			
			//Sinon, on retourne un objet vide
			logger.debug("Arrêt: getByNom");
			return null;
		}
		
	}
	
	/**
	 * Créé et remplit un objet avec les infos issues de la base, à partir d'un id
	 * @param String id
	 * @return Canal
	 * @throws SQLException 
	 */
	public static Canal getById(String id) /*throws SQLException*/ {
		logger.debug("Démarage: getById("+id+")");

		//Si le canal est déjà instancié
		if (instancesById.containsKey(id)) {
			logger.info("Instance trouvée pour Canal "+id);
			logger.debug("Arrêt: getById");
			return (Canal)instancesById.get(id);
		}
		
		//Sinon, on crée l'instance
		else {
		
			//On va chercher les infos depuis la base, en partant d'un id
			String requete = "SELECT * FROM canal WHERE id = '" + id + "';";
			Jdbc base = Jdbc.getInstance();
			Vector resultats = base.executeQuery(requete);
			
			// S'il y a un resultat
			if (resultats != null && resultats.size()>0) {
				
				//On prend le 1er élément
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
				logger.debug("Arrêt: getById");
				return c;
			}
			
			//Sinon, on retourne un objet vide
			logger.debug("Arrêt: getById");
			return null;
		}
	}
	
	/**
	 * Retourne un vecteur d'objets canaux instanciés à partir de toutes les infos de la base 
	 * @return Hashtable
	 * @throws SQLException 
	 */
	public static Hashtable getAll() /*throws SQLException*/ {
		logger.debug("Démarrage: getAll");
		
		//On crée un vecteur pour contenir les objets canaux instanciés
		Hashtable canaux = new Hashtable();
		
		String requete = "SELECT * FROM canal;";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		logger.info("Canal.getAll() : "+resultats.size()+" canal(canaux) trouvé(s)");
		
		// Pour chaque canal, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id"));
			canaux.put(id, getById(id));
		}
		
		//On retourne le vecteur contenant les objets canaux instanciés
		logger.debug("Arrêt: getAll");
		return canaux;
	}
	
	/**
	 * Détruit les infos d'un canal contenues dans la base
	 * @param String id
	 * @return boolean
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) /*throws SQLException*/ {
		logger.debug("Démarrage: deleteById");
		//On supprime le canal de la base, en partant d'un id
		String requete = "DELETE FROM canal WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la ligne est bien supprimée de la base
		if (nbRows>0) {
			
			//On retire l'instance
			Canal c = getById(id);
			instancesByNom.remove(c.getNom());
			instancesById.remove(id);
			logger.debug("Arrêt: deleteById");
			return true;
		}
		
		//Sinon, suppression invalide
		logger.debug("Arrêt: deleteById");
		return false;
	}
	
}
