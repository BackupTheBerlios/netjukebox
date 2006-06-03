package proto.serveur;

import java.sql.SQLException;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

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
	 * Contrats relatifs au document
	 */
	private Hashtable contrats= new Hashtable();
	
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
	 * Ajoute le contrat qui insère le document dans sa liste
	 * @param Programme prog
	 */
	public void ajouterContrat(Contrat contrat) {
		logger.debug("Démarrage: ajouterContrat");
		//Si le contrat n'est pas déjà associé
		if (!contrats.contains(contrat.getId())) {
			
			//On ajoute le contrat à la liste
			contrats.put(contrat.getId(), contrat);
		}
		
		logger.info("Contrat associé au document");
		logger.debug("Arrêt: ajouterContrat");
	}

	/**
	 * Supprime de sa liste le contrat qui supprime le document
	 * @param String
	 */
	public void retirerContrat(String idContrat) {
		logger.debug("Démarrage: retirerContrat");
		//Si le contrat est associé au document
		if (contrats.containsKey(idContrat)) {
			
			//On dissocie le contrat
			contrats.remove(idContrat);
		}		
		logger.info("Contrat dissocié du document");
		logger.debug("Arrêt: retirerContrat");
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
		
		String requete = "UPDATE document SET titre = '" + titre.replace("'", "''") + "', duree = '" + duree +
			"', date = '"+ date.getTimeInMillis() + "', source = '" + source.replace("'", "''") + "', langue = '" + langue.replace("'", "''") +
			"', genre = '" + genre.replace("'", "''") + "', fichier = '" + fichier.replace("'", "''") + "', artiste = '"+ artiste.replace("'", "''") +
			"', interprete = '" + interprete.replace("'", "''") + "', compositeur = '"+ compositeur.replace("'", "''") + "' WHERE id = '" + id + "';";
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
		Enumeration progs = programmes.elements();
		while (progs.hasMoreElements()) {
			Programme p = (Programme)progs.nextElement();
			p.retirerDocument(id);
		}
		
		//On supprime les infos de la base
		logger.debug("Arrêt: supprimer");
		return DocumentFactory.deleteById(id);
	}

//#### GETTERS ####
//#################
	
	/**
	 * Retourne l'ensemble des attributs sous la forme d'un dictionnaire
	 * @return Dictionary
	 */
	public Dictionary getAttributesDictionary() {
	
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
		dico.put("jour", jour);
		dico.put("mois", mois);
		dico.put("annee", annee);
		dico.put("fichier", fichier);
		
		dico.put("artiste", artiste);
		dico.put("interprete", interprete);
		dico.put("compositeur", compositeur);
		
//		Liste des programmes
		Vector vProgs = new Vector();
		Dictionary dicoProg;
		Programme prog;
		Enumeration listeProgs = programmes.elements();
		while (listeProgs.hasMoreElements()) {
			prog = (Programme)listeProgs.nextElement();
			dicoProg = new Hashtable();
			dicoProg.put("id", prog.getId());
			dicoProg.put("titre", prog.getTitre());
			vProgs.add(dicoProg);
		}
		dico.put("programmes", vProgs);
		
		
//		Liste des contrats
		Vector vConts = new Vector();
		Dictionary dicoCont;
		Contrat cont;
		Enumeration listeConts = contrats.elements();
		while (listeConts.hasMoreElements()) {
			cont = (Contrat)listeConts.nextElement();
			dicoCont = new Hashtable();
			dicoCont.put("id", cont.getId());
			dicoCont.put("titre", cont.getTitre());
			vConts.add(dicoCont);
		}
		dico.put("contrats", vConts);
		

		return dico;
	}

	/**
	 * Retourne l'identifiant du document
	 * @return String
	 */
	public /*pure*/ String getId() {
		return id;
	}

	/**
	 * Retourne le titre du document
	 * @return String
	 */
	public /*pure*/ String getTitre() {
		return titre;
	}

	/**
	 * Retourne la durée du document
	 * @return int
	 */
	public /*pure*/ int getDuree() {
		return duree;
	}

	/**
	 * Retourne la source du document
	 * @return String
	 */
	public /*pure*/ String getSource() {
		return source;
	}

	/**
	 * Retourne la langue du documennt
	 * @return String
	 */
	public /*pure*/ String getLangue() {
		return langue;
	}

	/**
	 * Retourne le genre du document
	 * @return String
	 */
	public /*pure*/String getGenre() {
		return genre;
	}

	/**
	 * Retourne le chemin où stocké le document
	 * @return String
	 */
	public /*pure*/ String getFichier() {
		return fichier;
	}

	/**
	 * Retourne l'état du document
	 * @return String
	 */
	public /*pure*/ String getEtat() {
		return etat;
	}
	
	/**
	 * Affiche le nombre de verrous sur le document
	 * Affiche les programmes vérrouillant le document
	 */
	public /*pure*/ void compterVerrouProgramme() {
		logger.info("Le document : " + id + " est verrouillé : " + verrous.size() + " fois");
	}
	
	/**
	 * Affiche la liste des programmes vérrouillant le document
	 */
	public /*pure*/ void getProgrammesArchives() {
		int i;
		if (verrous.size() != 0) {
			for (i = 0; i < verrous.size(); i++) {
				String idProg = (String) verrous.elementAt(i);
				logger.info("Le document : " + id + " est verrouillé par le programme : " + idProg);
			}
		} else {
			logger.info("Le document : " + id + " est verrouillé par aucun programme");
		}
	}
	
	/**
	 * Retourne la date de création du document
	 * @return Date
	 */
	//public /*pure*/ Date getDateCreation() {
	/*	
		int jour = dateParution.get(GregorianCalendar.DATE);
		int mois = dateParution.get(GregorianCalendar.MONTH);
		int annee = dateParution.get(GregorianCalendar.YEAR);

		String date = jour + "/"+ mois + "/" + annee;
		TransformeDate TD = new TransformeDate(date);
		logger.info(TD.d);
		return TD.d;
	}*/
	
	/**
	 * Retourne la date de création du document
	 * @return GregorianCalendar
	 */
	public GregorianCalendar getDate() {
		return dateParution;
	}
	
	/**
	 * Vérifie l'état EN LECTURE
	 * @return boolean
	 */
	public /*pure*/ boolean enLecture() {
		return etat.equalsIgnoreCase("LECTURE");
	}

	/**
	 * Vérifie l'état PROFRAMME
	 * @return boolean
	 */
	public /*pure*/ boolean estProgramme() {
		return etat.equalsIgnoreCase("PROGRAMME");
	}
	
	/**
	 * @return Renvoie artiste.
	 */
	public /*pure*/ String getArtiste() {
		return artiste;
	}

	/**
	 * @return Renvoie compositeur.
	 */
	public /*pure*/ String getCompositeur() {
		return compositeur;
	}

	/**
	 * @return Renvoie interprete.
	 */
	public /*pure*/ String getInterprete() {
		return interprete;
	}
		
