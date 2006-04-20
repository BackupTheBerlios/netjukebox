package proto.serveur;


import java.sql.SQLException;
import java.util.Hashtable;
import java.util.prefs.Preferences;

/**
 * Classe contenant la logique principale du serveur principal
 */
public class Systeme {

// ATTRIBUTS DU SYSTEME
//************************************************************
	
	/**
	 * Adresse IP de diffusion (streaming) :  broadcast
	 */
	private String ipStreaming;
	
	/**
	 * Port de diffusion (streaming)
	 */
	private String portStreaming;
	
	/**
	 * Port du serveur XML
	 */
	private int portXML;
	
	
	/**
	 * Utilisateurs connect�s aux syst�me
	 */
	private Hashtable utilisateurs = new Hashtable();
	
	
	/**
	 * Canaux de diffusion
	 */
	private Hashtable canaux = new Hashtable();
	
	/**
	 * Documents
	 */
	private Hashtable documents = new Hashtable();
	
	/**
	 * Programmes
	 */
	private Hashtable programmes = new Hashtable();
	
	/**
	 * Connexion � la base de donn�es
	 */
	private Jdbc base;
	
	/**
	 * Pr�f�rences
	 */
	private Preferences prefs;

// CONSTRUCTEUR
//************************************************************
	
	/**
	 * Constructeur
	 * @throws SQLException 
	 */
	public Systeme(Preferences prefs) throws SQLException {
		
		this.prefs = prefs;
		this.ipStreaming = prefs.node("streaming").get("ip", null);
		this.portStreaming = prefs.node("streaming").get("port", null);
		
		//On initialise la connection � la base
		String driver = prefs.node("jdbc").get("driver", null);
		String type = prefs.node("jdbc").get("type", null);
		String ip = prefs.node("jdbc").get("ip", null);
		String port = prefs.node("jdbc").get("port", null);
		String baseName = prefs.node("jdbc").get("base", null);
		String login = prefs.node("jdbc").get("login", null);
		String pwd = prefs.node("jdbc").get("pwd", null);
		
		this.base = Jdbc.getInstance();
		base.openDB(driver, "jdbc:"+type+"://"+ip+":"+port+"/"+baseName, login, pwd);
		
		//On initialise les listes de canaux, programmes, documents
		this.canaux = Canal.getAll();
		this.programmes = Programme.getAll();
		this.documents = Document.getAll();
	}
	
// METHODES DU SYSTEME
//************************************************************
	
	//XMLRPC
	//*******************************************************
	
	/**
	 * Signale au client que le seveur est bien connect�
	 * @param String ip
	 * @return String
	 */
	public String testConnectXML(String ip) {
		System.out.println("Test de connexion envoy� par client XML "+ip);
		return Boolean.toString(true);
	}
	
	// UTILISATEUR
	//*******************************************************
	
	/**
	 * Connexion d'un utilisateur
	 * @param login
	 * @param pwd
	 * @return String
	 */
	public String connexion(String login, String pwd) {
		
		System.out.println("Connexion de l'utilisateur "+login);
		
		//On v�rifie que le couple login/pwd existe
		if (Utilisateur.verifierPwd(pwd, login)) {
			
			//On r�cup�re l'utilisateur depuis la base
			Utilisateur util = Utilisateur.getByLogin(login);
			
			//On connecte l'utilisateur
			util.connexion();
			
			//On l'ajoute � la liste des utilisateurs connect�s au syst�me
			utilisateurs.put(login, util);
			
			System.out.println("Utilisateur "+login+" connect�");
			return Boolean.toString(true);
			
		}
		
		//Sinon, connexion refus�e
		System.out.println("Utilisateur "+login+" non connect�");
		return Boolean.toString(false);
	}
	/**
	 * Deconnexion d'un utilisateur
	 * @param login
	 * @return String
	 */
	public String deconnexion(String login) {
		
		System.out.println("Deconnexion de l'utilisateur "+login);
		
		//S'il y a des utilisateurs connect�s
		if (utilisateurs.size()>0) {
			
			//On recherche l'utilisateur en question parmi cette liste
			if (utilisateurs.containsKey(login)) {
				
				//On r�cup�re l'objet utilisateur
				Utilisateur util = (Utilisateur)utilisateurs.get(login);
				
				//On le d�connecte
				util.deconnexion();
				
				//On le supprime de la liste des utilisateurs connect�s au syst�me
				utilisateurs.remove(login);
				
				System.out.println("Utilisateur "+login+" deconnect�");
				return Boolean.toString(true);
			}
			System.out.println("Utilisateur "+login+" introuvable");
			return Boolean.toString(false);
		}
		System.out.println("Aucun utilisateur n'est connect�");
		return Boolean.toString(false);
	}

