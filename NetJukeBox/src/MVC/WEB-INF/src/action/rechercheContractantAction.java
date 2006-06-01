package action;

import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.rechercheContractantForm;

public class rechercheContractantAction extends Action {

	/**
	 * Client XMLRPC
	 */
	private XMLClient clientXML = null;
	
	/**
	 * Login de l'utilisateur sur la session
	 */
	private String sessionLogin;
	
	/** 
	 * Method execute
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping,	ActionForm form,
		HttpServletRequest request,	HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		//clientXML = (XMLClient) session.getAttribute("client");
		clientXML = XMLClient.getInstance();
		sessionLogin = (String) session.getAttribute("login");
		
		rechercheContractantForm contractantForm = (rechercheContractantForm)form;

		String id = contractantForm.getId();
		String nom = contractantForm.getNom();
		String adresse = contractantForm.getAdresse();
		String codepostal = contractantForm.getCodepostal();
		String ville = contractantForm.getVille();
		String telephone = contractantForm.getTelephone();
		String fax = contractantForm.getFax();
		String mail = contractantForm.getMail();
		String type = contractantForm.getType();

		response.setContentType("text/html");
		
		Vector vContractant = clientXML.rechercherContractant(sessionLogin, id, nom, adresse, 
				codepostal, ville, telephone, fax, mail, type);
		
		if (vContractant!=null && vContractant.size()>0) {
			session.setAttribute("Resultat" , vContractant);
			return mapping.findForward("ok");
		} else {
			String erreur = "WARNING: Aucun contractant disponible";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}
	}
}