//#### SETTERS ####
//#################
	
	/**
	 * Retourne la liste des documents programmés
	 * @return Hastable
	 */
	public /*pure*/ void setProgrammesAssocies() {
		logger.info("Document.getProgrammesAssocies()");
		
		//On crée un vecteur pour contenir les objets programmes instanciés
		Hashtable progs = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT DISTINCT id_prog FROM composer WHERE id_doc = '"+id+"';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		System.out.println("Document.getProgrammesAssocies() : "+resultats.size()+" programme(s) trouvé(s)");
		logger.info("Document.getProgrammesAssocies() : "+resultats.size()+" programme(s) trouvé(s)");
		
		// Pour chaque programme, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id_prog"));
			progs.put(id, ProgrammeFactory.getById(id));
		}
		
		this.programmes=progs;
	}
	
	/**
	 * Retourne la liste des contrats associés
	 * @return Hastable
	 */
	public /*pure*/ void setContratsAssocies() {
		logger.info("Document.getContratsAssocies()");
		
		//On crée un vecteur pour contenir les objets programmes instanciés
		Hashtable conts = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les contrats
		String requete = "SELECT DISTINCT id_contrat FROM contratdoc WHERE id_doc = '"+id+"';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		System.out.println("Document.getContratsAssocies() : "+resultats.size()+" contrat(s) trouvé(s)");
		logger.info("Document.getContratsAssocies() : "+resultats.size()+" contrat(s) trouvé(s)");
		
		// Pour chaque contrat, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id_contrat"));
			conts.put(id, ContratFactory.getById(id));
		}
		
		this.contrats=conts;
	}
	
	/**
	 * @param compositeur compositeur à définir.
	 */
	public boolean setCompositeur(String compositeur) {
		logger.debug("Démarrage: setCompositeur");
		
		String requete = "UPDATE document SET compositeur = '" + compositeur.replace("'", "''") + "' WHERE id = '" + id + "';";
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
		
		String requete = "UPDATE document SET artiste = '" + artiste.replace("'", "''") + "' WHERE id = '" + id + "';";
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

		String requete = "UPDATE document SET interprete = '" + interprete.replace("'", "''") + "' WHERE id = '" + id + "';";
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
	
		String requete = "UPDATE document SET fichier = '" + fichier.replace("'", "''") + "' WHERE id = '" + id + "';";
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

		String requete = "UPDATE document SET genre = '" + genre.replace("'", "''") + "' WHERE id = '" + id + "';";
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
		
		String requete = "UPDATE document SET langue = '" + langue.replace("'", "''") + "' WHERE id = '" + id + "';";
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
		
		String requete = "UPDATE document SET source = '" + source.replace("'", "''") + "' WHERE id = '" + id + "';";
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
		
		String requete = "UPDATE document SET titre = '" + titre.replace("'", "''") + "' WHERE id = '" + id + "';";
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