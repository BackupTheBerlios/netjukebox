package serveur;


/**
 * <p></p>
 * 
 */
public class CANAL {

/**
 * <p>Represents ...</p>
 * 
 */
    public String Id_Canal;

/**
 * <p>Represents ...</p>
 * 
 */
    public String nom_canal;

/**
 * <p>Represents ...</p>
 * 
 */
    public Integer Flux_Max;

/**
 * <p>Represents ...</p>
 * 
 */
    public Boolean Etat;
/**
 * <p></p>
 * 
 */
    public DIFFUSION dIFFUSION;
/**
 * <p></p>
 * 
 */
    public JOURNAL_CANAL jOURNAL_CANAL;

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id_Canal 
 * @param Nom_Canal 
 * @param Flux_Max 
 */
    public void Creer(String Id_Canal, String Nom_Canal, int Flux_Max) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id_Canal 
 */
    public void Detruire(String Id_Canal) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public boolean EstActif() {        
        // your code here
        return false;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public String GetId_Canal() {        
        // your code here
        return null;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public String GetId_Nom_Canal() {        
        // your code here
        return null;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public int GetId_Flux_Maxl() {        
        // your code here
        return 0;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Id_Canal 
 * @param Nom_Canal 
 * @param Flux_Max 
 */
    public void InsertionInfos(String Id_Canal, String Nom_Canal, int Flux_Max) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param Jour 
 * @param Heure 
 * @param IdeCanal 
 * @return 
 */
    public boolean VerifierPlanification(java.util.Date Jour, int Heure, String IdeCanal) {        
        // your code here
        return false;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param IdeProgramme 
 * @param Jour 
 * @param Heure 
 */
    public void BloquerPlage(String IdeProgramme, java.util.Date Jour, int Heure) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param IdeProgramme 
 */
    public void DiffuserProgramme(String IdeProgramme) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public boolean SetActif() {        
        // your code here
        return false;
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param IdeCanal 
 */
    public void ArreterDiffusionCanal(int IdeCanal) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param IdeAuditeur 
 */
    public void DeconnecterAuditeurs(String IdeAuditeur) {        
        // your code here
    } 

/**
 * <p>Does ...</p>
 * 
 * 
 * @param IdeCanal 
 */
    public void RelanceDiffusionCanal(String IdeCanal) {        
        // your code here
    } 
 }