	/**
	 * Recherche un utilisateur � partir de son login
	 * @param login
	 * @return Utilisateur
	 */
	public Utilisateur chercherUtilisateur(String login) {
		
		//On v�rifie que le couple login existe
		if (Utilisateur.verifierLogin(login)) return Utilisateur.getByLogin(login);

		return null;
	}

	
	public void inscription() {
		// your code here
	}

	public void suppressionUtilisateur() {
		// your code here
	}

	public void rechercheUtilisateur() {
		// your code here
	}

	public void rechercherUtilisateur(String Login) {
		// your code here
	}

	public void selectionnerUtilisateur(String Login) {
		// your code here
	}

	public void supprimerUtilisateur(String Login) {
		// your code here
	}
	
	public boolean emailValide(String Email) {
		// your code here
		return false;
	}

	public void modifierUtilisateur() {
		// your code here
	}

	public void modifierUtil(String Login) {
		// your code here
	}

	public void getListePermissions() {
		// your code here
	}

	// DOCUMENT
	//*******************************************************
	
	/**
	 * Cr�ation d'un document
	 * @param String titre
	 * @param String duree
	 * @param String jour
	 * @param String mois
	 * @param String annee
	 * @param String source
	 * @param String langue
	 * @param String genre
	 * @param String fichier
	 * @return String
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	public String creerDocument(String titre, String duree, String jour, String mois, String annee, String source, String langue, String genre, String fichier) throws NumberFormatException, SQLException {

		System.out.println("Cr�ation du document "+titre);
		
		//On v�rifie que le document n'existe pas
		if (Document.getByTitre(titre) == null) {
			
			//On cr�e le document
			Document d = Document.create(titre, Integer.parseInt(duree), jour, mois, annee, source, langue, genre, fichier);
			
			//Si le document a bien �t� cr��
			if (d != null) {
				//On l'ajoute � la liste des documents du syst�me
				documents.put(d.getId(), d);
				
				System.out.println("Document '"+titre+"' cr��");
				return Boolean.toString(true);
			}
			
			//Sinon, cr�ation a �chou�
			System.out.println("Document '"+titre+"' non cr��");
			return Boolean.toString(false);
		}
		
		//Sinon, cr�ation refus�e
		System.out.println("Document '"+titre+"' non cr��");
		return Boolean.toString(false);
	}
	
	public void supprimerDocument() {
		// your code here
	}

	public void ajouterDocument() {
		// your code here
	}

	public boolean documentExistant() {
		// your code here
		return false;
	}

	public void supprimerDoc(String Id_Doc) {
		// your code here
	}

	public void modifierDocument() {
		// your code here
	}

	public void modifierDoc(String Id_Doc) {
		// your code here
	}

	public void selectionnerDocument(String Id_Doc) {
		// your code here
	}

	public void rechercheDocument() {
		// your code here
	}

	public void rechercherDocument(String Id_Doc) {
		// your code here
	}

	
	// PROGRAMME
	//*******************************************************

	/**
	 * Cr�ation d'un programme
	 * @param String titre
	 * @param String thematique
	 * @return String
	 * @throws SQLException 
	 */
	public String creerProgramme(String titre, String thematique) throws SQLException {

		System.out.println("Cr�ation du programme "+titre);
		
		//On v�rifie que le programme n'existe pas
		if (Programme.getByTitre(titre) == null) {
			
			//On cr�e le programme
			Programme p = Programme.create(titre, thematique);
			
			//On l'ajoute � la liste des programmes du syst�me
			programmes.put(p.getId(), p);
			
			System.out.println("Programme '"+titre+"' cr��");
			return Boolean.toString(true);
			
		}
		
		//Sinon, cr�ation refus�e
		System.out.println("Programme '"+titre+"' non cr��");
		return Boolean.toString(false);
	}
	
