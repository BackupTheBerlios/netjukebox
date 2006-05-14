package proto.serveur;

import java.sql.SQLException;
import java.util.Date;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Document mis à disposition dans le NetJukeBox
 */
public class Document {

// ATTRIBUTS DU DOCUMENT
//********************************************

	/**
	 * Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(Document.class);
	
	/**
	 * Identifiant du document
	 */
	private String id;

	/**
	 * Titre du document
	 */
	private String titre;

	/**
	 * Source du document
	 */
	private String source;

	/**
	 * Durée du document
	 */
	private int duree;

	/**
	 * Date de parution du document
	 */
	private GregorianCalendar dateParution;

	/**
	 * Langue du document
	 */
	private String langue;

	/**
	 * Genre du document
	 */
	private String genre;

	/**
	 * Nom du fichier correspondant au document
	 */
	private String fichier;

	/**
	 * Etat du document
	 */
	private String etat;

	/**
	 * Liste de verrous sur le document
	 */
	private Vector verrous = new Vector();
	

	/**
	 * Liste des programmes associés au document
	 */
	private Hashtable programmes = new Hashtable();

	/**
	 * Contrat relatif au document
	 */
	private Contrat contrat;
	
	/**
	 * Artiste
	 */
	private String artiste;

	/**
	 * Interprète
	 */
	private String interprete;

	/**
	 * Compositeur
	 */
	private String compositeur;
	
	
// CONSTRUCTEUR
//********************************************
	
	/**
	 * Constructeur
	 * @param id
	 * @param titre
	 * @param duree
	 * @param GregorianCalendar dateParution
	 * @param source
	 * @param langue
	 * @param genre
	 * @param fichier
	 * 
	 * @param String artiste
	 * @param String interprete
	 * @param String compositeur
	 */
	public Document(String id, String titre, int duree, GregorianCalendar dateParution, String source,
			String langue, String genre, String fichier, String artiste, String interprete, String compositeur) {

		this.id = id;
		this.titre = titre;
		this.dateParution = dateParution;
		this.source = source;
		this.langue = langue;
		this.genre = genre;
		this.fichier = fichier;
		this.duree = duree;
		
		this.artiste = artiste;
		this.interprete = interprete;
		this.compositeur = compositeur;
	}

// METHODES STATIQUES
//********************************************
	
	/**
	 * Création du programme en base
	 * @param String titre
	 * @param int duree
	 * @param String jour
	 * @param String mois
	 * @param String annee
	 * @param String source
	 * @param String langue
	 * @param String genre
	 * @param String fichier
	 * @return Document
	 * @throws SQLException 
	 */
	public static Document create(String titre, int duree, int jour,
			int mois, int annee, String source, String langue, String genre,
			String fichier, String artiste, String interprete, String compositeur) /*throws SQLException*/ {
		
	
		PropertyConfigurator.configure("C:/Documents and Settings/Marie Rubini/Mes documents/workspace/NetJukeBox/proto/serveur/log4j.prop");
		logger.debug("Démarrage : Document.create");				
		
		//On assemble la date
		//String date = jour+"-"+mois+"-"+annee;
		GregorianCalendar date = new GregorianCalendar(annee, mois-1, jour);
		
		//On crée le document dans la base
		String requete = "INSERT INTO document (titre, duree, date, source, langue, genre, fichier, artiste, interprete, compositeur) VALUES ('" +
			titre + "', '" + duree + "', '" + date.getTimeInMillis() + "', '" + source + "', '" +langue + "', '" +
			genre + "', '" + fichier + "', '"+ artiste + "', '"+ interprete + "', '" + compositeur + "');"; 
		
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien déroulée
		if (nbRows>0) {
			//On retourne ensuite un objet pour ce document
			logger.debug("Arrêt: Document.create");
			return Document.getByTitre(titre);
		}
		//Sinon on retourne un objet vide
		logger.debug("Arrêt: Document.create");
		return null;
	}
	
