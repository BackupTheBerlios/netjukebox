package action;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.modifierUtilisateurForm;

public class validemodifUtilisateurAction extends Action {

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
		String logUtil = (String) session.getAttribute("logUtil");
		
		modifierUtilisateurForm utilForm = (modifierUtilisateurForm)form;

		String newLogUtil = utilForm.getLogin();
		String nom = utilForm.getNom();
		String prenom = utilForm.getPrenom();
		String mail = utilForm.getMail();
		String pays = utilForm.getPays();
		String pass = utilForm.getPass();

		response.setContentType("text/html");
		
		boolean modifie = clientXML.modifierUtilisateur(sessionLogin, logUtil, newLogUtil, pass, nom, prenom, mail, pays);
		
		if (modifie) {
			String result = "INFO: Utilisateur modifié";
			
			session.removeAttribute("logUtil");
			session.setAttribute("Resultat" , result);
			return mapping.findForward("ok");
		} else {
			String erreur = "ERREUR: Utilisateur non modifié";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}	
	}
}