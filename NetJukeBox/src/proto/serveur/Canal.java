package proto.serveur;

import java.sql.SQLException;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Canal de diffusion
 */
public class Canal {

// ATTRIBUTS DU CANAL (propres à un objet)
// *************************************************

	/**
	 * Identifiant
	 */
	private String id;

	/**
	 * Nom du canal
	 */
	private String nom;

	/**
	 * Nombre maximal d'auditeurs
	 */
	private int utilMax;

	
	/**
	 * Port de diffusion
	 */
	private String port;
	
	/**
	 * Etat du canal
	 */
	private String etat;

	/**
	 * #DEFINITION#
	 */
	private Diffusion diffusion;

	/**
	 * Historisation
	 */
	private JournalCanal journal;
	
	
	/**
	 * Auditeurs du canal
	 */
	private Vector auditeurs;
	
	/**
	 * RTP Server
	 */
	private RTPServer RTP = null;
	
	/**
	 * Liste de programmes à diffuser
	 */
	private Hashtable programmes;
	
	/**
	 * Timer
	 */
	private Timer timer;
	
	/**
	 * Threads de diffusion
	 */
	private Hashtable tasks;

// CONSTRUCTEUR (créé une instance de la classe : un objet)
// *************************************************
	
	/**
	 * Constructeur
	 * @param id
	 * @param nom
	 * @param fluxMax
	 */
	public Canal(String id, String nom, int utilMax) {
		this.auditeurs = new Vector();
		this.id = id;
		this.nom = nom;
		this.utilMax = utilMax;
		this.programmes = getProgrammesPlanifies();
		this.timer = new Timer(true);
		this.tasks = new Hashtable();
	}

// METHODES STATIQUES (propres à la classe, inaccessibles depuis un objet)
// *************************************************
	
	/**
	 * Création du canal en base
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
		
		//Si l'insertion s'est bien déroulée
		if (nbRows>0) {
			//On retourne ensuite un objet pour ce canal
			return Canal.getByNom(nom);
		}
		
		//Sinon, retourne un objet vide
		return null;
	}
	
	/**
	 * Créé et remplit un objet avec les infos issues de la base, à partir d'un nom
	 * @param String nom
	 * @return Canal
	 * @throws SQLException 
	 */
	public static Canal getByNom(String nom) /*throws SQLException*/ {
		
		System.out.println("Canal.getByNom("+nom+")");
		
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
			String nomCanal = (String)dico.get("nom");
			int utilMax = (int)(Integer)dico.get("utilmax");
			
			System.out.println("-------- Canal -----------");
			System.out.println("Nb de champs: "+dico.size());
			System.out.println("ID: "+id);
			System.out.println("Nom: "+nomCanal);
			System.out.println("Nb max d'auditeurs: "+utilMax);
			System.out.println("-----------------------------");
			
			//On retourne l'objet
			return new Canal(id, nomCanal, utilMax);
		}
		
		/*
		//------------ TEST SANS JDBC ---------------
		if (nom.equalsIgnoreCase("classic")) {
			//On retourne un objet canal configuré
			return new Canal("1", "classic", 10);
		}
		//-------------------------------------------
		*/
		
