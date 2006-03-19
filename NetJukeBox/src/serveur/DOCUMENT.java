package serveur;


/**
 * <p></p>
 * 
 */
public class DOCUMENT {

/**
 * <p>Represents ...</p>
 * 
 */
    public String Id_Doc;

/**
 * <p>Represents ...</p>
 * 
 */
    public String Titre;

/**
 * <p>Represents ...</p>
 * 
 */
    public java.util.Date Date_Creation;

/**
 * <p>Represents ...</p>
 * 
 */
    public String Source;

/**
 * <p>Represents ...</p>
 * 
 */
    public int Duree;

/**
 * <p>Represents ...</p>
 * 
 */
    public String Etat;

/**
 * <p>Represents ...</p>
 * 
 */
    public String Langue;

/**
 * <p>Represents ...</p>
 * 
 */
    public String Genre;

/**
 * <p>Represents ...</p>
 * 
 */
    public String Fichier;
/**
 * <p></p>
 * 
 * 
 * @poseidon-type PROGRAMME
 */
    public java.util.Collection pROGRAMME = new java.util.TreeSet();
/**
 * <p></p>
 * 
 */
    public Contrat contrat;

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id_Doc 
 * @param Titre 
 * @param Date_Creation 
 * @param Source 
 * @param Langue 
 * @param Genre 
 * @param Fichier 
 */
    public void Creer(String Id_Doc, String Titre, java.util.Date Date_Creation, String Source, String Langue, String Genre, String Fichier) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Fichier 
 */
    public void NommerFichier(String Fichier) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id_Doc 
 * @param Titre 
 * @param Date_Creation 
 * @param Source 
 * @param Langue 
 * @param Genre 
 */
    public void InsertionInfos(String Id_Doc, String Titre, java.util.Date Date_Creation, String Source, String Langue, String Genre) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Etat 
 * @return 
 */
    public Boolean EnLecture(String Etat) {        
        // your code here
        return null;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Etat 
 * @return 
 */
    public Boolean EstProgramme(String Etat) {        
        // your code here
        return null;
    } 

/**
 * <p>Does ...</p>
 * 
 */
    public void Supprimer() {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public Boolean Suppression() {        
        // your code here
        return null;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public Boolean SupprimerInfos() {        
        // your code here
        return null;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id_Doc 
 * @param Titre 
 * @param Date_Creation 
 * @param Source 
 * @param Langue 
 * @param Genre 
 * @return 
 */
    public Boolean Modifier(String Id_Doc, String Titre, java.util.Date Date_Creation, String Source, String Langue, String Genre) {        
        // your code here
        return null;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id_Doc 
 * @param Titre 
 * @param Date_Creation 
 * @param Source 
 * @param Langue 
 * @param Genre 
 * @return 
 */
    public Boolean MajInfos(String Id_Doc, String Titre, java.util.Date Date_Creation, String Source, String Langue, String Genre) {        
        // your code here
        return null;
    } 

/**
 * <p>Does ...</p>
 * 
 */
    public void Selectionner() {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 */
    public void GetProgrammesArchivés() {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public String GetId_doc() {        
        // your code here
        return null;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public String GetTitre() {        
        // your code here
        return null;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public java.util.Date GetDate_Céation() {        
        // your code here
        return null;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public String GetSource() {        
        // your code here
        return null;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public int GetDuree() {        
        // your code here
        return 0;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public String GetEtat() {        
        // your code here
        return null;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public String GetLangue() {        
        // your code here
        return null;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public String GetGenre() {        
        // your code here
        return null;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id_Doc 
 */
    public void DeverouillerDocument(String Id_Doc) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Verrou 
 */
    public void CompterVerrouProgramme(int Verrou) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Verrou 
 * @param Id_Prog 
 */
    public void PoserVerrou(int Verrou, String Id_Prog) {        
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
 * @param Id_Prog 
 */
    public void EnleverProgramme(String Id_Prog) {        
        // your code here
    } 
 }
