package action;

import java.util.Dictionary;
import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.infoForm;

public class infoDocAction extends Action {

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
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping,	ActionForm form,
		HttpServletRequest request,	HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		clientXML = (XMLClient) session.getAttribute("client");
		sessionLogin = (String) session.getAttribute("login");
		
		infoForm DocForm = (infoForm)form;

		String id = DocForm.getId();
				
		response.setContentType("text/html");
		
		Dictionary dDoc = clientXML.infoDocument(sessionLogin, id);
		Vector vDoc = new Vector();
		
		if (dDoc!=null) {
			vDoc.add(dDoc);
			
			session.setAttribute("Resultat" , vDoc);
			return mapping.findForward("ok");
		} else {
			String erreur = "WARNING: Aucun document disponible";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}
	}
}