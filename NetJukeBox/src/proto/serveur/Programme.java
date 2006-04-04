package proto.serveur;
import java.util.Vector;


public class Programme {

	public String Id_Prog;
	public String Titre;
	public String Thematique;
	public boolean Etat = false;
	//Création du vecteur d'Id de Document
	Vector v = new Vector();
	//Initialisation du fichier archive
	String fichier_archive = "_Archive.txt";
	// Initialisation de l'état du programme non archivé
	String etat_archive = "false";
	
	//Initialisation du proramme à vide
	public void Creer(String Id_Prog, String Titre, String Thematique) {        
    	this.Id_Prog = Id_Prog;
    	this.Titre = Titre;
    	this.Thematique = Thematique;
    } 

	public String GetId_Prog(){
		return this.Id_Prog;
		//System.out.println(Id_Prog);
	}
	
	public String GetTitre(){
		return this.Titre;
		//System.out.println(Titre);
	}
	
	public String GetThematique(){
		return this.Thematique;
		//System.out.println(Thematique);
	}
	
	public boolean GetEtat() {
		return this.Etat;
		//System.out.println(Etat);
	}
	
	//Ajout d'un document dans le programme
	public void AjouterDocument(Document Doc) {        
		v.addElement(Doc.GetId_Doc());
		Doc.PoserVerrou(Id_Prog);
	}

	//Retrait d'un document du programme
	public void RetraitDocument(Document Doc) {        
		v.remove(Doc.GetId_Doc());
		Doc.DeverouillerDocument(Id_Prog);
    }
	
	public Vector ListerDocuments() {        
		return v;
	}
	
	public void AjouterProgramme(Programme Prog) {        
		Prog.ListerDocuments();
		//System.out.println(Prog.v);
		int i;
		String donnee;
		for(i = 0; i < Prog.v.size(); i++) {
			donnee = (String)Prog.v.elementAt(i);
			v.addElement(donnee);
			System.out.println("Le document : " + donnee + " du programme : " + Prog.Id_Prog + " a été inséré dans le programme : " + Id_Prog);
		}
    }
		
	//Sauvegarde du vecteur de document si cela n'a pas déjà été fait
	public void archiver() {        
		int i;
		String donnee;
		if (etat_archive == "false") {
			etat_archive = "true";
			//Création du nom complet du fichier Archive de tous les documents composant le programme
			fichier_archive = Id_Prog + fichier_archive;
			//Sauvegarde de la liste des documents du programme dans le fichier Archive
			for(i = 0; i < v.size(); i++) {
				donnee = (String)v.elementAt(i);
				new EcrireFichier(fichier_archive, donnee);
			}
			System.out.println("Le programme est archivé dans le fichier : " + fichier_archive);
		} else {
			System.out.println("Erreur : le programme est deja archive");}	
	}	

	
	
	
	
	
	
	
	
	
	
	
	////////////////////// INUTILE ????? ///////////////////////////////////
	public void VerouillerDocuments(String Id_Doc) {        
		System.out.println("Document verrouillé : " + Id_Doc);
    } 
	public void DeverouillerDocuments(String Id_Doc) {        
		System.out.println("Document déverrouillé : " + Id_Doc);
    } 
	//////////////////////INUTILE ????? ///////////////////////////////////
	
	
	
	
	
	
	//Modification des informations d'un programme
	public void InsertionInfos(String Id_Prog, String Titre, String Thematique) {        
        // your code here
    } 
	
//	A quoi ça sert ???
	public Diffusion dIFFUSION;
	public java.util.Collection dOCUMENT = new java.util.TreeSet();
	public Programme PROGRAMME;
	
	
	public boolean enDiffusion() {        
        return false;
    }
	
	 

	public void DiffuserProgramme(String Id_Prog) {        
        // your code here
    } 

	public void Ajout(String Id) {        
        // your code here
    } 

	public void SetTitre(String Titre) {        
        // your code here
    } 

	public void RetraitProgramme(String Id_Prog) {        
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