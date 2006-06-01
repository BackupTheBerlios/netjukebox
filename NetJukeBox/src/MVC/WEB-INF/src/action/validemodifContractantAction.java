package action;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.modifierContractantForm;

public class validemodifContractantAction extends Action {

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
		
		modifierContractantForm contractantForm = (modifierContractantForm)form;

		String id = contractantForm.getId();
		String nom = contractantForm.getNom();
		String adresse = contractantForm.getAdresse();
		String codepostal = contractantForm.getCodePostal();
		String ville = contractantForm.getVille();
		String telephone = contractantForm.getTelephone();
		String fax = contractantForm.getFax();
		String mail = contractantForm.getMail();
		String type = contractantForm.getType();

		response.setContentType("text/html");
		
		boolean modifie = clientXML.modifierContractant(sessionLogin, id, nom, adresse, codepostal, ville, telephone, fax, mail, type);
		
		if (modifie) {
			String result = "INFO: Contractant modifié";
			
			session.setAttribute("Resultat" , result);
			return mapping.findForward("ok");
		} else {
			String erreur = "ERREUR: Contractant non modifié";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}	
	}
}