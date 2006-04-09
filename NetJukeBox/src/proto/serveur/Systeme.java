package proto.serveur;

import java.util.Vector;

/**
 * Classe contenant la logique principale du serveur principal
 */
public class Systeme {

// ATTRIBUTS DU SYSTEME
//************************************************************
	
	/**
	 * Utilisateurs connectés aux système
	 */
	private Vector utilisateurs;

// CONSTRUCTEUR
//************************************************************
	
	/**
	 * Constructeur
	 */
	public Systeme() {
		
		utilisateurs = new Vector();
	}
	
// METHODES DU SYSTEME
//************************************************************
	
	/**
	 * Connexion d'un utilisateur
	 * @param login
	 * @param pwd
	 * @return String
	 */
	public String Connexion(String login, String pwd) {
		
		//On vérifie que le couple login/pwd existe
		if (Utilisateur.VérifierPwd(pwd, login)) {
			
			Utilisateur util = new Utilisateur(login);
			
			//On connecte l'utilisateur
			util.Connexion();
			
			//On l'ajoute à la liste des utilisateurs connectés au système
			utilisateurs.addElement(util);
			
			return "Connexion établie";
		}
		
		//Sinon, connexion refusée
		return "Connexion refusée";
	}
	/**
	 * Deconnexion d'un utilisateur
	 */
	public String deconnexion(String login) {
		
		//S'il y a des utilisateurs connectés
		if (utilisateurs.size()>0) {
			
			//On recherche l'utilisateur en question parmi cette liste
			int indice=0 ;
			Utilisateur util = (Utilisateur)utilisateurs.elementAt(indice);
			
			while (indice<utilisateurs.size() && util.GetLogin()!=login) {
				indice++;
			}
			
			//Si on a trouvé l'utilisateur connecté
			if (indice<utilisateurs.size()) {
				
				//On le déconnecte
				util.Deconnexion();
				
				//On le supprime de la liste des utilisateurs connectés au système
				utilisateurs.removeElementAt(indice);
				
				return "Utilisateur deconnecté";
			}
			return "Utilisateur introuvable";
		}
		return "Aucun utilisateur n'est connecté";
	}

	/**
	 * Recherche un utilisateur à partir de son login
	 * @param Login
	 */
	public Utilisateur chercherUtilisateur(String login) {
		
		//On vérifie que le couple login existe
		if (Utilisateur.VérifierLogin(login)) return new Utilisateur(login);

		return null;
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param u
	 */
	public void connecterUtilisateur(Utilisateur u) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Login
	 */
	public void deconnecterUtilisateur(String Login) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void inscription() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void suppressionUtilisateur() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void rechercheUtilisateur() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Login
	 */
	public void rechercherUtilisateur(String Login) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Login
	 */
	public void selectionnerUtilisateur(String Login) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Login
	 */
	public void supprimerUtilisateur(String Login) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void supprimerDocument() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void ajouterDocument() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @return
	 */
	public boolean documentExistant() {
		// your code here
		return false;
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Doc
	 */
	public void supprimerDoc(String Id_Doc) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void modifierDocument() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Doc
	 */
	public void modifierDoc(String Id_Doc) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Doc
	 */
	public void selectionnerDocument(String Id_Doc) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void rechercheDocument() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Doc
	 */
	public void rechercherDocument(String Id_Doc) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Email
	 * @return
	 */
	public boolean emailValide(String Email) {
		// your code here
		return false;
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void modifierUtilisateur() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Login
	 */
	public void modifierUtil(String Login) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void getListePermissions() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Prog
	 * @param Titre
	 * @param Thematique
	 */
	public void RechercherProgramme(String Id_Prog, String Titre,
			String Thematique) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Prog
	 */
	public void SupprimerProgramme(String Id_Prog) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param SuppressionProgramme
	 */
	public void ConfirmerSupprimerProgramme(Boolean SuppressionProgramme) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Prog
	 * @param Titre
	 */
	public void SaisirInfosProgramme(String Id_Prog, String Titre) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Doc
	 */
	public void AjouterDocument(String Id_Doc) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Doc
	 */
	public void RechercherDocument(String Id_Doc) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Prog
	 */
	public void AjouterProgrammeArchive(String Id_Prog) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Canal
	 */
	public void CreerCanal(Object Canal) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Programme
	 */
	public void CreerProgramme(Object Programme) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void ValiderCreationCanal() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void PlannifierUnProgramme() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Prog
	 */
	public void DiffuserProgramme(String Id_Prog) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @return
	 */
	public ArrayIndexOutOfBoundsException ListeDocs() {
		// your code here
		return null;
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param SuppressionCanal
	 */
	public void ConfirmerSupprimerCanal(boolean SuppressionCanal) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Suppression
	 */
	public void AnnulerSupprimerProgramme(boolean Suppression) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Confirmation
	 */
	public void ConfirmerCreationCanal(boolean Confirmation) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void RechercherUnCanal() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Canal
	 */
	public void SupprimerCanal(String Id_Canal) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Supprimer
	 */
	public void AnnulerSupprimerCanal(boolean Supprimer) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Canal
	 * @param NomCanal
	 * @param Flux_Max
	 */
	public void VerifierInfosCanal(String Id_Canal, String NomCanal,
			int Flux_Max) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Prog
	 * @param Titre
	 * @param Thematique
	 */
	public void VerifierinfosProgramme(String Id_Prog, String Titre,
			String Thematique) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Validation
	 */
	public void ValiderAjout(boolean Validation) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Validation
	 */
	public void ValiderModification(boolean Validation) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Doc
	 */
	public void RetirerDocument(String Id_Doc) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Prog
	 */
	public void RetirerProgramme(String Id_Prog) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void AjoutDocument() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void AjouterProgramme() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param choix
	 */
	public void insertion(String choix) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Canal
	 */
	public void AjoutCanal(String Id_Canal) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void ValiderCreerProgramme() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Prog
	 */
	public void AjoutProgramme(String Id_Prog) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void SupprimerUnProgramme() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void RetraitCanal() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Prog
	 */
	public void PlannifierProgramme(String Id_Prog) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void ModifierUnProgramme() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Prog
	 */
	public void ModifierProgramme(String Id_Prog) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Choix
	 */
	public void ChoixModification(String Choix) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Duree
	 */
	public void VerifierDuree(int Duree) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Canal
	 * @param NomCanal
	 * @param Flux_Max
	 */
	public void RechercherCanal(String Id_Canal, String NomCanal, int Flux_Max) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id
	 */
	public void Retirer(String Id) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void SupprimerUnCanal() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void retraitProgramme() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @return
	 */
	public boolean Modifier() {
		// your code here
		return false;
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Id_Canal
	 */
	public void PlanifierSurCanal(String Id_Canal) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Jour
	 * @param Heure
	 */
	public void VerifierJourHeure(java.util.Date Jour, int Heure) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void RechercherUnProgramme() {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 * 
	 * @param Choix
	 */
	public void ArretDiffusion(boolean Choix) {
		// your code here
	}

	/**
	 * <p>
	 * Does ...
	 * </p>
	 * 
	 */
	public void AjouterDocumentProgramme() {
		// your code here
	}
}
