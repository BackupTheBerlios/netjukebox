package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

public class ContratFactory {

//ATTRIBUTS
//-----------------------------------------

	/**
	 * Instances organis�es par ID
	 */
	private static Hashtable instancesById = new Hashtable();
	
	/**
	 * Instances organis�es par Titre
	 */
	private static Hashtable instancesByTitre = new Hashtable();
		
//METHODES STATIQUES
//-----------------------------------------	
	
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
	 * V�rifie s'il y a une instance pour le titre demand�
	 * @param String key
	 * @return boolean
	 */
	public static boolean containsTitre(String key) {
		return instancesByTitre.containsKey(key);
	}
	
	/**
	 * Cr�ation du contrat en base
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
		
		System.out.println("ContratFactory.create()");
		
		//On assemble la date de signature
		GregorianCalendar dateSignature = new GregorianCalendar(anneeSignature, moisSignature-1, jourSignature);
		
		//On assemble la date d'expiration
		GregorianCalendar dateExpiration = new GregorianCalendar(anneeExpiration, moisExpiration-1, jourExpiration);
		
		//On cr�e le contractant dans la base
		String requete = "INSERT INTO contrat (titre, signature, expiration, id_contractant, reglement, type) VALUES ('" +
			titre + "', '" + dateSignature.getTimeInMillis() + "', '" + dateExpiration.getTimeInMillis() +
			"', '" + idContractant + "', '" + modeReglement + "', '" + type + "');"; 
		
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien d�roul�e
		if (nbRows>0) {
			
			//On instancie l'objet correspondant
			Contrat c = getByTitre(titre);
			
			//On informe le contractant de la cr�ation du contrat
			Contractant ct = c.getContractant();
			ct.ajouterContrat(c);
			
			//On retourne ensuite un objet pour ce contrat
			return c;
		}
		
		//Sinon on retourne un objet vide
		return null;
	}
	
	/**
	 * Instancie un objet contrat apr�s avoir r�cup�r� ces infos depuis la base � partir de son id
	 * @param String id
	 * @return Contrat
	 */
	public static Contrat getById(String id) /*throws SQLException*/ {
		
		System.out.println("ContratFactory.getById("+id+")");
		
		//Si le contrat est d�j� instanci�
		if (instancesById.containsKey(id)) {
			System.out.println("Instance trouv�e pour Contrat "+id);
			return (Contrat)instancesById.get(id);
		}
		
		//Sinon, on cr�e l'instance
		else {
			System.out.println("Nouvelle instance pour Contrat "+id);
			String requete = "SELECT * FROM contrat WHERE id = '" + id + "';";
	
			Jdbc base = Jdbc.getInstance();
			Vector resultats = base.executeQuery(requete);
			
			//S'il y a un resultat
			if (resultats!=null && resultats.size()>0) {
				
				//On prend le 1er �l�ment
				Dictionary dico = (Dictionary) resultats.firstElement();
				
				//On mappe les champs
				String titre = (String)dico.get("titre");
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
				System.out.println("Mode de r�glement: "+modeReglement);
				System.out.println("Type: "+type);
				System.out.println("-----------------------------");
				
				//On retourne l'objet
				Contrat contrat = new Contrat(id, titre, dateSignature, dateExpiration, idContractant, modeReglement, type);
				instancesById.put(id, contrat);
				instancesByTitre.put(titre, contrat);
				contrat.setDocumentsAssocies();
				return contrat;
			}
			
			//Sinon, on retourne un objet vide
			return null;
		}
	}
	
	/**
	 * Instancie un objet contrat apr�s avoir r�cup�r� ces infos depuis la base � partir de son titre
	 * @param String id
	 * @return Contrat
	 */
	public static Contrat getByTitre(String titre) /*throws SQLException*/ {
		
		System.out.println("ContratFactory.getByTitre("+titre+")");
		
		//Si le contrat est d�j� instanci�
		if (instancesByTitre.containsKey(titre)) {
			return (Contrat)instancesByTitre.get(titre);
		}
		
		//Sinon, on cr�e l'instance
		else {
		
			String requete = "SELECT * FROM contrat WHERE titre = '" + titre + "';";
	
			Jdbc base = Jdbc.getInstance();
			Vector resultats = base.executeQuery(requete);
			
			//S'il y a un resultat
			if (resultats!=null && resultats.size()>0) {
				
				//On prend le 1er �l�ment
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
				System.out.println("Mode de r�glement: "+modeReglement);
				System.out.println("Type: "+type);
				System.out.println("-----------------------------");
				
				//On retourne l'objet
				Contrat contrat = new Contrat(id, titre, dateSignature, dateExpiration, idContractant, modeReglement, type);
				instancesById.put(id, contrat);
				instancesByTitre.put(titre, contrat);
				contrat.setDocumentsAssocies();
				return contrat;
			}
			
			//Sinon, on retourne un objet vide
			return null;
		}
	}
	
	/**
	 * Retourne un vecteur d'objets contrat instanci�s � partir de toutes les infos de la base
	 * @return Hashtable
	 * @throws SQLException 
	 */

	public static Hashtable getAll() /*throws SQLException*/ {
		
		System.out.println("ContratFactory.getAll()");
		
		//On cr�e un vecteur pour contenir les objets documents instanci�s
		Hashtable contrats = new Hashtable();
		
		//On va chercher dans la liste des id de tous les documents
		String requete = "SELECT id FROM contrat;";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		System.out.println("ContratFactory.getAll() : "+resultats.size()+" contrat(s) trouv�(s)");
		
		//Pour chaque document, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id"));
			contrats.put(id, getById(id));
		}
		
		//On retourne le vecteur contenant les objets contractants instanci�s
		return contrats;
	}
	
	/**
	 * D�truit les infos d'un contrat contenues dans la base
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) /*throws SQLException*/ {
		
		//On supprime le contractant de la base, en partant d'un id
		String requete = "DELETE FROM contrat WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la ligne est bien supprim�e de la base
		if (nbRows>0) {
			
			//On retire l'instance
			Contrat c = getById(id);
			instancesByTitre.remove(c.getTitre());
			instancesById.remove(id);
			return true;
		}
		
		//Sinon, suppression invalide
		return false;
	}
}