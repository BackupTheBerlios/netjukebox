package proto.serveur;

/**
 * Canal de diffusion
 */
public class Canal {

// ATTRIBUTS DU CANAL
// *************************************************

	/**
	 * Identifiant
	 */
	public String id;

	/**
	 * Nom du canal
	 */
	public String nom;

	/**
	 * Nombre maximal d'auditeurs
	 */
	public int fluxMax;

	/**
	 * Etat du canal
	 */
	public String etat;

	/**
	 * #DEFINITION#
	 */
	public Diffusion diffusion;

	/**
	 * Historisation
	 */
	public Journal_Canal journal;

// CONSTRUCTEUR
// *************************************************

	/**
	 * Constructeur
	 */
	public Canal() {

	}
	
	/**
	 * Constructeur
	 * @param id
	 * @param nom
	 * @param fluxMax
	 */
	public Canal(String id, String nom, int fluxMax) {
		super();
		
		this.id = id;
		this.nom = nom;
		this.fluxMax = fluxMax;
	}

// METHODES STATIQUES
// *************************************************

// METHODES DYNAMIQUES
// *************************************************

	/**
	 * Création du canal ==> CONSTRUCTEUR ???
	 * @param id
	 * @param nom
	 * @param fluxMax
	 */
	public void creer(String id, String nom, int fluxMax) {
		// your code here
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
	public int getFluxMax() {
		return fluxMax;
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
	public boolean verifierPlanification(java.util.Date Jour, int heure,
			String idCanal) {
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
	 * Diffuser un programme
	 * @param idProgramme
	 */
	public void diffuserProgramme(String idProgramme) {
		// your code here
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
		// your code here
	}

	/**
	 * Relance la diffusion d'un canal
	 * @param idCanal
	 */
	public void relanceDiffusionCanal(String idCanal) {
		// your code here
	}
}
