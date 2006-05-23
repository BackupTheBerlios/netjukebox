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
	 * RTP Server
	 */
	private RTPServer RTP = null;
	
	/**
	 * Liste de programmes � diffuser
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
		
		//this.programmes = getProgrammesPlanifies();
	}

// METHODES DYNAMIQUES
// *************************************************
	
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
		
		//Si on a des programmes planifi�s
		if (programmes.size()>0) {
			
			//On v�rifie l� cr�neau demand�
			boolean trouveProgSuivant = false;
			Programme progPrecedent=null, progSuivant=null, prog;
			long horairePrecedent=0, horaireSuivant=0;
			Enumeration horaires = programmes.keys();
			
			while (horaires.hasMoreElements() && !trouveProgSuivant) {
				
				//On r�cup�re l'horaire
				long horaireProg = (Long)horaires.nextElement();
				
				//On r�cup�re le programme
				prog = (Programme)programmes.get(horaireProg);
				
				//Si l'horaire demand� est d�j� occup�
				if (horaireProg == horaire) {
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
				return true;
			}
			
			//S'il y a un prog apr�s
			else {

				//S'il y a un prog avant
				if (progPrecedent != null) {
					
					//On v�rifie que le cr�neau demand� commence apr�s le pr�c�dent et se termine avant le suivant
					return ((horaire>(progPrecedent.getDuree()+horairePrecedent)) && ((horaire+duree)<horaireSuivant));
				}
				
				//S'il n'y a pas de prog avant
				else {
					
					//On v�rifie que le cr�neau demand� finit avant l'horaire suivant
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
				
				return true;
			}
			
			//Sinon, probl�me � l'insertion
			return false;
		}
		
		//Sinon, cr�neau non libre
		return false;
	}
	
	/**
	 * Annuler la planification d'un programme
	 * @param long calage
	 * @return boolean
	 */
	public boolean annulerPlanification(long calage) {
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
				return true;
			}
			//Sinon
			return false;
		}
		return false;
	}
	
	/**
	 * D�finit la liste des programmes planifi�s
	 */
	public void setProgrammesPlanifies() {
		System.out.println("Canal.getProgrammesPlanifies()");
		
		//On cr�e un vecteur pour contenir les objets documents instanci�s
		Hashtable progs = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id_prog, calage FROM diffuser WHERE id_canal = '"+this.id+"';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		System.out.println("Canal.getProgrammesPlanifies() : "+resultats.size()+" programme(s) trouv�(s)");
		
		// Pour chaque programme, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id_prog"));
			long calage = (Long)dico.get("calage");
			progs.put(calage, ProgrammeFactory.getById(id));
		}
		
		programmes = progs;
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
		
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
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
	 * Cr�ation du RTPServer
	 * @param ip
	 * @param port
	 */
	public void createRTPServer(String ip, int port, String publicite) {
		
		//Si le RTPServer n'existe pas, on le cr�e
		if (this.RTP == null) this.RTP = new RTPServer(ip, port, publicite);
	}
	
	/**
	 * V�rifie si le serveur RTP est d�marr�
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
	 * Arr�te la diffusion
	 */
	public void stopDiffusion() {
		
		//Si le RTPServer existe, on le stoppe
		if (this.RTP != null) this.RTP.stop();
	}

	/**
	 * D�truit le canal et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() /*throws SQLException*/ {
		
		//On stoppe la diffusion �ventuelle
		this.stopDiffusion();
		
		//On supprime les infos de la base
		return CanalFactory.deleteById(this.id);
	}

	/**
	 * V�rifie si le canal est actif
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
	 * Retourne le nombre maximal d'auditeurs support� par le canal
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
	 * D�connecter un auditeur du canal
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
	 * Met � jour l'attribut nom
	 * @param String nom
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setNom(String nom) /*throws SQLException*/ {
		String requete = "UPDATE canal SET nom = '" + nom + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.nom = nom;
		}
		return nbRows>0;
	}
	
	/**
	 * Met � jour l'attribut utilMax
	 * @param int utilmax
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setUtilMax(int utilmax) /*throws SQLException*/ {
		String requete = "UPDATE canal SET utilmax = '" + utilmax + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
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
		System.err.println("TIMER: Programmation de la t�che "+p.getId());
		this.programme = p;
		this.canal = c;
	}
	
	public void run() {
		System.err.println("TIMER: Lancement de la t�che "+programme.getId());
				
		canal.diffuserProgramme(programme);
		
		/*
		Vector medias = new Vector();
		Enumeration cles = programme.getDocuments().keys();
		
		for(int i = 0; i < programme.getDocuments().size(); i++) {
			medias.addElement(((Document)programme.getDocuments().get(cles.nextElement())).getFichier());
		}
		if (RTP!=null) {
			System.err.println("TIMER: On programme le RTP pour la t�che "+programme.getId());
			RTP.programmer(medias);
			RTP.diffuser();
		}
		*/
	}
}