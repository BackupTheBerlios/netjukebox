package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class planifProgForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String idprog;
	private String idcanal;
	private String jour;
	private String mois;
	private String annee;
	private String heure;
	private String minute;
	private String seconde;

	// --------------------------------------------------------- Methods
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		idprog = null;
		idcanal = null;
		jour = null;
		mois = null;
		annee = null;
		heure = null;
		minute = null;
		seconde = null;
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

		if (idprog.length() == 0) {
			ae.add(idprog, new ActionError("error.id"));
		}
		if (idcanal.length() == 0)	{
			ae.add(idprog, new ActionError("error.id"));
		}
		if (jour.length() == 0) {
			ae.add(idprog, new ActionError("error.jour"));
		}
		if (mois.length() == 0) {
			ae.add(idprog, new ActionError("error.mois"));
		}
		if (annee.length() == 0) {
			ae.add(idprog, new ActionError("error.annee"));
		}
		if (heure.length() == 0) {
			ae.add(idprog, new ActionError("error.heure"));
		}
		if (minute.length() == 0) {
			ae.add(idprog, new ActionError("error.minute"));
		}
		if (seconde.length() == 0) {
			ae.add(idprog, new ActionError("error.seconde"));
		}
		return ae;
	}

	public String getIdprog()	{
		return idprog;
	}

	public void setIdprog(String idprog) {
		this.idprog = idprog;
	}

	public String getIdcanal() {
		return idcanal;
	}

	public void setIdcanal(String idcanal) {
		this.idcanal = idcanal;
	}
	
	public String getJour() {
		return jour;
	}

	public void setJour(String jour) {
		this.jour = jour;
	}
	
	public String getMois() {
		return mois;
	}

	public void setMois(String mois) {
		this.mois = mois;
	}
	
	public String getAnnee() {
		return annee;
	}

	public void setAnnee(String annee) {
		this.annee = annee;
	}
	
	public String getHeure() {
		return heure;
	}

	public void setHeure(String heure) {
		this.heure = heure;
	}
	
	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}
	
	public String getSeconde() {
		return seconde;
	}

	public void setSeconde(String seconde) {
		this.seconde = seconde;
	}
}