	/**
	 * Instancie un objet document après avoir récupéré ces infos depuis la base à partir de son titre
	 * @param String titre
	 * @return Document
	 * @throws SQLException 
	 */
	public static Document getByTitre(String titre) /*throws SQLException*/ {
		logger.debug("Démarrage: Doument.getByTitre");
		logger.info("Document.getByTitre("+titre+")");
		
		String requete = "SELECT * FROM document WHERE titre = '" + titre + "';";

		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		//S'il y a un resultat
		if (resultats!=null && resultats.size()>0) {
			
			//On prend le 1er élément
			Dictionary dico = (Dictionary) resultats.firstElement();
			
			//On mappe les champs
			String id = String.valueOf((Integer)dico.get("id"));
			String titreDoc = (String)dico.get("titre");
			int duree = (int)(Integer)dico.get("duree");
			String source = (String)dico.get("source");
			String langue = (String)dico.get("langue");
			String genre = (String)dico.get("genre");
			String fichier = (String)dico.get("fichier");
			
			//On assemble la date
			GregorianCalendar date = new GregorianCalendar();
			date.setTimeInMillis(Long.valueOf((String)dico.get("date")));
			int jour = date.get(GregorianCalendar.DATE);
			int mois = date.get(GregorianCalendar.MONTH);
			int annee = date.get(GregorianCalendar.YEAR);
			
			String artiste = (String)dico.get("artiste");
			String interprete = (String)dico.get("interprete");
			String compositeur = (String)dico.get("compositeur");
			
			System.out.println("-------- Document -----------");
			System.out.println("Nb de champs: "+dico.size());
			System.out.println("ID: "+id);
			System.out.println("Titre: "+titreDoc);
			System.out.println("Duree: "+duree);
			System.out.println("Source: "+source);
			System.out.println("Langue: "+langue);
			System.out.println("Genre: "+genre);
			System.out.println("Fichier: "+fichier);
			System.out.println("Date: "+jour+"/"+mois+"/"+annee);
			System.out.println("Artiste: "+artiste);
			System.out.println("Interprète: "+interprete);
			System.out.println("Compositeur: "+compositeur);
			System.out.println("-----------------------------");
			
			//On retourne l'objet

			logger.debug("Arrêt: Document.getByTitre");	
			return new Document(id, titreDoc, duree, date, source, langue, genre, fichier, artiste, interprete, compositeur);
		}

	
		/*
		//----------- TEST SANS JDBC---------------------
		if (titre.equalsIgnoreCase("Rhapsody in Blue")) {
			//On retourne un objet document configuré
			return new Document("1", "Rhapsody in Blue", 1100, "01", "01", "2006", "Source", "FR", "classic", "file://home/philippe/njb/1.mp3");
		}
		//-----------------------------------------------
		*/
		
		//Sinon, on retourne un objet vide
		logger.debug("Arrêt: Document.getByTitre");
		return null;
	}
	
