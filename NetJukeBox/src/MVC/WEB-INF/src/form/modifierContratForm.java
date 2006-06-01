package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class modifierContratForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String id;
	private String titre;
	private String jourSignature;
	private String moisSignature;
	private String anneeSignature;
	private String jourExpiration;
	private String moisExpiration;
	private String anneeExpiration;
	private String contractant;
	private String modeReglement;
	private String type;

	// --------------------------------------------------------- Methods
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		id = null;
		titre = null;
		jourSignature = null;
		moisSignature = null;
		anneeSignature = null;
		jourExpiration = null;
		moisExpiration = null;
		anneeExpiration = null;
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
		
		if (id.length() == 0) {
			ae.add(id, new ActionError("error.id"));
		}
		if (titre.length() == 0) {
			ae.add(id, new ActionError("error.titre"));
		}
		if (jourSignature.length() == 0) {
			ae.add(id, new ActionError("error.joursig"));
		}
		if (moisSignature.length() == 0) {
			ae.add(id, new ActionError("error.moissig"));
		}
		if (anneeSignature.length() == 0) {
			ae.add(id, new ActionError("error.anneesig"));
		}
		if (jourExpiration.length() == 0) {
			ae.add(id, new ActionError("error.jourexp"));
		}
		if (moisExpiration.length() == 0) {
			ae.add(id, new ActionError("error.moisexp"));
		}
		if (anneeExpiration.length() == 0) {
			ae.add(id, new ActionError("error.anneeexp"));
		}
		if (contractant.length() == 0) {
			ae.add(id, new ActionError("error.idcontractant"));
		}
		if (modeReglement.length() == 0) {
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

	public String getJourSignature() {
		return jourSignature;
	}

	public void setJourSignature(String jourSignature) {
		this.jourSignature = jourSignature;
	}
	
	public String getMoisSignature() {
		return moisSignature;
	}

	public void setMoisSignature(String moisSignature) {
		this.moisSignature = moisSignature;
	}
	
	public String getAnneeSignature() {
		return anneeSignature;
	}

	public void setAnneeSignature(String anneeSignature) {
		this.anneeSignature = anneeSignature;
	}
	
	public String getJourExpiration() {
		return jourExpiration;
	}

	public void setJourExpiration(String jourExpiration) {
		this.jourExpiration = jourExpiration;
	}
	
	public String getMoisExpiration() {
		return moisExpiration;
	}

	public void setMoisExpiration(String moisExpiration) {
		this.moisExpiration = moisExpiration;
	}
	
	public String getAnneeExpiration() {
		return anneeExpiration;
	}

	public void setAnneeExpiration(String anneeExpiration) {
		this.anneeExpiration = anneeExpiration;
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

	public void setModeReglement(String modeReglement) {
		this.modeReglement = modeReglement;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}