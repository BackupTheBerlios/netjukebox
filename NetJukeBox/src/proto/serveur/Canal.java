package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * Canal de diffusion
 */
public class Canal {
	/**
	 * Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(Canal.class);

// ATTRIBUTS DU CANAL
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
	 * Audimat du canal sur le média courant
	 */
	private int audimat;
	
	/**
	 * RTP Server
	 */
	private RTPServer RTP = null;
	
	/**
	 * Liste de programmes à diffuser
	 */
	//private Hashtable programmes;
	private TreeMap programmes;
	
	
	/**
	 * Timer
	 */
	private Timer timer;
	
	/**
	 * Threads de diffusion
	 */
	private Hashtable tasks;

// CONSTRUCTEUR
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
		this.timer = new Timer(true);
		this.tasks = new Hashtable();
		this.audimat = auditeurs.size();
		
		//this.programmes = getProgrammesPlanifies();
	}


// METHODES DYNAMIQUES
// *************************************************

	/**
	 * Retourne l'audimat courant
	 */
	public int getAudimat() {
		return audimat;
	}
	
	/**
	 * Met à jour l'audimat
	 */
	public void updateAudimat() {
		audimat = auditeurs.size();
	}
	
	/**
	 * Retourne la liste des auditeurs courants
	 */
	public Vector getAuditeurs() {
		return auditeurs;
	}
	
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
		logger.debug("Démarrage: verifierPlanification");
		//Si on a des programmes planifiés
		if (programmes.size()>0) {
			
			//On vérifie lé créneau demandé
			boolean trouveProgSuivant = false;
			Programme progPrecedent=null, progSuivant=null, prog;
			long horairePrecedent=0, horaireSuivant=0;
			Iterator horaires = programmes.keySet().iterator();
			
			while (horaires.hasNext() && !trouveProgSuivant) {
				
				//On récupère l'horaire
				long horaireProg = (Long)horaires.next();
				
				//On récupère le programme
				prog = (Programme)programmes.get(horaireProg);
				
				//Si l'horaire demandé est déjà occupé
				if (horaireProg == horaire) {
					logger.debug("Arrêt: verifierPlanification");
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
				logger.debug("Arrêt: verifierPlanification");
				return true;
			}
			
			//S'il y a un prog après
			else {

				//S'il y a un prog avant
				if (progPrecedent != null) {
					
					//On vérifie que le créneau demandé commence après le précédent et se termine avant le suivant
					logger.debug("Arrêt: verifierPlanification");
					return ((horaire>(progPrecedent.getDuree()+horairePrecedent)) && ((horaire+duree)<horaireSuivant));
				}
				
				//S'il n'y a pas de prog avant
				else {
					
					//On vérifie que le créneau demandé finit avant l'horaire suivant
					logger.debug("Arrêt: verifierPlanification");
					return (horaire+duree)<horaireSuivant;
				}
			}
		}
		
		//Sinon, c'est libre
		else {
			logger.debug("Arrêt: verifierPlanification");
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
		logger.debug("Démarrage: planifierProgramme");
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
				
				//On planifie la tâche de diffusion
			    DiffusionTask task = new DiffusionTask(p, this);
			    timer.schedule(task, horaire.getTime());
			    tasks.put(horaire.getTime(), task);
			    
				logger.debug("Arrêt: planifierProgramme");
				return true;
			}
			
			//Sinon, problème à l'insertion
			logger.debug("Arrêt: planifierProgramme");
			return false;
		}
		
		//Sinon, créneau non libre
		logger.debug("Arrêt: planifierProgramme");
		return false;
	}
	
	/**
	 * Annuler la planification d'un programme
	 * @param long calage
	 * @return boolean
	 */
	public boolean annulerPlanification(long calage) {
		logger.debug("Démarrage: annulerPlanification");
		if (programmes.containsKey(calage)) {
			
			//On retire le programme du canal
			String requete = "DELETE FROM diffuser WHERE id_canal='"+id+"' AND calage='"+calage+"';";
			Jdbc base = Jdbc.getInstance();
			int nbRows = base.executeUpdate(requete);
			
			//Si la suppression s'est bien déroulée
			if (nbRows>0) {
				
				//Si une tâche est programmée
				if (tasks.containsKey(calage)) {
					//On annule la tâche
					DiffusionTask task = (DiffusionTask)tasks.get(calage);
					task.cancel();
					tasks.remove(calage);
				}
				
				//On dissocie le programme
				programmes.remove(calage);
				logger.debug("Arrêt: annulerPlanification");
				return true;
			}
			//Sinon
			logger.debug("Arrêt: annulerPlanification");
			return false;
		}
		logger.debug("Arrêt: annulerPlanification");
		return false;
	}
	
	/**
	 * Définit la liste des programmes planifiés
	 */
	public void setProgrammesPlanifies() {
		logger.debug("Démarrage: setProgrammesPlanifies");
		
		//On crée un vecteur pour contenir les objets documents instanciés
		//Hashtable progs = new Hashtable();
		TreeMap progs = new TreeMap();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id_prog, calage FROM diffuser WHERE id_canal = '"+this.id+"';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		logger.info("Canal.getProgrammesPlanifies() : "+resultats.size()+" programme(s) trouvé(s)");
		
		// Pour chaque programme, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id_prog"));
			long calage = (Long)dico.get("calage");
			progs.put(calage, ProgrammeFactory.getById(id));
		}
		
		programmes = progs;
		logger.debug("Arrêt: setProgrammesPlanifies");
	}
	
	/**
	 * Modifie les attributs
	 * @param String nom
	 * @param int utilMax
	 * @return boolean
	 */
	public boolean modifier(String nom, int utilMax) {
		logger.debug("Démarrage: modifier");
		String requete = "UPDATE canal SET nom = '" + nom + "', utilMax = '" + utilMax + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.nom = nom;
			this.utilMax = utilMax;
		}
		logger.debug("Arrêt: modifier");
		return nbRows>0;
	}
	
	/**
	 * Retourne l'ensemble des attributs sous la forme d'un dictionnaire
	 * @return Dictionary
	 */
	public /*pure*/ Dictionary getAttributesDictionary() {
		
		Dictionary dico = new Hashtable();
		
		dico.put("id", id);
		dico.put("nom", nom);
		dico.put("utilMax", utilMax);
		dico.put("nbProgs", programmes.size());
		
		//Liste des progs
		Vector vProgs = new Vector();
		Dictionary dicoProg;
		Programme prog;
		
		Iterator horaires = programmes.keySet().iterator();
		while (horaires.hasNext()) {
			long horaire = (Long)horaires.next();
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
	public /*pure*/ String getUrlStreaming() {
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
		logger.debug("Démarrage: createRTPServer");
		//Si le RTPServer n'existe pas, on le crée
		if (this.RTP == null) this.RTP = new RTPServer(ip, port, publicite, this);
		logger.debug("Arrêt: createRTPServer");
	}
	
	/**
	 * Vérifie si le serveur RTP est démarré
	 * @return boolean
	 */
	public /*pure*/ boolean isRTPstarted() {
		return RTP != null;
	}
	
	/**
	 * Lance la diffusion
	 */
	public void startDiffusion() {
		logger.debug("Démarrage: startDiffusion");
		//Si le RTPServer existe
		if (this.RTP != null) {
			
			//On le stoppe
			this.RTP.stop();
			
			//On programme les diffusions
			Iterator calages = programmes.keySet().iterator();
			GregorianCalendar horaire = new GregorianCalendar();

			while (calages.hasNext()) {
				Long calage = (Long)calages.next();
				Programme p = (Programme)programmes.get(calage);
				horaire.setTimeInMillis(calage);
			    DiffusionTask task = new DiffusionTask(p, this);
			    timer.schedule(task, horaire.getTime());
			    tasks.put(calage, task);
			}	
		    
		    //On lance le RTPServer
		    this.RTP.diffuser();
		    logger.debug("Arrêt: startDiffusion");
		}
	}
	
	/**
	 * Arrête la diffusion
	 */
	public void stopDiffusion() {
		logger.debug("Démarrage: stopDiffusion");
		//Si le RTPServer existe, on le stoppe
		if (this.RTP != null) this.RTP.stop();
		logger.debug("Arrêt: stopDiffusion");
	}

	/**
	 * Détruit le canal et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() /*throws SQLException*/ {
		logger.debug("Démarrage: supprimer");
		//On stoppe la diffusion éventuelle
		this.stopDiffusion();
		
		//On supprime les infos de la base
		logger.debug("Arrêt: supprimer");
		return CanalFactory.deleteById(this.id);
	}

	/**
	 * Vérifie si le canal est actif
	 * @return boolean
	 */
	public /*pure*/ boolean estActif() {
		return etat.equalsIgnoreCase("ACTIF");
	}

	/**
	 * Retourne l'identifiant du canal
	 * @return String
	 */
	public /*pure*/ String getId() {
		return id;
	}

	/**
	 * Retourne le nom du canal
	 * @return String
	 */
	public /*pure*/ String getNom() {
		return nom;
	}

	/**
	 * Retourne le nombre maximal d'auditeurs supporté par le canal
	 * @return int
	 */
	public /*pure*/ int getUtilMax() {
		return utilMax;
	}
	
	/**
	 * Diffuser un programme
	 * @param Programme
	 */
	public void diffuserProgramme(Programme p) {    
		logger.debug("Démarrage: diffuserProgramme");
		//p.getTitre();
		//p.diffuser();
		logger.info("Le programme " +  p.getTitre() + " est en cours de diffusion");
		
		Vector medias = new Vector();
		TreeMap documents = p.getDocuments();
		Iterator calages = documents.keySet().iterator();
		
		
		while (calages.hasNext()) {
			medias.addElement(documents.get(calages.next()));
		}
		
		if (RTP!=null) {
			RTP.programmer(medias);
			//RTP.diffuser();
		}
		logger.debug("Arrêt: diffuserProgramme");
	} 
	

	/**
	 * Rend le canal actif
	 */
	public void setActif() {
		logger.debug("Démarrage: setActif");
		etat = "ACTIF";
		logger.debug("Arrêt: setActif");
	}

	/**
	 * Déconnecter un auditeur du canal
	 * @param idAuditeur
	 */
	public void deconnecterAuditeur(String idAuditeur) {
		logger.debug("Démarrage: deconnecterAuditeur");
		if (auditeurs.contains(idAuditeur)) auditeurs.removeElement(idAuditeur);
		logger.debug("Arrêt: deconnecterAuditeur");
	}
	
	/**
	 * Connecter un auditeur au canal
	 * @param idAuditeur
	 */
	public void connecterAuditeur(String idAuditeur) {
		logger.debug("Démarrage: connecterAuditeur");
		if (!auditeurs.contains(idAuditeur)) {
			auditeurs.addElement(idAuditeur);
			audimat++;
		}
		logger.debug("Arrêt: connecterAuditeur");
	}
	
	/**
	 * Met à jour l'attribut nom
	 * @param String nom
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setNom(String nom) /*throws SQLException*/ {
		logger.debug("Démarrage: setNom");
		String requete = "UPDATE canal SET nom = '" + nom.replace("'", "''") + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.nom = nom;
		}
		logger.debug("Arrêt: setNom");
		return nbRows>0;
	}
	
	/**
	 * Met à jour l'attribut utilMax
	 * @param int utilmax
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setUtilMax(int utilmax) /*throws SQLException*/ {
		logger.debug("Démarrage: setUtilMax");
		String requete = "UPDATE canal SET utilmax = '" + utilmax + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.utilMax = utilmax;
		}
		logger.debug("Arrêt: setUtilMax");
		return nbRows>0;
	}
}

/**
 * Thread de diffusion
 */
class DiffusionTask extends TimerTask {
	/**
	 * Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(DiffusionTask.class);
	
	private Programme programme;
	private Canal canal;
	
	public DiffusionTask(Programme p, Canal c) {
		logger.debug("Démarrage: DiffusionTask");
		logger.error("TIMER: Programmation de la tâche "+p.getId());
		this.programme = p;
		this.canal = c;
		logger.debug("Arrêt: DiffusionTask");
	}
	
	public void run() {
		logger.debug("Démarrage: run");
		logger.error("TIMER: Lancement de la tâche "+programme.getId());				
		canal.diffuserProgramme(programme);
		
		logger.debug("Démarrage: run");
	}
}