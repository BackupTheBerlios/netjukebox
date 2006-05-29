package action;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.ajoutsuppressionForm;

public class retirerdoccontratAction extends Action {

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
		
		ajoutsuppressionForm ajoutForm = (ajoutsuppressionForm)form;

		String iddoc = ajoutForm.getId1();
		String idcontrat = ajoutForm.getId2();

		response.setContentType("text/html");
		
		boolean retrait = clientXML.retirerDocumentContrat(sessionLogin, iddoc, idcontrat);
		
		if (retrait) {
			String result = "INFO: Document retir� du contrat";
			
			session.setAttribute("Resultat" , result);
			return mapping.findForward("ok");
		} else {
			String erreur = "ERREUR: Document non retir� du contrat";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}	
	}
}