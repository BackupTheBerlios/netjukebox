package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

public class ContratFactory {
	/**
	 * Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(ContratFactory.class);

//ATTRIBUTS
//-----------------------------------------

	/**
	 * Instances organisées par ID
	 */
	private static Hashtable instancesById = new Hashtable();
	
	/**
	 * Instances organisées par Titre
	 */
	private static Hashtable instancesByTitre = new Hashtable();
		
//METHODES STATIQUES
//-----------------------------------------	
	
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
	 * Vérifie s'il y a une instance pour le titre demandé
	 * @param String key
	 * @return boolean
	 */
	public /*pure*/ static boolean containsTitre(String key) {
		return instancesByTitre.containsKey(key);
	}
	
	/**
	 * Création du contrat en base
	 * @param String titre
	 * @param int jourSignature
	 * @param int moisSignature
	 * @param int anneeSignature
	 * @param int jourExpiration
	 * @param int moisExpiration
	 * @param int anneExpiration
	 * @param String idContractant
	 * @param String modeReglement
	 * @param String type
	 * @return Contrat
	 */
	public static Contrat create(String titre, int jourSignature, int moisSignature, int anneeSignature,
			int jourExpiration, int moisExpiration, int anneeExpiration, String idContractant,
			String modeReglement, String type) /*throws SQLException*/ {
		
		logger.debug("Démarrage: create");
		
		//On échappe les ' du titre
		String titreSQL = titre.replace("'", "''");
		
		//On assemble la date de signature
		GregorianCalendar dateSignature = new GregorianCalendar(anneeSignature, moisSignature-1, jourSignature);
		
		//On assemble la date d'expiration
		GregorianCalendar dateExpiration = new GregorianCalendar(anneeExpiration, moisExpiration-1, jourExpiration);
		
		//On crée le contractant dans la base
		String requete = "INSERT INTO contrat (titre, signature, expiration, id_contractant, reglement, type) VALUES ('" +
			titreSQL + "', '" + dateSignature.getTimeInMillis() + "', '" + dateExpiration.getTimeInMillis() +
			"', '" + idContractant + "', '" + modeReglement + "', '" + type + "');"; 
		
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien déroulée
		if (nbRows>0) {
			
			//On instancie l'objet correspondant
			Contrat c = getByTitre(titre);
			
			//On informe le contractant de la création du contrat
			Contractant ct = c.getContractant();
			ct.ajouterContrat(c);
			
			//On retourne ensuite un objet pour ce contrat
			logger.debug("Arrêt: create");
			return c;
		}
		
		//Sinon on retourne un objet vide
		logger.debug("Arrêt: create");
		return null;
	}
	
	/**
	 * Instancie un objet contrat après avoir récupéré ces infos depuis la base à partir de son id
	 * @param String id
	 * @return Contrat
	 */
	public static Contrat getById(String id) /*throws SQLException*/ {
		logger.debug("Démarrage: getById("+id+")");
		
		//Si le contrat est déjà instancié
		if (instancesById.containsKey(id)) {
			logger.info("Instance trouvée pour Contrat "+id);
			logger.debug("Arrêt: getById");
			return (Contrat)instancesById.get(id);
		}
		
		//Sinon, on crée l'instance
		else {
			logger.info("Nouvelle instance pour Contrat "+id);
			String requete = "SELECT * FROM contrat WHERE id = '" + id + "';";
	
			Jdbc base = Jdbc.getInstance();
			Vector resultats = base.executeQuery(requete);
			
			//S'il y a un resultat
			if (resultats!=null && resultats.size()>0) {
				
				//On prend le 1er élément
				Dictionary dico = (Dictionary) resultats.firstElement();
				
				//On mappe les champs
				String titre = (String)dico.get("titre");
				titre.replace("''", "'");
				String idContractant = (String)dico.get("id_contractant");
				String modeReglement = (String)dico.get("reglement");
				String type = (String)dico.get("type");
				
				//On assemble la date de signature
				GregorianCalendar dateSignature = new GregorianCalendar();
				dateSignature.setTimeInMillis(Long.valueOf((String)dico.get("signature")));
				int jourSignature = dateSignature.get(GregorianCalendar.DATE);
				int moisSignature = dateSignature.get(GregorianCalendar.MONTH);
				int anneeSignature = dateSignature.get(GregorianCalendar.YEAR);
				
				//On assemble la date d'expiration
				GregorianCalendar dateExpiration = new GregorianCalendar();
				dateExpiration.setTimeInMillis(Long.valueOf((String)dico.get("expiration")));
				int jourExpiration = dateExpiration.get(GregorianCalendar.DATE);
				int moisExpiration = dateExpiration.get(GregorianCalendar.MONTH);
				int anneeExpiration = dateExpiration.get(GregorianCalendar.YEAR);
				
				System.out.println("-------- Contrat -----------");
				System.out.println("Nb de champs: "+dico.size());
				System.out.println("ID: "+id);
				System.out.println("Titre: "+titre);
				System.out.println("Signature: "+jourSignature+"/"+moisSignature+"/"+anneeSignature);
				System.out.println("Expiration: "+jourExpiration+"/"+moisExpiration+"/"+anneeExpiration);
				System.out.println("Contractant: "+idContractant);
				System.out.println("Mode de règlement: "+modeReglement);
				System.out.println("Type: "+type);
				System.out.println("-----------------------------");
				
				//On retourne l'objet
				Contrat contrat = new Contrat(id, titre, dateSignature, dateExpiration, idContractant, modeReglement, type);
				instancesById.put(id, contrat);
				instancesByTitre.put(titre, contrat);
				contrat.setDocumentsAssocies();
				logger.debug("Arrêt: getById");
				return contrat;
			}
			
			//Sinon, on retourne un objet vide
			logger.debug("Arrêt: getById");
			return null;
		}
	}
	