	/**
	 * Ajoute un document dans un programme
	 * @param String idDoc
	 * @param String idProg
	 * @return String
	 * @throws SQLException 
	 */
	public String ajouterDocumentProgramme(String idDoc, String idProg) throws SQLException {
		System.out.println("Ajout du document "+idDoc+" au programme "+idProg);
		
		//On v�rifie que le programme et le document existent
		if (programmes.containsKey(idProg) && documents.containsKey(idDoc)) {
			
			//On r�cup�re les objets
			Document d = (Document)documents.get(idDoc);
			Programme p = (Programme)programmes.get(idProg);
			
			//On ajoute le document au programme
			p.ajouterDocument(d);
			
			System.out.println("Document "+idDoc+" ajout� au programme "+idProg);
			return Boolean.toString(true);
			
		}
		
		//Sinon, ajout refus�
		System.out.println("Le document "+idDoc+" n'a pas �t� ajout� au programme "+idProg);
		return Boolean.toString(false);
	}
	
	public void RechercherProgramme(String Id_Prog, String Titre,
			String Thematique) {
		// your code here
	}

	public void SupprimerProgramme(String Id_Prog) {
		// your code here
	}

	public void ConfirmerSupprimerProgramme(Boolean SuppressionProgramme) {
		// your code here
	}

	public void SaisirInfosProgramme(String Id_Prog, String Titre) {
		// your code here
	}

	public void RechercherDocument(String Id_Doc) {
		// your code here
	}

	public void AjouterProgrammeArchive(String Id_Prog) {
		// your code here
	}
	
	public void VerifierinfosProgramme(String Id_Prog, String Titre,
			String Thematique) {
		// your code here
	}
	
	// CANAL
	//*******************************************************

	/**
	 * Cr�ation d'un canal
	 * @param String nom
	 * @param String utilMax
	 * @return String
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	public String creerCanal(String nom, String utilMax) throws NumberFormatException, SQLException {

		System.out.println("Cr�ation du canal "+nom);
		
		//On v�rifie que le canal n'existe pas
		if (Canal.getByNom(nom) == null) {
			
			//On cr�e le canal
			Canal c = Canal.create(nom, Integer.parseInt(utilMax));
			
			//On l'ajoute � la liste des canaux du syst�me
			canaux.put(c.getId(), c);
			
			System.out.println("Canal '"+nom+"' cr��");
			return Boolean.toString(true);
			
		}
		
		//Sinon, cr�ation refus�e
		System.out.println("Canal '"+nom+"' non cr��");
		return Boolean.toString(false);
	}
	
	/**
	 * Planifier un programme sur un canal
	 * @param String idProg
	 * @param String idCanal
	 * @param String jour
	 * @param String mois
	 * @param String annee
	 * @param String heure
	 * @param String minute
	 * @param String seconde 
	 * @return String
	 */
	public String planifierProgramme(String idProg, String idCanal, String jour, String mois, String annee, String heure, String minute, String seconde) {

		System.out.println("Planification du programme "+idProg+" sur le canal "+idCanal);
		
		//On v�rifie que le canal et le programme existent
		if (canaux.containsKey(idCanal) && programmes.containsKey(idProg)) {
			
			//On r�cup�re les objets
			Canal c = (Canal)canaux.get(idCanal);
			Programme p = (Programme)programmes.get(idProg);
			
			//On planifie le programme
			//c.planifierProgramme(p);
			
			System.out.println("Programme "+idProg+" planifi� sur le canal "+idCanal);
			return Boolean.toString(true);
			
		}
		
		//Sinon, ajout refus�
		System.out.println("Le programme "+idProg+" n'a pas �t� palnifi� sur le canal "+idCanal);
		return Boolean.toString(false);
	}
	