		//Sinon, on retourne un objet vide
		return null;
		
	}
	
	/**
	 * Créé et remplit un objet avec les infos issues de la base, à partir d'un id
	 * @param String id
	 * @return Canal
	 * @throws SQLException 
	 */
	public static Canal getById(String id) /*throws SQLException*/ {
		
		System.out.println("Canal.getById("+id+")");
		
		//On va chercher les infos depuis la base, en partant d'un id
		String requete = "SELECT * FROM canal WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		// S'il y a un resultat
		if (resultats != null && resultats.size()>0) {
			
			//On prend le 1er élément
			Dictionary dico = (Dictionary) resultats.firstElement();
			
			//On mappe les champs
			String idCanal = String.valueOf((Integer)dico.get("id"));
			String nom = (String)dico.get("nom");
			int utilMax = (int)(Integer)dico.get("utilmax");
			
			System.out.println("-------- Canal -----------");
			System.out.println("Nb de champs: "+dico.size());
			System.out.println("ID: "+idCanal);
			System.out.println("Nom: "+nom);
			System.out.println("Nb max d'auditeurs: "+utilMax);
			System.out.println("-----------------------------");
			
			//On retourne l'objet
			return new Canal(idCanal, nom, utilMax);
		}
		
		/*
		//------------ TEST SANS JDBC ---------------
		if (id.equalsIgnoreCase("1")) {
			//On retourne un objet canal configuré
			return new Canal("1", "classic", 10);
		}
		//-------------------------------------------
		*/
		
		//Sinon, on retourne un objet vide
		return null;
	}
	
	/**
	 * Retourne un vecteur d'objets canaux instanciés à partir de toutes les infos de la base 
	 * @return Hashtable
	 * @throws SQLException 
	 */
	public static Hashtable getAll() /*throws SQLException*/ {
		
		System.out.println("Canal.getAll()");
		
		//On crée un vecteur pour contenir les objets canaux instanciés
		Hashtable canaux = new Hashtable();
		
		String requete = "SELECT * FROM canal;";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		System.out.println("Canal.getAll() : "+resultats.size()+" canal(canaux) trouvé(s)");
		
		// Pour chaque canal, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id"));
			canaux.put(id, Canal.getById(id));
		}
		
		/*
		//-------- TEST SANS JDBC -------------
		canaux.put("1", Canal.getById("1"));
		//-------------------------------------
		*/
		
		//On retourne le vecteur contenant les objets canaux instanciés
		return canaux;
	}
	
	/**
	 * Détruit les infos d'un canal contenues dans la base
	 * @param String id
	 * @return boolean
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) /*throws SQLException*/ {
		
		//On supprime le canal de la base, en partant d'un id
		String requete = "DELETE FROM canal WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//On retourne le resultat de l'opération (succès/échec)
		return nbRows>0;
	}