	/**
	 * Instancie un objet document après avoir récupéré ces infos depuis la base à partir de son id
	 * @param String id
	 * @return Document
	 * @throws SQLException 
	 */
	public static Document getById(String id) /*throws SQLException*/ {
		logger.debug("Démarrage: Document.getById");
		logger.info("Document.getById("+id+")");
		
		String requete = "SELECT * FROM document WHERE id = '" + id + "';";

		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		//S'il y a un resultat
		if (resultats!=null && resultats.size()>0) {
			
			//On prend le 1er élément
			Dictionary dico = (Dictionary) resultats.firstElement();
			
			//On mappe les champs
			String idDoc = String.valueOf((Integer)dico.get("id"));
			String titre = (String)dico.get("titre");
			int duree = (int)(Integer)dico.get("duree");
			String source = (String)dico.get("source");
			String langue = (String)dico.get("langue");
			String genre = (String)dico.get("genre");
			String fichier = (String)dico.get("fichier");
			
			String artiste = (String)dico.get("artiste");
			String interprete = (String)dico.get("interprete");
			String compositeur = (String)dico.get("compositeur");
			
			//On assemble la date
			GregorianCalendar date = new GregorianCalendar();
			date.setTimeInMillis(Long.valueOf((String)dico.get("date")));
			int jour = date.get(GregorianCalendar.DATE);
			int mois = date.get(GregorianCalendar.MONTH);
			int annee = date.get(GregorianCalendar.YEAR);
			
			
			System.out.println("-------- Document -----------");
			System.out.println("Nb de champs: "+dico.size());
			System.out.println("ID: "+idDoc);
			System.out.println("Titre: "+titre);
			System.out.println("Duree: "+duree);
			System.out.println("Source: "+source);
			System.out.println("Langue: "+langue);
			System.out.println("Genre: "+genre);
			System.out.println("Fichier: "+fichier);
			System.out.println("Date: "+jour+"/"+mois+"/"+annee);
			System.out.println("Artiste: "+artiste);
			System.out.println("Interprète: "+interprete);
			System.out.println("Compositeur: "+compositeur);
			System.out.println("-----------------------------");
			
			//On retourne l'objet

			logger.debug("Arrêt: Document.getById");
			return new Document(idDoc, titre, duree, date, source, langue, genre, fichier, artiste, interprete, compositeur);

		}

		/*
		//----------- TEST SANS JDBC---------------------
		if (id.equalsIgnoreCase("1")) {
			//On retourne un objet document configuré
			return new Document("1", "Rhapsody in Blue", 1100, "01", "01", "2006", "Source", "FR", "classic", "file://home/philippe/njb/1.mp3");
		}
		//-----------------------------------------------
		*/
		
		//Sinon, on retourne un objet vide
		logger.debug("Arrêt: Document.getById");
		return null;
	}
	
	/**
	 * Retourne un vecteur d'objets documents instanciés à partir de toutes les infos de la base 

	 * @return Hashtable
	 * @throws SQLException 
	 */

	public static Hashtable getAll() /*throws SQLException*/ {
		logger.debug("Démarrage: getAll");
		
		//On crée un vecteur pour contenir les objets documents instanciés
		Hashtable documents = new Hashtable();
		
		//On va chercher dans la liste des id de tous les documents
		String requete = "SELECT id FROM document;";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		logger.info("Document.getAll() : "+resultats.size()+" document(s) trouvé(s)");
		
		//Pour chaque document, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id"));
			documents.put(id, Document.getById(id));
		}

		/*
		//----------- TEST SANS JDBC---------------------
		documents.put("1", Document.getById("1"));
		//-----------------------------------------------
		*/
		
		//On retourne le vecteur contenant les objets documents instanciés

		logger.debug("Arrêt: getAll");
		return documents;
	}
	
	/**
	 * Détruit les infos d'un document contenues dans la base
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) /*throws SQLException*/ {
		logger.debug("Démarrage: deleteById");
		
		//On supprime le document de la base, en partant d'un id
		String requete = "DELETE FROM document WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//On retourne le resultat de l'opération (succès/échec)
		logger.debug("Arrêt: deleteById");
		return nbRows > 0;
	}

