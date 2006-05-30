package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class modifierProgForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String id;
	private String titre;
	private String thematique;

	// --------------------------------------------------------- Methods
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		id = null;
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
		
		if (id.length() == 0) {
			ae.add(id, new ActionError("error.id"));
		}
		if (titre.length() == 0) {
			ae.add(id, new ActionError("error.titre"));
		}
		if (thematique.length() == 0) {
			ae.add(id, new ActionError("error.thematique"));
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

	public String getThematique() {
		return thematique;
	}

	public void setThematique(String thematique) {
		this.thematique = thematique;
	}
}