// METHODES DYNAMIQUES (propres à un objet, inaccessibles depuis la classe)
// *************************************************
	
	/**
	 * Relance la diffusion d'un canal
	 * @param idCanal
	 */
	public void relanceDiffusionCanal(String idCanal) {
		// your code here
	}
	
	/**
	 * Vérifie si la tranche horaire est libre pour une planification
	 * @param jour
	 * @param heure
	 * @param idCanal
	 * @return
	 */
	public boolean verifierPlanification(long horaire, long duree) {
		
		//Si on a des programmes planifiés
		if (programmes.size()>0) {
			
			//On vérifie lé créneau demandé
			boolean trouveProgSuivant = false;
			Programme progPrecedent=null, progSuivant=null, prog;
			long horairePrecedent=0, horaireSuivant=0;
			Enumeration horaires = programmes.keys();
			
			while (horaires.hasMoreElements() && !trouveProgSuivant) {
				
				//On récupère l'horaire
				long horaireProg = (Long)horaires.nextElement();
				
				//On récupère le programme
				prog = (Programme)programmes.get(horaireProg);
				
				//Si l'horaire demandé est déjà occupé
				if (horaireProg == horaire) {
					return false;
				}
				
				//Si l'horaire est avant celui demandé
				else if (horaireProg < horaire) {
					progPrecedent = prog;
					horairePrecedent = horaireProg;
				}
				
				//Si l'horaire est après celui demandé
				else {
					progSuivant = prog;
					horaireSuivant = horaireProg;
					trouveProgSuivant = true;
				}
			}//Fin TQ
			
			
			//S'il n'y a pas de prog après l'horaire demandé
			if (!trouveProgSuivant) {
				return true;
			}
			
			//S'il y a un prog après
			else {

				//S'il y a un prog avant
				if (progPrecedent != null) {
					
					//On vérifie que le créneau demandé commence après le précédent et se termine avant le suivant
					return ((horaire>(progPrecedent.getDuree()+horairePrecedent)) && ((horaire+duree)<horaireSuivant));
				}
				
				//S'il n'y a pas de prog avant
				else {
					
					//On vérifie que le créneau demandé finit avant l'horaire suivant
					return (horaire+duree)<horaireSuivant;
				}
			}
		}
		
		//Sinon, c'est libre
		else {
			return true;
		}
	}
	
	/**
	 * Planifie un programme à diffuser
	 * @param Programme p
	 * @param String jour
	 * @param String mois
	 * @param String mois
	 * @param String annee
	 * @param String heure
	 * @param String minute
	 * @param String seconde
	 * @return boolean
	 */
	public boolean planifierProgramme(Programme p, int jour, int mois, int annee, int heure, int minute, int seconde) {
		
		//On assemble la date
		GregorianCalendar horaire = new GregorianCalendar(annee, mois-1, jour, heure, minute, seconde);
		
		//Si le créneau est libre
		if (verifierPlanification(horaire.getTimeInMillis(), p.getDuree())) {
			
			//On ajoute le programme au canal
			String requete = "INSERT INTO diffuser (id_prog, id_canal, calage) VALUES ('" + p.getId() + "', '" + this.id + "', '" +horaire.getTimeInMillis()+"');";
			Jdbc base = Jdbc.getInstance();
			int nbRows = base.executeUpdate(requete);
			
			//Si l'insertion s'est bien déroulée
			if (nbRows>0) {
				
				//On met à jour le vecteurs d'association
				programmes.put(horaire.getTimeInMillis(), p);
				
				return true;
			}
			
			//Sinon, problème à l'insertion
			return false;
		}
		
		//Sinon, créneau non libre
		return false;
	}
	
	/**
	 * Annuler la planification d'un programme
	 * @param long calage
	 * @return boolean
	 */
	public boolean annulerPlanification(long calage) {
		if (tasks.containsKey(calage)) {
			DiffusionTask task = (DiffusionTask)tasks.get(calage);
			task.cancel();
			tasks.remove(calage);
			programmes.remove(calage);
			return true;
		}
		return false;
	}
	
	/**
	 * Retourne la liste des programmes planifiés
	 * @return Hastable
	 */
	private Hashtable getProgrammesPlanifies() {
		System.out.println("Canal.getProgrammesPlanifies()");
		
		//On crée un vecteur pour contenir les objets documents instanciés
		Hashtable progs = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id_prog, calage FROM diffuser WHERE id_canal = '"+this.id+"';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		System.out.println("Canal.getProgrammesPlanifies() : "+resultats.size()+" programme(s) trouvé(s)");
		
		// Pour chaque programme, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id_prog"));
			long calage = (Long)dico.get("calage");
			progs.put(calage, Programme.getById(id));
		}
		
		return progs;
	}
	
	/**
	 * Modifie les attributs
	 * @param String nom
	 * @param int utilMax
	 * @return boolean
	 */
	public boolean modifier(String nom, int utilMax) {
		
		String requete = "UPDATE canal SET nom = '" + nom + "', utilMax = '" + utilMax + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.nom = nom;
			this.utilMax = utilMax;
		}
		return nbRows>0;
	}
	
	/**
	 * Retourne l'ensemble des attributs sous la forme d'un dictionnaire
	 * @return Dictionary
	 */
	public Dictionary getAttributesDictionary() {
		
		Dictionary dico = new Hashtable();
		
		dico.put("id", id);
		dico.put("nom", nom);
		dico.put("utilMax", utilMax);
		dico.put("nbProgs", programmes.size());
		
		//Liste des progs
		Vector vProgs = new Vector();
		Dictionary dicoProg;
		Programme prog;
		
		Enumeration horaires = programmes.keys();
		while (horaires.hasMoreElements()) {
			long horaire = (Long)horaires.nextElement();
			prog = (Programme)programmes.get(horaire);
			dicoProg = new Hashtable();
			dicoProg.put("calage", Long.toString(horaire));
			dicoProg.put("id", prog.getId());
			dicoProg.put("titre", prog.getTitre());
			dicoProg.put("duree", Long.toString(prog.getDuree()));
			vProgs.add(dicoProg);
		}
		
		dico.put("programmes", vProgs);
		
		return dico;
	}
	
	/**
	 * Retourne l'URL du canal
	 * @return String
	 */
	public String getUrlStreaming() {
		if (RTP!=null) {
			return RTP.getUrl();
		}
		return null;
	}
	
	/**
	 * Création du RTPServer
	 * @param ip
	 * @param port
	 */
	public void createRTPServer(String ip, int port, String publicite) {
		
		//Si le RTPServer n'existe pas, on le crée
		if (this.RTP == null) this.RTP = new RTPServer(ip, port, publicite);
	}
	
	/**
	 * Vérifie si le serveur RTP est démarré
	 * @return boolean
	 */
	public boolean isRTPstarted() {
		return RTP != null;
	}
	
	/**
	 * Lance la diffusion
	 */
	public void startDiffusion() {
		
		//Si le RTPServer existe
		if (this.RTP != null) {
			
			//On le stoppe
			this.RTP.stop();
			
			//On programme les diffusions
			Enumeration calages = programmes.keys();
			GregorianCalendar horaire = new GregorianCalendar();
			
			while (calages.hasMoreElements()) {
				Long calage = (Long)calages.nextElement();
				Programme p = (Programme)programmes.get(calage);
				horaire.setTimeInMillis(calage);
			    DiffusionTask task = new DiffusionTask(p, this);
			    timer.schedule(task, horaire.getTime());
			    tasks.put(calage, task);
			}	
		    
		    //On lance le RTPServer
		    this.RTP.diffuser();
		}
	}
	
	/**
	 * Arrête la diffusion
	 */
	public void stopDiffusion() {
		
		//Si le RTPServer existe, on le stoppe
		if (this.RTP != null) this.RTP.stop();
	}

	/**
	 * Détruit le canal et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() /*throws SQLException*/ {
		
		//On stoppe la diffusion éventuelle
		this.stopDiffusion();
		
		//On supprime les infos de la base
		return Canal.deleteById(this.id);
	}

	/**
	 * Vérifie si le canal est actif
	 * @return boolean
	 */
	public boolean estActif() {
		return etat.equalsIgnoreCase("ACTIF");
	}

	/**
	 * Retourne l'identifiant du canal
	 * @return String
	 */
	public String getId() {
		return id;
	}

	/**
	 * Retourne le nom du canal
	 * @return String
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * Retourne le nombre maximal d'auditeurs supporté par le canal
	 * @return int
	 */
	public int getUtilMax() {
		return utilMax;
	}
	
	/**
	 * Diffuser un programme
	 * @param Programme
	 */
	public void diffuserProgramme(Programme p) {        
		//p.getTitre();
		//p.diffuser();
		System.out.println("Le programme " +  p.getTitre() + " est en cours de diffusion");
		
		Vector medias = new Vector();
		Enumeration cles = p.getDocuments().keys();
		
		for(int i = 0; i < p.getDocuments().size(); i++) {
			medias.addElement(((Document)p.getDocuments().get(cles.nextElement())).getFichier());
		}
		
		if (RTP!=null) {
			RTP.programmer(medias);
			//RTP.diffuser();
		}
	} 
	

	/**
	 * Rend le canal actif
	 */
	public void setActif() {
		etat = "ACTIF";
	}

	/**
	 * Déconnecter un auditeur du canal
	 * @param idAuditeur
	 */
	public void deconnecterAuditeur(String idAuditeur) {
		if (auditeurs.contains(idAuditeur)) auditeurs.removeElement(idAuditeur);
	}
	
	/**
	 * Connecter un auditeur au canal
	 * @param idAuditeur
	 */
	public void connecterAuditeur(String idAuditeur) {
		if (!auditeurs.contains(idAuditeur)) auditeurs.addElement(idAuditeur);
	}
	
	/**
	 * Met à jour l'attribut nom
	 * @param String nom
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setNom(String nom) /*throws SQLException*/ {
		String requete = "UPDATE canal SET nom = '" + nom + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.nom = nom;
		}
		return nbRows>0;
	}
	
	/**
	 * Met à jour l'attribut utilMax
	 * @param int utilmax
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setUtilMax(int utilmax) /*throws SQLException*/ {
		String requete = "UPDATE canal SET utilmax = '" + utilmax + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.utilMax = utilmax;
		}
		return nbRows>0;
	}
}

/**
 * Thread de diffusion
 */
class DiffusionTask extends TimerTask {
	
	private Programme programme;
	private Canal canal;
	
	public DiffusionTask(Programme p, Canal c) {
		System.err.println("TIMER: Programmation de la tâche "+p.getId());
		this.programme = p;
		this.canal = c;
	}
	
	public void run() {
		System.err.println("TIMER: Lancement de la tâche "+programme.getId());
				
		canal.diffuserProgramme(programme);
		
		/*
		Vector medias = new Vector();
		Enumeration cles = programme.getDocuments().keys();
		
		for(int i = 0; i < programme.getDocuments().size(); i++) {
			medias.addElement(((Document)programme.getDocuments().get(cles.nextElement())).getFichier());
		}
		if (RTP!=null) {
			System.err.println("TIMER: On programme le RTP pour la tâche "+programme.getId());
			RTP.programmer(medias);
			RTP.diffuser();
		}
		*/
	}
}