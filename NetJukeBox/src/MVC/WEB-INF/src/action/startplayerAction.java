package action;

import java.util.StringTokenizer;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.infoForm;

public class startplayerAction extends Action {

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
		clientXML = XMLClient.getInstance();
		sessionLogin = (String) session.getAttribute("login");
		
		infoForm playerForm = (infoForm)form;

		String id = playerForm.getId();

		response.setContentType("text/html");
		
		String urlPlayer = clientXML.ecouterCanal(sessionLogin, id);
		
		if (urlPlayer != null) {			
			String result = "INFO: Ecoute du canal lanc�e";

			String ip = null;
			String port = null;
			StringTokenizer st = new StringTokenizer(urlPlayer);
			
			ip = st.nextToken("/");
			
			while (st.hasMoreTokens()) {
				port = st.nextToken("/");
			}
			
			session.setAttribute("audiosession" , ip);
			session.setAttribute("audioport" , port);
			session.setAttribute("Resultat" , result);
			
			return mapping.findForward("ok");
		} else {
			String erreur = "ERREUR: Ecoute du canal non lanc�e";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}
	}
}