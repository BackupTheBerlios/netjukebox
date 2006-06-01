package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class rechercheContratForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String id;
	private String titre;
	private String joursig;
	private String moissig;
	private String anneesig;
	private String jourexp;
	private String moisexp;
	private String anneeexp;
	private String contractant;
	private String modeReglement;
	private String type;

	// --------------------------------------------------------- Methods
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		id = null;
		titre = null;
		joursig = null;
		moissig = null;
		anneesig= null;
		jourexp = null;
		moisexp = null;
		anneeexp = null;
		contractant = null;
		modeReglement = null;
		type = null;
	}

	/** 
	 * Method validate
	 * @param ActionMapping mapping
	 * @param HttpServletRequest request
	 * @return ActionErrors
	 */
	@SuppressWarnings("deprecation")
	public ActionErrors validate(ActionMapping mapping,	HttpServletRequest request)	{
		ActionErrors ae = new ActionErrors();
		return ae;
	}

	public String getId()	{
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getTitre()	{
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getJoursig() {
		return joursig;
	}

	public void setJoursig(String joursig) {
		this.joursig = joursig;
	}
	
	public String getMoissig() {
		return moissig;
	}

	public void setMoissig(String moissig) {
		this.moissig = moissig;
	}
	
	public String getAnneesig() {
		return anneesig;
	}

	public void setAnneesig(String anneesig) {
		this.anneesig = anneesig;
	}
	
	public String getJourexp() {
		return jourexp;
	}

	public void setJourexp(String jourexp) {
		this.jourexp = jourexp;
	}
	
	public String getMoisexp() {
		return moisexp;
	}

	public void setMoisexp(String moisexp) {
		this.moisexp = moisexp;
	}
	
	public String getAnneeexp() {
		return anneeexp;
	}

	public void setAnneeexp(String anneeexp) {
		this.anneeexp = anneeexp;
	}
	
	public String getContractant() {
		return contractant;
	}

	public void setContractant(String contractant) {
		this.contractant = contractant;
	}
	
	public String getModeReglement() {
		return modeReglement;
	}

	public void setmodeReglement(String modeReglement) {
		this.modeReglement = modeReglement;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}