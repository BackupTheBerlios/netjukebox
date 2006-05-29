package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class NewContratForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String titre;
	private String joursig;
	private String moissig;
	private String anneesig;
	private String jourexp;
	private String moisexp;
	private String anneeexp;
	private String idcontractant;
	private String reg;
	private String type;

	// --------------------------------------------------------- Methods
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		titre = null;
		joursig = null;
		moissig = null;
		anneesig = null;
		jourexp = null;
		moisexp = null;
		anneeexp = null;
		idcontractant = null;
		reg = null;
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

		if (titre.length() == 0) {
			ae.add(titre, new ActionError("error.titre"));
		}
		if (joursig.length() == 0)	{
			ae.add(titre, new ActionError("error.joursig"));
		}
		if (moissig.length() == 0)	{
			ae.add(titre, new ActionError("error.moissig"));
		}
		if (anneesig.length() == 0)	{
			ae.add(titre, new ActionError("error.anneesig"));
		}
		if (jourexp.length() == 0)	{
			ae.add(titre, new ActionError("error.jourexp"));
		}
		if (moisexp.length() == 0)	{
			ae.add(titre, new ActionError("error.moisexp"));
		}
		if (anneeexp.length() == 0)	{
			ae.add(titre, new ActionError("error.anneeexp"));
		}
		if (idcontractant.length() == 0)	{
			ae.add(titre, new ActionError("error.id"));
		}
		if (reg.length() == 0)	{
			ae.add(titre, new ActionError("error.reg"));
		}
		if (type.length() == 0)	{
			ae.add(titre, new ActionError("error.type"));
		}
		
		return ae;
	}

	public String getTitre()	{
		return titre;
	}

	public String getJoursig() {
		return joursig;
	}
	
	public String getMoissig() {
		return moissig;
	}
	
	public String getAnneesig() {
		return anneesig;
	}
	
	public String getJourexp() {
		return jourexp;
	}
	
	public String getMoisexp() {
		return moisexp;
	}
	
	public String getAnneeexp() {
		return anneeexp;
	}
	
	public String getIdcontractant() {
		return idcontractant;
	}
	
	public String getReg() {
		return reg;
	}
	
	public String getType() {
		return type;
	}
	
	public void setTitre(String titre) {
		this.titre = titre;
	}

	public void setJoursig(String joursig) {
		this.joursig = joursig;
	}
	
	public void setMoissig(String moissig) {
		this.moissig = moissig;
	}
	
	public void setAnneesig(String anneesig) {
		this.anneesig = anneesig;
	}
	
	public void setIdcontractant(String idcontractant) {
		this.idcontractant = idcontractant;
	}
	
	public void setJourexp(String jourexp) {
		this.jourexp = jourexp;
	}
	
	public void setMoisexp(String moisexp) {
		this.moisexp = moisexp;
	}
	
	public void setAnneeexp(String anneeexp) {
		this.anneeexp = anneeexp;
	}
	
	public void setReg(String reg) {
		this.reg = reg;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}