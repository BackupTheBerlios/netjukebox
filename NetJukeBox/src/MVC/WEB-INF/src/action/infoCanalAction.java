package action;

import java.util.Dictionary;
import java.util.Vector;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.infoForm;

public class infoCanalAction extends Action {

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
		//clientXML = (XMLClient) session.getAttribute("client");
		clientXML = XMLClient.getInstance();
		sessionLogin = (String) session.getAttribute("login");
		
		infoForm canalForm = (infoForm)form;

		String id = canalForm.getId();

		response.setContentType("text/html");
		
		Dictionary dCanaux = clientXML.infoCanal(sessionLogin, id);
		Vector vCanaux = new Vector();
		
		if (dCanaux!=null) {
			vCanaux.add(dCanaux);
			Vector vProg = (Vector)dCanaux.get("programmes");
			
			session.setAttribute("Resultat" , vCanaux);
			session.setAttribute("Programmes" , vProg);
			
			return mapping.findForward("ok");
		} else {
			String erreur = "WARNING: Aucun canal disponible";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}
	}
}