// METHODES DYNAMIQUES
//*********************************************
	
	/**
	 * Attribue un nom au fichier
	 * @param fichier
	 */
	public void nommerFichier(String fichier) {
		// your code here
	}

	public void selectionner() {
		// your code here
	}
	
	/**
	 * Ajoute le programme qui insère le document dans sa liste
	 * Appel la fonction de pose d'un verrou
	 * @param Programme prog
	 */
	public void ajouterProgramme(Programme prog) {
		logger.debug("Démarrage: ajouterProgramme");
		
		//S'il n'y a pas de verrous pour le programme
		if (!verrous.contains(prog.getId())) {
			
			//On ajoute le prog à la liste des programmes associés
			programmes.put(prog.getId(), prog);
		}
		
		//On pose un verrou pour le programme
		verrous.addElement(prog.getId());
		
		logger.info("Document verrouillé : " + this.id);
		logger.info("Le compteur de verrou = " + verrous.size());
		logger.debug("Arrêt: ajouterProgramme");
	}

	/**
	 * Supprime de sa liste le programme qui supprime le document
	 * Appel la fonction de déverrouillage
	 * @param String
	 */
	public void retirerProgramme(String idProg) {
		logger.debug("Démarrage: retirerProgramme");
				
		//On enlève un verrou pour le programme
		verrous.remove(idProg);
		
		//S'il n'y a plus de verrous pour le programme
		if (!verrous.contains(idProg)) {
			
			//Le document n'est plus dans ce programme.
			//On le retire de la liste des progs associés
			programmes.remove(idProg);
		}
		
		logger.info("Document déverrouillé : " + this.id);
		logger.info("Le compteur de verrou = " + verrous.size());
		logger.debug("Arrêt: retirerProgramme");
	}
	
	/**
	 * Modifie les attributs
	 * @param String titre
	 * @param int duree
	 * @param String jour
	 * @param String mois
	 * @param String annee
	 * @param String source
	 * @param String langue
	 * @param String genre
	 * @param String fichier
	 * @return boolean
	 */
	public boolean modifier(String titre, int duree, int jour,
			int mois, int annee, String source, String langue, String genre,
			String fichier, String artiste, String interprete, String compositeur) {
		
		logger.debug("Démarrage: modifier");

		GregorianCalendar date = new GregorianCalendar(annee, mois, jour);
		
		String requete = "UPDATE document SET titre = '" + titre + "', duree = '" + duree +
			"', date = '"+ date.getTimeInMillis() + "', source = '" + source + "', langue = '" + langue +
			"', genre = '" + langue + "', fichier = '" + fichier + "', artiste = '"+ artiste +
			"', interprete = '" + interprete + "', compositeur = '"+ compositeur + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.titre = titre;
			this.dateParution = date;
			this.source = source;
			this.langue = langue;
			this.genre = genre;
			this.fichier = fichier;
			this.duree = duree;
			
			this.artiste = artiste;
			this.compositeur = compositeur;
			this.interprete = interprete;
		}
		logger.debug("Arrêt: modifier");
		return nbRows>0;
	}
	
	/**
	 * Détruit le document et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() /*throws SQLException*/ {
		logger.debug("Démarrage: supprimer");
				
		//On supprime les associations document/programme
		Programme p;
		for (int i=0; i<programmes.size(); i++){
			p = (Programme)programmes.get(i);
			p.retirerDocument(id);
		}
		
		//On supprime les infos de la base
		logger.debug("Arrêt: supprimer");
		return Document.deleteById(id);
	}

