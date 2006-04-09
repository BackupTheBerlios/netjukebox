package Serveur;

public class CANAL {
	public String Id_Canal;
	public String Nom_Canal;
	public int Util_Max;
	public String Port;
	
	public void Creer(String Id_Canal, String Nom_Canal, int Util_Max, String Port) {        
        this.Id_Canal = Id_Canal;
        this.Nom_Canal = Nom_Canal;
        this.Util_Max = Util_Max;
        this.Port = Port;
        System.out.println("Le Canal : " + Nom_Canal + " a été créé");
    } 
	
	public String GetId_Canal() {        
		return this.Id_Canal;
    }
	
	public String GetNom_Canal() {        
		return this.Nom_Canal;
    }

	public String GetPort(){
		return this.Port;
	}
	
	public int GetFlux_Maxl() {        
		return this.Util_Max;
    }

	public void DiffuserProgramme(PROGRAMME Prog) {        
		Prog.GetTitre();
		Prog.DiffuserProgramme();
		System.out.println("Le programme " +  Prog.GetTitre() + " est en cours de diffusion");
		for(int i = 0; i < Prog.v.size(); i++) {
			String donnee = (String)Prog.v.elementAt(i);
			System.out.println("Le document : " + donnee + " a été diffusé");
		}
		//new RTPServer(Port, Prog.v);
	} 
	
	
	
	public Boolean Etat;
	public DIFFUSION dIFFUSION;
	public JOURNAL_CANAL jOURNAL_CANAL;

	
	
	
	public void Detruire(String Id_Canal) {        
        // your code here
    } 

	public boolean EstActif() {        
        // your code here
        return false;
    } 
 
	public void InsertionInfos(String Id_Canal, String Nom_Canal, int Flux_Max) {        
        // your code here
    } 

	public boolean VerifierPlanification(java.util.Date Jour, int Heure, String IdeCanal) {        
        // your code here
        return false;
    } 

	public void BloquerPlage(String IdeProgramme, java.util.Date Jour, int Heure) {        
        // your code here
    } 

	public boolean SetActif() {        
        // your code here
        return false;
    } 

	public void ArreterDiffusionCanal(int IdeCanal) {        
        // your code here
    } 

	public void DeconnecterAuditeurs(String IdeAuditeur) {        
        // your code here
    } 

	public void RelanceDiffusionCanal(String IdeCanal) {        
        // your code here
    } 
}