	/**
	 * Instancie un objet contrat après avoir récupéré ces infos depuis la base à partir de son titre
	 * @param String id
	 * @return Contrat
	 */
	public static Contrat getByTitre(String titre) /*throws SQLException*/ {
		logger.debug("Démarrage: getByTitre("+titre+")");
		
		//Si le contrat est déjà instancié
		if (instancesByTitre.containsKey(titre)) {
			logger.info("Instance trouvée pour Contrat "+titre);
			logger.debug("Arrêt: getByTitre");
			return (Contrat)instancesByTitre.get(titre);
		}
		
		//Sinon, on crée l'instance
		else {
			String titreSQL = titre.replace("'", "''");
			String requete = "SELECT * FROM contrat WHERE titre = '" + titreSQL + "';";
	
			Jdbc base = Jdbc.getInstance();
			Vector resultats = base.executeQuery(requete);
			
			//S'il y a un resultat
			if (resultats!=null && resultats.size()>0) {
				
				//On prend le 1er élément
				Dictionary dico = (Dictionary) resultats.firstElement();
				
				//On mappe les champs
				String id = String.valueOf((Integer)dico.get("id"));
				String idContractant = (String)dico.get("id_contractant");
				String modeReglement = (String)dico.get("reglement");
				String type = (String)dico.get("type");
				
				//On assemble la date de signature
				GregorianCalendar dateSignature = new GregorianCalendar();
				dateSignature.setTimeInMillis(Long.valueOf((String)dico.get("signature")));
				int jourSignature = dateSignature.get(GregorianCalendar.DATE);
				int moisSignature = dateSignature.get(GregorianCalendar.MONTH);
				int anneeSignature = dateSignature.get(GregorianCalendar.YEAR);
				
				//On assemble la date d'expiration
				GregorianCalendar dateExpiration = new GregorianCalendar();
				dateExpiration.setTimeInMillis(Long.valueOf((String)dico.get("expiration")));
				int jourExpiration = dateExpiration.get(GregorianCalendar.DATE);
				int moisExpiration = dateExpiration.get(GregorianCalendar.MONTH);
				int anneeExpiration = dateExpiration.get(GregorianCalendar.YEAR);
				
				System.out.println("-------- Contrat -----------");
				System.out.println("Nb de champs: "+dico.size());
				System.out.println("ID: "+id);
				System.out.println("Titre: "+titre);
				System.out.println("Signature: "+jourSignature+"/"+moisSignature+"/"+anneeSignature);
				System.out.println("Expiration: "+jourExpiration+"/"+moisExpiration+"/"+anneeExpiration);
				System.out.println("Contractant: "+idContractant);
				System.out.println("Mode de règlement: "+modeReglement);
				System.out.println("Type: "+type);
				System.out.println("-----------------------------");
				
				//On retourne l'objet
				Contrat contrat = new Contrat(id, titre, dateSignature, dateExpiration, idContractant, modeReglement, type);
				instancesById.put(id, contrat);
				instancesByTitre.put(titre, contrat);
				contrat.setDocumentsAssocies();
				logger.debug("Arrêt: getByTitre");
				return contrat;
			}
			
			//Sinon, on retourne un objet vide
			logger.debug("Arrêt: getByTitre");
			return null;
		}
	}
	
	/**
	 * Retourne un vecteur d'objets contrat instanciés à partir de toutes les infos de la base
	 * @return Hashtable
	 * @throws SQLException 
	 */

	public static Hashtable getAll() /*throws SQLException*/ {
		logger.debug("Démarrage: getAll");
		
		//On crée un vecteur pour contenir les objets documents instanciés
		Hashtable contrats = new Hashtable();
		
		//On va chercher dans la liste des id de tous les documents
		String requete = "SELECT id FROM contrat;";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		logger.info("ContratFactory.getAll() : "+resultats.size()+" contrat(s) trouvé(s)");
		
		//Pour chaque document, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id"));
			contrats.put(id, getById(id));
		}
		
		//On retourne le vecteur contenant les objets contractants instanciés
		logger.debug("Arrêt: getAll");
		return contrats;
	}
	
	/**
	 * Détruit les infos d'un contrat contenues dans la base
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) /*throws SQLException*/ {
		logger.debug("Démarrage: deleteById");
		//On supprime le contractant de la base, en partant d'un id
		String requete = "DELETE FROM contrat WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la ligne est bien supprimée de la base
		if (nbRows>0) {
			
			//On retire l'instance
			Contrat c = getById(id);
			instancesByTitre.remove(c.getTitre());
			instancesById.remove(id);
			logger.debug("Arrêt: deleteById");
			return true;
		}
		
		//Sinon, suppression invalide
		logger.debug("Arrêt: deleteById");
		return false;
	}
}
