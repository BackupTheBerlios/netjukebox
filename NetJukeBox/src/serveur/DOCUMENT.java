package serveur;

import java.util.Vector;

public class DOCUMENT {

	public String Id_Doc;
	public String Titre;
	public String Source;
	public int Duree;
	public int Jour;
	public int Mois;
	public int Annee;
	public String Langue;
	public String Genre;
	public String Fichier;
	
	public String Etat;

	//	Création du vecteur d'Id de programme et initialisation du compteur de verrou
	Vector vecteur_verrou = new Vector();
	int compteur_verrou = 0;
	
	// A quoi ça sert ???
	public java.util.Collection pROGRAMME = new java.util.TreeSet();
	public Contrat contrat;
    
	public void Creer(String Id_Doc, String Titre, int Duree, int Jour, int Mois, int Annee, String Source, String Langue, String Genre, String Fichier) {        
		this.Id_Doc = Id_Doc;
		this.Titre = Titre;
		this.Jour = Jour;
		this.Mois = Mois;
		this.Annee = Annee;
		this.Source = Source;
		this.Langue = Langue;
		this.Genre = Genre;
		this.Fichier = Fichier;
		this.Duree = Duree;
    } 
	
	public String GetId_Doc() {        
		return Id_Doc;
    } 

	public String GetTitre() {        
			return Titre;
	} 

	 public int GetDuree() {
		 return Duree;
	 }

	public String GetSource() {        
		return Source;
    } 

	public String GetLangue() {        
		return Langue;
    } 

	public String GetGenre() {        
		return Genre;
    } 
	
	public void PoserVerrou(String Id_Prog) {        
		vecteur_verrou.addElement(Id_Prog);
		compteur_verrou = compteur_verrou + 1;
		System.out.println("Document verrouillé : " + Id_Doc);
		System.out.println("Le compteur de verrou = " + compteur_verrou);
	}
	
	
	
	
	
	
	
	
	
	
	public String GetEtat() {        
        // your code here
        return null;
    }
	
	public java.util.Date GetDate_Céation() {        
        // your code here
		return null;
    }	
	
	
	public void NommerFichier(String Fichier) {        
        // your code here
    } 

	public void InsertionInfos(String Id_Doc, String Titre, java.util.Date Date_Creation, String Source, String Langue, String Genre) {        
        // your code here
    } 

	public Boolean EnLecture(String Etat) {        
        // your code here
        return null;
    } 

	public Boolean EstProgramme(String Etat) {        
        // your code here
        return null;
    } 

	public void Supprimer() {        
        // your code here
    } 

	public Boolean Suppression() {        
        // your code here
        return null;
    } 

	public Boolean SupprimerInfos() {        
        // your code here
        return null;
    } 

	public Boolean Modifier(String Id_Doc, String Titre, java.util.Date Date_Creation, String Source, String Langue, String Genre) {        
        // your code here
        return null;
    } 

	public Boolean MajInfos(String Id_Doc, String Titre, java.util.Date Date_Creation, String Source, String Langue, String Genre) {        
        // your code here
        return null;
    } 

	public void Selectionner() {        
        // your code here
    } 

	public void GetProgrammesArchivés() {        
        // your code here
    } 

	
	public void DeverouillerDocument(String Id_Doc) {        
        // your code here
    } 
	
	

	public void CompterVerrouProgramme(int Verrou) {        
        // your code here
    } 

	 

	public void AjouterProgramme(String Id_Prog) {        
        // your code here
    } 

	public void EnleverProgramme(String Id_Prog) {        
        // your code here
    } 
 }