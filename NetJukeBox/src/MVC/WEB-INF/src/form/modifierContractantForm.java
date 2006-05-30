package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class modifierContractantForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String id;
	private String nom;
	private String adresse;
	private String codepostal;
	private String ville;
	private String telephone;
	private String fax;
	private String mail;
	private String type;

	// --------------------------------------------------------- Methods
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		id = null;
		nom = null;
		adresse = null;
		codepostal = null;
		ville = null;
		telephone = null;
		fax = null;
		mail = null;
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
		if (nom.length() == 0) {
			ae.add(id, new ActionError("error.nom"));
		}
		if (adresse.length() == 0) {
			ae.add(id, new ActionError("error.adresse"));
		}
		if (codepostal.length() == 0) {
			ae.add(id, new ActionError("error.codepostal"));
		}
		if (ville.length() == 0) {
			ae.add(id, new ActionError("error.ville"));
		}
		if (telephone.length() == 0) {
			ae.add(id, new ActionError("error.telephone"));
		}
		if (fax.length() == 0) {
			ae.add(id, new ActionError("error.fax"));
		}
		if (mail.length() == 0) {
			ae.add(id, new ActionError("error.mail"));
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
	
	public String getNom()	{
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	
	public String getCodepostal() {
		return codepostal;
	}

	public void setCodepostal(String codepostal) {
		this.codepostal = codepostal;
	}
	
	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}
	
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}