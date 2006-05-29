package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class NewCanalForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String nom;
	private String nbmaxutil;

	// --------------------------------------------------------- Methods
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		nom = null;
		nbmaxutil = null;
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

		if (nom.length() == 0) {
			ae.add(nom, new ActionError("error.titre"));
		}
		if (nbmaxutil.length() == 0)	{
			ae.add(nom, new ActionError("error.thematique"));
		}
		return ae;
	}

	public String getNom()	{
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getNbmaxutil() {
		return nbmaxutil;
	}

	public void setNbmaxutil(String nbmaxutil) {
		this.nbmaxutil = nbmaxutil;
	}
}