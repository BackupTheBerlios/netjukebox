package serveur;
import java.util.Vector;

public class PROGRAMME {

	public String Id_Prog;
	public String Titre;
	public String Thematique;
	public boolean Etat = false;
	Vector v = new Vector();
	
	//A quoi ça sert ???
	public DIFFUSION dIFFUSION;
	public java.util.Collection dOCUMENT = new java.util.TreeSet();
	public PROGRAMME PROGRAMME;

	//Initialisation du proramme à vide
	public void Creer(String Id_Prog, String Titre, String Thematique) {        
    	this.Id_Prog = Id_Prog;
    	this.Titre = Titre;
    	this.Thematique = Thematique;
    } 

	public void GetId_Prog(){
		//return Id_Prog;
		System.out.println(Id_Prog);
	}
	
	public void GetTitre(){
		//return Titre;
		System.out.println(Titre);
	}
	
	public void GetThematique(){
		//return Thematique;
		System.out.println(Thematique);
	}
	
	public void GetEtat() {
		//return Etat;
		System.out.println(Etat);
	}
	
	public void AjouterDocument(String Id_Doc) {        
		v.addElement(Id_Doc);
		//Affichage du vecteur de document
        int i;
		for(i = 0; i < v.size(); i++)
		{
			System.out.println(v.elementAt(i));
		}
	} 
	
	
	
	public boolean enDiffusion() {        
        return false;
    }
	
	public void AjouterProgramme(String Id_Prog) {        
        // your code here
    } 

	 

	public void ListerDocuments(String Id_Doc) {        
        // your code here
    } 

	public void archiver() {        
        // your de here
    } 

	public void DiffuserProgramme(String Id_Prog) {        
        // your code here
    } 

	public void VerrouillerDocument(String Id_Doc) {        
        // your code here
    } 

	public void Ajout(String Id) {        
        // your code here
    } 

	public void DeverouillerDocuments() {        
        // your code here
    } 

	public void SetTitre(String Titre) {        
        // your code here
    } 

	public void RetraitDocument(String Id_Doc) {        
        // your code here
    } 

	public void RetraitProgramme(String Id_Prog) {        
        // your code here
    } 

	public void InsertionInfos(String Id_Prog, String Titre, String Thematique) {        
        // your code here
    } 

	public void Planifier(java.util.Date Jour, int Heure, String IdeCanal) {        
        // your code here
    } 

	public boolean SetDiffusion() {        
        // your code here
        return false;
    } 

	public void ArreterDiffusionProgramme(String Id_Prog) {        
        // your code here
    } 

	public void RelancerDiffusionProgramme(String Id_Prog) {        
        // your code here
    } 

	public boolean retirerDocument(String Id) {        
        // your code here
        return false;
    } 

	public boolean retraitDoc(String Id) {        
        // your code here
        return false;
	} 
}