package Serveur;

public class UTILISATEUR {
	public String Login;
	public String Nom;
	public String Prenom;
	public String Mail;
	public String Pwd;
	public String Pays;

	public Attribuer attribuer;

	public void Creer(String Login, String Nom, String Prenom, String Mail, String Pwd, String Pays){
		this.Login = Login;
		this.Nom = Nom;
		this.Prenom = Prenom;
		this.Mail = Mail;
		this.Pwd = Pwd;
		this.Pays = Pays;
	}
	
	public String GetLogin() {
		return Login;
    }
	
	public String GetNom() {
		return Nom;
    } 

	public String GetPrenom() {
		return Prenom;
    } 

	public String GetMail() {
		return Mail;
    } 

	public String GetPwd() {
		return Pwd;
    } 

	public String GetPays() {
		return Pays;
    } 
	
	
	
	
	
	
	
	public boolean Connecter() {        
        // your code here
        return false;
    }
		
	public Boolean Deconnecter() {        
        // your code here
        return null;
    } 

	public void Deconnexion() {        
        // your code here
    } 

	public boolean Connecter(String Login, String Pwd) {        
        // your code here
        return false;
    } 

	public void VérifierPwd(String Pwd, String Login) {        
        // your code here
    } 

	public Boolean Inscrire(String Nom, String Prenom, String Mail, String Pwd, String Pays) {        
        // your code here
        return null;
    } 

	public Boolean Inscription(String Nom, String Prenom, String Mail, String Pwd, java.util.List Pays) {        
        // your code here
        return null;
    } 

	public void SupprimerInfos() {        
        // your code here
    } 

	public void ModifierInfos() {        
        // your code here
    } 

	public void SupprimerUtilisateur(String Login) {        
        // your code here
    } 

	public Boolean SetPermission(int Id_Perm, String Lib_Perm) {        
        // your code here
        return null;
    } 

	public Boolean VérifConnecté() {        
        // your code here
        return null;
    } 

	public void supprimer() {        
        // your code here
    } 
 
	public Boolean EstConnecté() {        
        // your code here
        return null;
    } 

	public void Selectionner() {        
        // your code here
    } 

	public Boolean Modifier(String Nom, String Prenom, String Mail, String Pwd, String Pays) {        
        // your code here
        return null;
    } 

	public Boolean MajInfos(String Nom, String Prenom, String Mail, String Pwd, String Pays) {        
        // your code here
        return null;
    } 

	public void GetPermissions() {        
        // your code here
    } 

	public Boolean ajouterPermission(int Id_Perm) {        
        // your code here
        return null;
    } 

	public Boolean RetirerPermission(int Id_Perm) {        
        // your code here
        return null;
    } 

	public Boolean DefinirPermission(int Id_Perm, String Lib_Perm) {        
        // your code here
        return null;
    } 
}