//#### GETTERS ####
//#################
	
	/**
	 * Retourne l'ensemble des attributs sous la forme d'un dictionnaire
	 * @return Dictionary
	 */
	public Dictionary getAttributesDictionary() {
		logger.debug("Démarrage: getAttributesDictionary");
	
		int jour = dateParution.get(GregorianCalendar.DATE);
		int mois = dateParution.get(GregorianCalendar.MONTH);
		int annee = dateParution.get(GregorianCalendar.YEAR);
		
		Dictionary dico = new Hashtable();
		
		dico.put("id", id);
		dico.put("titre", titre);
		dico.put("duree", duree);
		dico.put("genre", genre);
		dico.put("source", source);
		dico.put("langue", langue);
		dico.put("date", (jour+"/"+mois+"/"+annee));
		dico.put("fichier", fichier);
		
		dico.put("artiste", artiste);
		dico.put("interprete", interprete);
		dico.put("compositeur", compositeur);

		logger.debug("Arrêt: getAttributesDictionary");
		return dico;
	}

	/**
	 * Retourne l'identifiant du document
	 * @return String
	 */
	public String getId() {
		logger.debug("Démarrage: getId");
		logger.debug("Arrêt: getId");
		return id;
	}

	/**
	 * Retourne le titre du document
	 * @return String
	 */
	public String getTitre() {
		logger.debug("Démarrage: getTitre");
		logger.debug("Arrêt: getTitre");
		return titre;
	}

	/**
	 * Retourne la durée du document
	 * @return int
	 */
	public int getDuree() {
		logger.debug("Démarrage: getDuree");
		logger.debug("Arrêt: getDuree");
		return duree;
	}

	/**
	 * Retourne la source du document
	 * @return String
	 */
	public String getSource() {
		logger.debug("Démarrage: getSource");
		logger.debug("Arrêt: getSource");
		return source;
	}

	/**
	 * Retourne la langue du documennt
	 * @return String
	 */
	public String getLangue() {
		logger.debug("Démarrage: getLangue");
		logger.debug("Arrêt: getLangue");
		return langue;
	}

	/**
	 * Retourne le genre du document
	 * @return String
	 */
	public String getGenre() {
		logger.debug("Démarrage: getGenre");
		logger.debug("Arrêt: getGenre");
		return genre;
	}

	/**
	 * Retourne le chemin où stocké le document
	 * @return String
	 */
	public String getFichier() {
		logger.debug("Démarrage: getFichier");
		logger.debug("Arrêt: getFichier");
		return fichier;
	}

	/**
	 * Retourne l'état du document
	 * @return String
	 */
	public String getEtat() {
		logger.debug("Démarrage: getEtat");
		logger.debug("Arrêt: getEtat");
		return etat;
	}
	
	/**
	 * Affiche le nombre de verrous sur le document
	 * Affiche les programmes vérrouillant le document
	 */
	public void compterVerrouProgramme() {
		logger.debug("Démarrage: compterVerrouProgramme");
		logger.info("Le document : " + id + " est verrouillé : " + verrous.size() + " fois");
		logger.debug("Arrêt: compterVerrouProgramme");
	}
	
	/**
	 * Affiche la liste des programmes vérrouillant le document
	 */
	public void getProgrammesArchives() {
	logger.debug("Démarrage: getProgrammesArchives");

		int i;
		if (verrous.size() != 0) {
			for (i = 0; i < verrous.size(); i++) {
				String idProg = (String) verrous.elementAt(i);
				logger.info("Le document : " + id + " est verrouillé par le programme : " + idProg);
			}
		} else {
			logger.info("Le document : " + id + " est verrouillé par aucun programme");
		}
		logger.debug("Arrêt: getProgrammesArchives");
	}
	
	/**
	 * Retourne la date de création du document
	 * @return Date
	 */
	public Date getDateCreation() {

		logger.debug("Démarrage: getDateCreation");
		
		int jour = dateParution.get(GregorianCalendar.DATE);
		int mois = dateParution.get(GregorianCalendar.MONTH);
		int annee = dateParution.get(GregorianCalendar.YEAR);

		String date = jour + "/"+ mois + "/" + annee;
		TransformeDate TD = new TransformeDate(date);
		logger.info(TD.d);
		logger.debug("Arrêt: getDateCreation");
		return TD.d;
	}
	
	/**
	 * Vérifie l'état EN LECTURE
	 * @return boolean
	 */
	public boolean enLecture() {
		logger.debug("Démarrage: enLecture");
		logger.debug("Arrêt: enLecture");
		return etat.equalsIgnoreCase("LECTURE");
	}

	/**
	 * Vérifie l'état PROFRAMME
	 * @return boolean
	 */
	public boolean estProgramme() {
		logger.debug("Démarrage: estProgramme");
		logger.debug("Arrêt: estProgramme");
		return etat.equalsIgnoreCase("PROGRAMME");
	}
	
	/**
	 * @return Renvoie artiste.
	 */
	public String getArtiste() {
		logger.debug("Démarrage: getArtiste");
		logger.debug("Arrêt: getArtiste");
		return artiste;
	}

	/**
	 * @return Renvoie compositeur.
	 */
	public String getCompositeur() {
		logger.debug("Démarrage: getCompositeur");
		logger.debug("Arrêt: getCompositeur");
		return compositeur;
	}

	/**
	 * @return Renvoie interprete.
	 */
	public String getInterprete() {
		logger.debug("Démarrage: getInterprete");
		logger.debug("Arrêt: getInterprete");
		return interprete;
	}
		
