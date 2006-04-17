package proto.serveur;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
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
	private Journal_Canal journal;
	
	
	/**
	 * Auditeurs du canal
	 */
	private Vector auditeurs;
	
	/**
	 * RTP Server
	 */
	private RTPServer RTP = null;

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
	}

// METHODES STATIQUES (propres à la classe, inaccessibles depuis un objet)
// *************************************************
	
	/**
	 * Création du canal en base
	 * @param String nom
	 * @param int utilmax
	 * @return Canal
	 */
	public static Canal create(String nom, int utilmax) {
		
		//************
		// => JDBC <=
		//************
		
		System.out.println("Le Canal : " + nom + " a été créé");
		
		//On retourne un objet avec les infos complètes issues de la base
		return Canal.getByNom(nom);
	}
	
	/**
	 * Créé et remplit un objet avec les infos issues de la base, à partir d'un nom
	 * @param String nom
	 * @return Canal
	 */
	public static Canal getByNom(String nom) {
		
		//On va chercher les infos depuis la base, en partant d'un nom
		
		//************
		// => JDBC <=
		//************
		
		if (nom.equalsIgnoreCase("classic")) {
			//On retourne un objet canal configuré
			return new Canal("1", "classic", 10);
		}
		
		//Sinon, on retourne un objet vide
		return null;
		
	}
	
	/**
	 * Créé et remplit un objet avec les infos issues de la base, à partir d'un id
	 * @param String id
	 * @return Canal
	 */
	public static Canal getById(String id) {
		
		//On va chercher les infos depuis la base, en partant d'un id
		
		//************
		// => JDBC <=
		//************
		
		if (id.equalsIgnoreCase("1")) {
			//On retourne un objet canal configuré
			return new Canal("1", "classic", 10);
		}
		
		//Sinon, on retourne un objet vide
		return null;
	}
	
	/**
	 * Retourne un vecteur d'objets canaux instanciés à partir de toutes les infos de la base 
	 * @return Hashtable
	 */
	public static Hashtable getAll() {
		
		//On crée un vecteur pour contenir les objets canaux instanciés
		Hashtable canaux = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les canaux
		//************
		// => JDBC <=
		//************
		
		//Pour chaque canal, on instancie un objet que l'on stocke dans le vecteur
		canaux.put("1", Canal.getById("1"));
		
		//On retourne le vecteur contenant les objets canaux instanciés
		return canaux;
	}
	
	/**
	 * Détruit les infos d'un canal contenues dans la base
	 * @param id
	 * @return
	 */
	public static boolean deleteById(String id) {
		
		//On supprime le canal de la base, en partant d'un id
		
		//************
		// => JDBC <=
		//************
		
		//On retourne le resultat de l'opération (succès/échec)
		return true;
	}

// METHODES DYNAMIQUES (propres à un objet, inaccessibles depuis la classe)
// *************************************************
	
	/**
	 * Retourne l'URL du canal
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
	public void createRTPServer(String ip, int port) {
		
		//Si le RTPServer n'existe pas, on le crée
		if (this.RTP == null) this.RTP = new RTPServer(ip, port);
	}
	
	/**
	 * Lance la diffusion
	 */
	public void startDiffusion() {
		
		//Si le RTPServer existe, on le stoppe
		if (this.RTP != null) this.RTP.diffuser();
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
	 */
	public boolean supprimer() {
		
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
	 * Insertion d'informations
	 * @param id
	 * @param nom
	 * @param fluxMax
	 */
	public void insertionInfos(String id, String nom, int fluxMax) {
		// your code here
	}

	/**
	 * Vérifie si la tranche horaire est libre pour une planification
	 * @param jour
	 * @param heure
	 * @param idCanal
	 * @return
	 */
	public boolean verifierPlanification(Date Jour, int heure, String idCanal) {
		// your code here
		return false;
	}

	/**
	 * Bloquer (réserver) une plage horaire pour une planification
	 * @param idProgramme
	 * @param jour
	 * @param heure
	 */
	public void bloquerPlage(String IdeProgramme, Date jour, int heure) {
		// your code here
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
			RTP.diffuser();
		}
	} 
	

	/**
	 * Rend le canal actif
	 */
	public void setActif() {
		etat = "ACTIF";
	}

	/**
	 * Arrête la diffusion du canal
	 * @param idCanal
	 */
	public void arreterDiffusionCanal(int idCanal) {
		// your code here
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
	 * Relance la diffusion d'un canal
	 * @param idCanal
	 */
	public void relanceDiffusionCanal(String idCanal) {
		// your code here
	}
}
