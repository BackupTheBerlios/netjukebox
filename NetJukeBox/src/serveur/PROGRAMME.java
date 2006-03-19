package serveur;


/**
 * <p></p>
 * 
 */
public class PROGRAMME {

/**
 * <p>Represents ...</p>
 * 
 */
    public String Id_Prog;

/**
 * <p>Represents ...</p>
 * 
 */
    public String Titre;

/**
 * <p>Represents ...</p>
 * 
 */
    public boolean Etat;

/**
 * <p>Represents ...</p>
 * 
 */
    public String Thematique;
/**
 * <p></p>
 * 
 */
    public DIFFUSION dIFFUSION;
/**
 * <p></p>
 * 
 * 
 * @poseidon-type DOCUMENT
 */
    public java.util.Collection dOCUMENT = new java.util.TreeSet();
/**
 * <p></p>
 * 
 */
    public PROGRAMME pROGRAMME;

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id_Prog 
 * @param Titre 
 * @param Thematique 
 */
    public void Creer(String Id_Prog, String Titre, String Thematique) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id_Prog 
 */
    public void AjouterProgramme(String Id_Prog) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public boolean enDiffusion() {        
        // your code here
        return false;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id_Doc 
 */
    public void ListerDocuments(String Id_Doc) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 */
    public void archiver() {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id_Prog 
 */
    public void DiffuserProgramme(String Id_Prog) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id_Doc 
 */
    public void VerrouillerDocument(String Id_Doc) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id_Doc 
 */
    public void AjouterDocument(String Id_Doc) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id 
 */
    public void Ajout(String Id) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 */
    public void DeverouillerDocuments() {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Titre 
 */
    public void GetTitre(String Titre) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Titre 
 */
    public void SetTitre(String Titre) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id_Doc 
 */
    public void RetraitDocument(String Id_Doc) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id_Prog 
 */
    public void RetraitProgramme(String Id_Prog) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id_Prog 
 * @param Titre 
 * @param Thematique 
 */
    public void InsertionInfos(String Id_Prog, String Titre, String Thematique) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Jour 
 * @param Heure 
 * @param IdeCanal 
 */
    public void Planifier(java.util.Date Jour, int Heure, String IdeCanal) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public boolean SetDiffusion() {        
        // your code here
        return false;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id_Prog 
 */
    public void ArreterDiffusionProgramme(String Id_Prog) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id_Prog 
 */
    public void RelancerDiffusionProgramme(String Id_Prog) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id 
 * @return 
 */
    public boolean retirerDocument(String Id) {        
        // your code here
        return false;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id 
 * @return 
 */
    public boolean retraitDoc(String Id) {        
        // your code here
        return false;
    } 
 }