//#### SETTERS ####
//#################
	/**
	 * @param compositeur compositeur à définir.
	 */
	public boolean setCompositeur(String compositeur) {
		logger.debug("Démarrage: setCompositeur");
		
		String requete = "UPDATE document SET compositeur = '" + compositeur + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attribut de l'objet
		if (nbRows>0) {
			this.compositeur = compositeur;
		}
		logger.debug("Arrêt: setCompositeur");
		return nbRows>0;
	}
	/**
	 * @param artiste artiste à définir.
	 */
	public boolean setArtiste(String artiste) {
		logger.debug("Démarrage: setArtiste");
		
		String requete = "UPDATE document SET artiste = '" + artiste + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attribut de l'objet
		if (nbRows>0) {
			this.artiste = artiste;
		}
		logger.debug("Arrêt: setArtiste");
		return nbRows>0;
	}
	/**
	 * @param interprete interprete à définir.
	 */
	public boolean setInterprete(String interprete) {
		logger.debug("Démarrage: setInterprete");

		String requete = "UPDATE document SET interprete = '" + interprete + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attribut de l'objet
		if (nbRows>0) {
			this.interprete = interprete;
		}
		logger.debug("Arrêt: setInterprete");
		return nbRows>0;
	}
	
	/**
	 * Met à jour l'attribut fichier
	 * @param String fichier
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setFichier(String fichier) /*throws SQLException*/ {
		logger.debug("Démarrage: setFichier");
	
		String requete = "UPDATE document SET fichier = '" + fichier + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.fichier = fichier;
		}
		logger.debug("Arrêt: setFichier");
		return nbRows>0;
	}
	
	/**
	 * Met à jour l'attribut genre
	 * @param genre
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setGenre(String genre) /*throws SQLException*/ {
		logger.debug("Démarrage: setGenre");

		String requete = "UPDATE Document SET genre = '" + genre + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.genre = genre;
		}
		logger.debug("Arrêt: setGenre");
		return nbRows>0;
	}
	
	/**
	 * Met à jour l'attribut langue
	 * @param String langue
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setLangue(String langue) /*throws SQLException*/ {
		logger.debug("Démarrage: setLangue");
		
		String requete = "UPDATE document SET langue = '" + langue + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.langue = langue;
		}
		logger.debug("Arrêt: setLangue");
		return nbRows>0;
	}
	
	/**
	 * Met à jour l'attribut source
	 * @param String source
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setSource(String source) /*throws SQLException*/ {
		logger.debug("Démarrage: setSource");
		
		String requete = "UPDATE document SET source = '" + source + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.source = source;
		}
		logger.debug("Arrêt: setSource");
		return nbRows>0;
	}
	
	/**
	 * Met à jour l'attribut date
	 * @param int jour
	 * @param int mois
	 * @param int annee
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setDate(int jour, int mois, int annee) /*throws SQLException*/ {
		logger.debug("Démarrage: setDate");
		
		GregorianCalendar date = new GregorianCalendar(annee, mois, jour);
		
		String requete = "UPDATE document SET date = '" + date.getTimeInMillis() + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.dateParution = date;
		}
		logger.debug("Arret: setDate");
		return nbRows>0;
	}
	
	/**
	 * Met à jour l'attribut titre
	 * @param String titre
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setTitre(String titre) /*throws SQLException*/ {
		logger.debug("Démarrage: setTitre");
		
		String requete = "UPDATE document SET titre = '" + titre + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.titre = titre;
		}
		logger.debug("Arrêt: setTitre");
		return nbRows>0;
	}
	
	/**
	 * Met à jour l'attribut durée
	 * @param String duree
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setDuree(int duree) /*throws SQLException*/ {
		logger.debug("Démarrage: setDuree");
		
		String requete = "UPDATE document SET duree = '" + duree + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.duree = duree;
		}
		logger.debug("Arrêt: setDuree");
		return nbRows>0;
	}
}