package action;

import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.infoForm;

public class modifierContratAction extends Action {

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
		clientXML = (XMLClient) session.getAttribute("client");
		sessionLogin = (String) session.getAttribute("login");
		
		infoForm contratForm = (infoForm)form;

		String id = contratForm.getId();
		
		response.setContentType("text/html");
		
		Vector vContrat = clientXML.rechercherContrat(sessionLogin, id, "", "", "", "", "", "", "", "", "", "");
		
		if (vContrat!=null) {
			session.setAttribute("Resultat" , vContrat);
			return mapping.findForward("ok");
		} else {
			String erreur = "WARNING: Aucun contrat disponible";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}
	}
}