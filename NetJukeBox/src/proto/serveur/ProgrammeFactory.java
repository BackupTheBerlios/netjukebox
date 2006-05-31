package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

public class ProgrammeFactory {

//ATTRIBUTS
//--------------------------------
	/**
	 * Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(Programme.class);
	
	/**
	 * Instances organisées par ID
	 */
	private static Hashtable instancesById = new Hashtable();
	
	/**
	 * Instances organisées par Titre
	 */
	private static Hashtable instancesByTitre = new Hashtable();
	
	
//METHODES STATIQUES
//--------------------------------
	
	/**
	 * Vérifie s'il y a une instance pour l'ID demandé
	 * @param String key
	 * @return boolean
	 */
	public static boolean containsId(String key) {
		return instancesById.containsKey(key);
	}
	
	/**
	 * Vérifie s'il y a une instance pour le titre demandé
	 * @param String key
	 * @return boolean
	 */
	public static boolean containsTitre(String key) {
		return instancesByTitre.containsKey(key);
	}
	
	/**
	 * Renvoie la liste des instances
	 * @return Hashtable
	 */
	public static Hashtable getInstances() {
		return instancesById;
	}
	
	/**
	 * Création du programme en base
	 * @param String titre
	 * @param String thematique
	 * @return Programme
	 * @throws SQLException 
	 */
	public static Programme create(String titre, String thematique) /*throws SQLException*/ {
		//PropertyConfigurator.configure("src/proto/serveur/log4j.prop");
		//PropertyConfigurator.configure("C:/Documents and Settings/Marie Rubini/Mes documents/workspace/NetJukeBox/proto/serveur/log4j.prop");
		
		logger.debug("Démarrage: Programme.create");
		
		String requete = "INSERT INTO programme (titre, thematique, duree) VALUES ('" + titre.replace("'", "''") + "', '" + thematique.replace("'", "''") + "', '0');";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien déroulée
		if (nbRows>0) {
			//On retourne ensuite un objet pour ce programme
			logger.debug("Arrêt: Programme.create");
			return getByTitre(titre);
		}
		
		//Sinon, retourne un objet vide
		logger.debug("Arrêt: Programme.create");
		return null;
	}
	
	/**
	 * Insancie un objet programme après avoir récupéré ces infos depuis la base à partir de son titre
	 * @param String titre
	 * @return Programme
	 * @throws SQLException 
	 */
	public static Programme getByTitre(String titre) /*throws SQLException*/ {
		logger.debug("Démarrage: Programme.getByTitre");
		logger.info ("Programme.getByTitre("+titre+")");
		
		//Si le programme est déjà instancié
		if (instancesByTitre.containsKey(titre)) {
			System.out.println("Instance trouvée pour Programme "+titre);
			logger.debug("Arrêt: Programme.getByTitre");
			return (Programme)instancesByTitre.get(titre);
		}
		
		//Sinon, on crée l'instance
		else {
		
			String requete = "SELECT * FROM programme WHERE titre = '" + titre.replace("'", "''") + "';";
			Jdbc base = Jdbc.getInstance();
			Vector resultats = base.executeQuery(requete);
			
			// S'il y a un resultat
			if (resultats != null && resultats.size()>0) {
				
				//On prend le 1er élément
				Dictionary dico = (Dictionary) resultats.firstElement();
				
				//On mappe les champs
				String id = String.valueOf((Integer)dico.get("id"));
				String thematique = ((String)dico.get("thematique")).replace("''", "'");
				long duree = (long)(Long)dico.get("duree");
				
				System.out.println("-------- Programme -----------");
				System.out.println("Nb de champs: "+dico.size());
				System.out.println("ID: "+id);
				System.out.println("Titre: "+titre);
				System.out.println("Thématique: "+thematique);
				System.out.println("Durée: "+duree);
				System.out.println("-----------------------------");
				
				//On retourne l'objet
				Programme prog = new Programme(id, titre, thematique, duree);
				instancesById.put(prog.getId(), prog);
				instancesByTitre.put(prog.getTitre(), prog);
				prog.setDocumentsProgrammes();
				logger.debug("Arrêt: Programme.getByTitre");
				return prog;
			}
			
			//Sinon, on retourne un objet vide
			logger.debug("Arrêt: Programme.getByTitre");
			return null;
		}
	}
	
	/**
	 * Insancie un objet programme après avoir récupéré ces infos depuis la base à partir de son id
	 * @param String id
	 * @return Programme
	 * @throws SQLException 
	 */
	public static Programme getById(String id) /*throws SQLException*/ {
		logger.debug("Démarrage: Programme.getById");
		logger.info("Programme.getById("+id+")");
		
		//Si le programme est déjà instancié
		if (instancesById.containsKey(id)) {
			System.out.println("Instance trouvée pour Programme "+id);
			logger.debug("Arrêt: Programme.getById");
			return (Programme)instancesById.get(id);
		}
		
		//Sinon, on crée l'instance
		else {
		
			String requete = "SELECT * FROM programme WHERE id = '" + id + "';";
			Jdbc base = Jdbc.getInstance();
			Vector resultats = base.executeQuery(requete);
	
			// S'il y a un resultat
			if (resultats != null && resultats.size()>0) {
				
				//On prend le 1er élément
				Dictionary dico = (Dictionary) resultats.firstElement();
				
				//On mappe les champs
				String titre = ((String)dico.get("titre")).replace("''", "'");
				String thematique = ((String)dico.get("thematique")).replace("''", "'");
				long duree = (long)(Long)dico.get("duree");
				
				System.out.println("-------- Programme -----------");
				System.out.println("Nb de champs: "+dico.size());
				System.out.println("ID: "+id);
				System.out.println("Titre: "+titre);
				System.out.println("Thématique: "+thematique);
				System.out.println("Durée: "+duree);
				System.out.println("-----------------------------");
				
				//On retourne l'objet
				Programme prog = new Programme(id, titre, thematique, duree);
				instancesById.put(prog.getId(), prog);
				instancesByTitre.put(prog.getTitre(), prog);
				prog.setDocumentsProgrammes();
				logger.debug("Arrêt: Programme.getById");
				return prog;
			}
			
			//Sinon, on retourne un objet vide
			logger.debug("Arrêt: Programme.getById");
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
				
		//On crée un vecteur pour contenir les objets programmes instanciés
		Hashtable programmes = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id FROM programme;";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		logger.info("Programme.getAll() : "+resultats.size()+" programme(s) trouvé(s)");
		
		// Pour chaque programme, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id"));
			programmes.put(id, getById(id));
		}

		
		//On retourne le vecteur contenant les objets programmes instanciés
		logger.debug("Arrêt: getAll");
		return programmes;
	}
	
	/**
	 * Détruit les infos d'un programme contenues dans la base
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) /*throws SQLException*/ {
		logger.debug("Démarrage: deleteById");
		//On supprime le programme de la base, en partant d'un id
		String requete = "DELETE FROM programme WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la ligne est bien supprimée de la base
		if (nbRows>0) {
			
			//On retire l'instance
			Programme p = getById(id);
			instancesByTitre.remove(p.getTitre());
			instancesById.remove(id);
			logger.debug("Arrêt: deleteById");
			return true;
		}
		
		//Sinon, suppression invalide
		logger.debug("Arrêt: deleteById");
		return false;
	}
	
}