	/**
	 * Diffuser un programme sur un canal
	 * @param String idProg
	 * @param String idCanal
	 * @return String
	 */
	public String diffuserProgramme(String idProg, String idCanal) {
		
		System.out.println("Diffusion du programme "+idProg+" sur le canal "+idCanal);
		
		//On v�rifie que le canal et le programme existent
		if (canaux.containsKey(idCanal) && programmes.containsKey(idProg)) {
			
			//On r�cup�re les objets
			Canal c = (Canal)canaux.get(idCanal);
			Programme p = (Programme)programmes.get(idProg);
			
			//On diffuse le programme
			c.createRTPServer(ipStreaming, Integer.parseInt(portStreaming));
			c.diffuserProgramme(p);
			
			System.out.println("Programme "+idProg+" en diffusion sur le canal "+idCanal);
			return Boolean.toString(true);
			
		}
		
		//Sinon, ajout refus�
		System.out.println("La diffusion du programme "+idProg+" n'a pas �t� lanc�e sur le canal "+idCanal);
		return Boolean.toString(false);
	}
	
	/**
	 * Ecouter un canal (retourne l'URL du canal)
	 * @param String idCanal
	 * @return String
	 */
	public String ecouterCanal(String idCanal) {
		System.out.println("Ecoute du canal "+idCanal);
		
		//On v�rifie que le canal et le programme existent
		if (canaux.containsKey(idCanal)) {
			
			//On r�cup�re l'objet canal
			Canal c = (Canal)canaux.get(idCanal);
			
			System.out.println("Construction de l'url du canal "+idCanal);
			String url = c.getUrlStreaming();
			System.out.println("URL: "+url);
			
			return url;
		}
		
		//Sinon, ajout refus�
		System.out.println("Canal "+idCanal+" inconnu");
		return null;
	}

	public void ValiderCreationCanal() {
		// your code here
	}

	public void PlannifierUnProgramme() {
		// your code here
	}

	public ArrayIndexOutOfBoundsException ListeDocs() {
		// your code here
		return null;
	}

	public void ConfirmerSupprimerCanal(boolean SuppressionCanal) {
		// your code here
	}

	public void AnnulerSupprimerProgramme(boolean Suppression) {
		// your code here
	}

	public void ConfirmerCreationCanal(boolean Confirmation) {
		// your code here
	}

	public void RechercherUnCanal() {
		// your code here
	}

	public void SupprimerCanal(String Id_Canal) {
		// your code here
	}

	public void AnnulerSupprimerCanal(boolean Supprimer) {
		// your code here
	}

	public void VerifierInfosCanal(String Id_Canal, String NomCanal,
			int Flux_Max) {
		// your code here
	}

	public void ValiderAjout(boolean Validation) {
		// your code here
	}

	public void ValiderModification(boolean Validation) {
		// your code here
	}

	public void RetirerDocument(String Id_Doc) {
		// your code here
	}

	public void RetirerProgramme(String Id_Prog) {
		// your code here
	}

	public void AjoutDocument() {
		// your code here
	}

	public void AjouterProgramme() {
		// your code here
	}

	public void insertion(String choix) {
		// your code here
	}

	public void AjoutCanal(String Id_Canal) {
		// your code here
	}

	public void ValiderCreerProgramme() {
		// your code here
	}

	public void AjoutProgramme(String Id_Prog) {
		// your code here
	}

	public void SupprimerUnProgramme() {
		// your code here
	}

	public void RetraitCanal() {
		// your code here
	}

	public void ModifierUnProgramme() {
		// your code here
	}

	public void ModifierProgramme(String Id_Prog) {
		// your code here
	}

	public void ChoixModification(String Choix) {
		// your code here
	}

	public void VerifierDuree(int Duree) {
		// your code here
	}

	public void RechercherCanal(String Id_Canal, String NomCanal, int Flux_Max) {
		// your code here
	}

	public void Retirer(String Id) {
		// your code here
	}

	public void SupprimerUnCanal() {
		// your code here
	}

	public void retraitProgramme() {
		// your code here
	}

	public boolean Modifier() {
		// your code here
		return false;
	}

	public void PlanifierSurCanal(String Id_Canal) {
		// your code here
	}

	public void VerifierJourHeure(java.util.Date Jour, int Heure) {
		// your code here
	}

	public void RechercherUnProgramme() {
		// your code here
	}

	public void ArretDiffusion(boolean Choix) {
		// your code here
	}

	public void AjouterDocumentProgramme() {
		// your code here
	}
}
