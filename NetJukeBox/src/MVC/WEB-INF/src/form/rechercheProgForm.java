package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class rechercheProgForm extends ActionForm {
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