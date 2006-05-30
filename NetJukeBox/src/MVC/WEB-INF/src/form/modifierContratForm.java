package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class modifierContratForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String id;
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
		id = null;
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
		
		if (id.length() == 0) {
			ae.add(id, new ActionError("error.id"));
		}
		if (titre.length() == 0) {
			ae.add(id, new ActionError("error.titre"));
		}
		if (joursig.length() == 0) {
			ae.add(id, new ActionError("error.joursig"));
		}
		if (moissig.length() == 0) {
			ae.add(id, new ActionError("error.moissig"));
		}
		if (anneesig.length() == 0) {
			ae.add(id, new ActionError("error.anneesig"));
		}
		if (jourexp.length() == 0) {
			ae.add(id, new ActionError("error.jourexp"));
		}
		if (moisexp.length() == 0) {
			ae.add(id, new ActionError("error.moisexp"));
		}
		if (anneeexp.length() == 0) {
			ae.add(id, new ActionError("error.anneeexp"));
		}
		if (idcontractant.length() == 0) {
			ae.add(id, new ActionError("error.idcontractant"));
		}
		if (reg.length() == 0) {
			ae.add(id, new ActionError("error.reg"));
		}
		if (type.length() == 0) {
			ae.add(id, new ActionError("error.type"));
		}
		
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
	
	public String getIdcontractant() {
		return idcontractant;
	}

	public void setIdcontractant(String idcontractant) {
		this.idcontractant = idcontractant;
	}
	
	public String getReg() {
		return reg;
	}

	public void setReg(String reg) {
		this.reg = reg;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}