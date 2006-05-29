package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class NewProgForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String titre;
	private String thematique;

	// --------------------------------------------------------- Methods
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		titre = null;
		thematique = null;
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
		if (thematique.length() == 0)	{
			ae.add(titre, new ActionError("error.thematique"));
		}
		return ae;
	}

	public String getThematique()	{
		return thematique;
	}

	public void setThematique(String thematique) {
		this.thematique = thematique;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}
}