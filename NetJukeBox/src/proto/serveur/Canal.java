package proto.serveur;

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
	private int utilmax;

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

// CONSTRUCTEUR
// *************************************************

	/**
	 * Constructeur
	 */
	public Canal() {
		this.auditeurs = new Vector();
	}
	
	/**
	 * Constructeur
	 * @param id
	 * @param nom
	 * @param fluxMax
	 */
	public Canal(String id, String nom, int utilmax) {
		this.auditeurs = new Vector();
		this.id = id;
		this.nom = nom;
		this.utilmax = utilmax;
	}

// METHODES STATIQUES
// *************************************************

// METHODES DYNAMIQUES
// *************************************************
	
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
	 * Arrête la diffusion
	 */
	public void stopDiffusion() {
		
		//Si le RTPServer existe, on le stoppe
		if (this.RTP != null) this.RTP.stop();
	}
	
	/**
	 * Création du canal ==> CONSTRUCTEUR ???
	 * @param id
	 * @param nom
	 * @param fluxMax
	 */
	public void creer(String id, String nom, int utilmax, String port) {
		this.id = id;
        this.nom = nom;
        this.utilmax = utilmax;
        this.port = port;
        System.out.println("Le Canal : " + nom + " a été créé");
	}

	/**
	 * Détruit le canal
	 * @param Id_Canal
	 */
	public void detruire(String Id_Canal) {
		// your code here
	}

	/**
	 * Vérifie si le canal est actif
	 * @return boolean
	 */
	public boolean estActif() {
		return etat == "ACTIF";
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
	public int getutilmax() {
		return utilmax;
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
	public boolean verifierPlanification(java.util.Date Jour, int heure, String idCanal) {
		// your code here
		return false;
	}

	/**
	 * Bloquer (réserver) une plage horaire pour une planification
	 * @param idProgramme
	 * @param jour
	 * @param heure
	 */
	public void bloquerPlage(String IdeProgramme, java.util.Date jour, int heure) {
		// your code here
	}
	
	/**
	Diffuser un programme
	 * @param idProgramme
	 */
	public void diffuserProgramme(Programme Prog) {        
		Prog.getTitre();
		//Prog.DiffuserProgramme();
		System.out.println("Le programme " +  Prog.getTitre() + " est en cours de diffusion");
		for(int i = 0; i < Prog.getDocuments().size(); i++) {
			String donnee = (String)Prog.getDocuments().elementAt(i);
			System.out.println("Le document : " + donnee + " a été diffusé");
		}
		//new RTPServer(Port, Prog.v);
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
