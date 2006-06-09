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
	 * Cr�e le logger de la classe
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
	 * Audimat du canal sur le m�dia courant
	 */
	private int audimat;
	
	/**
	 * RTP Server
	 */
	private RTPServer RTP = null;
	
	/**
	 * Liste de programmes � diffuser
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
	 * Met � jour l'audimat
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
	 * V�rifie si la tranche horaire est libre pour une planification
	 * @param jour
	 * @param heure
	 * @param idCanal
	 * @return
	 */
	public boolean verifierPlanification(long horaire, long duree) {
		logger.debug("D�marrage: verifierPlanification");
		//Si on a des programmes planifi�s
		if (programmes.size()>0) {
			
			//On v�rifie l� cr�neau demand�
			boolean trouveProgSuivant = false;
			Programme progPrecedent=null, progSuivant=null, prog;
			long horairePrecedent=0, horaireSuivant=0;
			Iterator horaires = programmes.keySet().iterator();
			
			while (horaires.hasNext() && !trouveProgSuivant) {
				
				//On r�cup�re l'horaire
				long horaireProg = (Long)horaires.next();
				
				//On r�cup�re le programme
				prog = (Programme)programmes.get(horaireProg);
				
				//Si l'horaire demand� est d�j� occup�
				if (horaireProg == horaire) {
					logger.debug("Arr�t: verifierPlanification");
					return false;
				}
				
				//Si l'horaire est avant celui demand�
				else if (horaireProg < horaire) {
					progPrecedent = prog;
					horairePrecedent = horaireProg;
				}
				
				//Si l'horaire est apr�s celui demand�
				else {
					progSuivant = prog;
					horaireSuivant = horaireProg;
					trouveProgSuivant = true;
				}
			}//Fin TQ
			
			
			//S'il n'y a pas de prog apr�s l'horaire demand�
			if (!trouveProgSuivant) {
				logger.debug("Arr�t: verifierPlanification");
				return true;
			}
			
			//S'il y a un prog apr�s
			else {

				//S'il y a un prog avant
				if (progPrecedent != null) {
					
					//On v�rifie que le cr�neau demand� commence apr�s le pr�c�dent et se termine avant le suivant
					logger.debug("Arr�t: verifierPlanification");
					return ((horaire>(progPrecedent.getDuree()+horairePrecedent)) && ((horaire+duree)<horaireSuivant));
				}
				
				//S'il n'y a pas de prog avant
				else {
					
					//On v�rifie que le cr�neau demand� finit avant l'horaire suivant
					logger.debug("Arr�t: verifierPlanification");
					return (horaire+duree)<horaireSuivant;
				}
			}
		}
		
		//Sinon, c'est libre
		else {
			logger.debug("Arr�t: verifierPlanification");
			return true;
		}
	}
	
	/**
	 * Planifie un programme � diffuser
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
		logger.debug("D�marrage: planifierProgramme");
		//On assemble la date
		GregorianCalendar horaire = new GregorianCalendar(annee, mois-1, jour, heure, minute, seconde);
		
		//Si le cr�neau est libre
		if (verifierPlanification(horaire.getTimeInMillis(), p.getDuree())) {
			
			//On ajoute le programme au canal
			String requete = "INSERT INTO diffuser (id_prog, id_canal, calage) VALUES ('" + p.getId() + "', '" + this.id + "', '" +horaire.getTimeInMillis()+"');";
			Jdbc base = Jdbc.getInstance();
			int nbRows = base.executeUpdate(requete);
			
			//Si l'insertion s'est bien d�roul�e
			if (nbRows>0) {
				
				//On met � jour le vecteurs d'association
				programmes.put(horaire.getTimeInMillis(), p);
				
				//On planifie la t�che de diffusion
			    DiffusionTask task = new DiffusionTask(p, this);
			    timer.schedule(task, horaire.getTime());
			    tasks.put(horaire.getTime(), task);
			    
				logger.debug("Arr�t: planifierProgramme");
				return true;
			}
			
			//Sinon, probl�me � l'insertion
			logger.debug("Arr�t: planifierProgramme");
			return false;
		}
		
		//Sinon, cr�neau non libre
		logger.debug("Arr�t: planifierProgramme");
		return false;
	}
	
	/**
	 * Annuler la planification d'un programme
	 * @param long calage
	 * @return boolean
	 */
	public boolean annulerPlanification(long calage) {
		logger.debug("D�marrage: annulerPlanification");
		if (programmes.containsKey(calage)) {
			
			//On retire le programme du canal
			String requete = "DELETE FROM diffuser WHERE id_canal='"+id+"' AND calage='"+calage+"';";
			Jdbc base = Jdbc.getInstance();
			int nbRows = base.executeUpdate(requete);
			
			//Si la suppression s'est bien d�roul�e
			if (nbRows>0) {
				
				//Si une t�che est programm�e
				if (tasks.containsKey(calage)) {
					//On annule la t�che
					DiffusionTask task = (DiffusionTask)tasks.get(calage);
					task.cancel();
					tasks.remove(calage);
				}
				
				//On dissocie le programme
				programmes.remove(calage);
				logger.debug("Arr�t: annulerPlanification");
				return true;
			}
			//Sinon
			logger.debug("Arr�t: annulerPlanification");
			return false;
		}
		logger.debug("Arr�t: annulerPlanification");
		return false;
	}
	
	/**
	 * D�finit la liste des programmes planifi�s
	 */
	public void setProgrammesPlanifies() {
		logger.debug("D�marrage: setProgrammesPlanifies");
		
		//On cr�e un vecteur pour contenir les objets documents instanci�s
		//Hashtable progs = new Hashtable();
		TreeMap progs = new TreeMap();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id_prog, calage FROM diffuser WHERE id_canal = '"+this.id+"';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		logger.info("Canal.getProgrammesPlanifies() : "+resultats.size()+" programme(s) trouv�(s)");
		
		// Pour chaque programme, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id_prog"));
			long calage = (Long)dico.get("calage");
			progs.put(calage, ProgrammeFactory.getById(id));
		}
		
		programmes = progs;
		logger.debug("Arr�t: setProgrammesPlanifies");
	}
	
	/**
	 * Modifie les attributs
	 * @param String nom
	 * @param int utilMax
	 * @return boolean
	 */
	public boolean modifier(String nom, int utilMax) {
		logger.debug("D�marrage: modifier");
		String requete = "UPDATE canal SET nom = '" + nom + "', utilMax = '" + utilMax + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.nom = nom;
			this.utilMax = utilMax;
		}
		logger.debug("Arr�t: modifier");
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
	 * Cr�ation du RTPServer
	 * @param ip
	 * @param port
	 */
	public void createRTPServer(String ip, int port, String publicite) {
		logger.debug("D�marrage: createRTPServer");
		//Si le RTPServer n'existe pas, on le cr�e
		if (this.RTP == null) this.RTP = new RTPServer(ip, port, publicite, this);
		logger.debug("Arr�t: createRTPServer");
	}
	
	/**
	 * V�rifie si le serveur RTP est d�marr�
	 * @return boolean
	 */
	public /*pure*/ boolean isRTPstarted() {
		return RTP != null;
	}
	
	/**
	 * Lance la diffusion
	 */
	public void startDiffusion() {
		logger.debug("D�marrage: startDiffusion");
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
		    logger.debug("Arr�t: startDiffusion");
		}
	}
	
	/**
	 * Arr�te la diffusion
	 */
	public void stopDiffusion() {
		logger.debug("D�marrage: stopDiffusion");
		//Si le RTPServer existe, on le stoppe
		if (this.RTP != null) this.RTP.stop();
		logger.debug("Arr�t: stopDiffusion");
	}

	/**
	 * D�truit le canal et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() /*throws SQLException*/ {
		logger.debug("D�marrage: supprimer");
		//On stoppe la diffusion �ventuelle
		this.stopDiffusion();
		
		//On supprime les infos de la base
		logger.debug("Arr�t: supprimer");
		return CanalFactory.deleteById(this.id);
	}

	/**
	 * V�rifie si le canal est actif
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
	 * Retourne le nombre maximal d'auditeurs support� par le canal
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
		logger.debug("D�marrage: diffuserProgramme");
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
		logger.debug("Arr�t: diffuserProgramme");
	} 
	

	/**
	 * Rend le canal actif
	 */
	public void setActif() {
		logger.debug("D�marrage: setActif");
		etat = "ACTIF";
		logger.debug("Arr�t: setActif");
	}

	/**
	 * D�connecter un auditeur du canal
	 * @param idAuditeur
	 */
	public void deconnecterAuditeur(String idAuditeur) {
		logger.debug("D�marrage: deconnecterAuditeur");
		if (auditeurs.contains(idAuditeur)) auditeurs.removeElement(idAuditeur);
		logger.debug("Arr�t: deconnecterAuditeur");
	}
	
	/**
	 * Connecter un auditeur au canal
	 * @param idAuditeur
	 */
	public void connecterAuditeur(String idAuditeur) {
		logger.debug("D�marrage: connecterAuditeur");
		if (!auditeurs.contains(idAuditeur)) {
			auditeurs.addElement(idAuditeur);
			audimat++;
		}
		logger.debug("Arr�t: connecterAuditeur");
	}
	
	/**
	 * Met � jour l'attribut nom
	 * @param String nom
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setNom(String nom) /*throws SQLException*/ {
		logger.debug("D�marrage: setNom");
		String requete = "UPDATE canal SET nom = '" + nom.replace("'", "''") + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.nom = nom;
		}
		logger.debug("Arr�t: setNom");
		return nbRows>0;
	}
	
	/**
	 * Met � jour l'attribut utilMax
	 * @param int utilmax
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setUtilMax(int utilmax) /*throws SQLException*/ {
		logger.debug("D�marrage: setUtilMax");
		String requete = "UPDATE canal SET utilmax = '" + utilmax + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.utilMax = utilmax;
		}
		logger.debug("Arr�t: setUtilMax");
		return nbRows>0;
	}
}

/**
 * Thread de diffusion
 */
class DiffusionTask extends TimerTask {
	/**
	 * Cr�e le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(DiffusionTask.class);
	
	private Programme programme;
	private Canal canal;
	
	public DiffusionTask(Programme p, Canal c) {
		logger.debug("D�marrage: DiffusionTask");
		logger.error("TIMER: Programmation de la t�che "+p.getId());
		this.programme = p;
		this.canal = c;
		logger.debug("Arr�t: DiffusionTask");
	}
	
	public void run() {
		logger.debug("D�marrage: run");
		logger.error("TIMER: Lancement de la t�che "+programme.getId());				
		canal.diffuserProgramme(programme);
		
		logger.debug("D�marrage: run");
	}
}