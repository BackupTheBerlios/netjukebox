package action;

import java.util.Dictionary;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.rechercheContratForm;

public class infoContratAction extends Action {

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
		
		rechercheContratForm contratForm = (rechercheContratForm)form;

		String id = contratForm.getId();
		
		response.setContentType("text/html");
		
		Dictionary dContrat = clientXML.infoContrat(sessionLogin, id);
		
		if (dContrat!=null) {
			session.setAttribute("Resultat" , dContrat);
			return mapping.findForward("ok");
		} else {
			String erreur = "WARNING: Aucun contrat disponible";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}
	}
}