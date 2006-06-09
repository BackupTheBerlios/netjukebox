package action;

import java.io.File;
import java.util.prefs.Preferences;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.ini4j.IniFile;
import plugin.XMLClient;
import form.pwdPerduForm;

public class pwdPerduAction extends Action {

	/**
	 * Chemin du fichier d'initialisation
	 */
	private String filename = "/home/admindg/Workspace/MVC/WEB-INF/src/plugin/client.ini";
	
	/**
	 * Connection
	 */
	private Object clientXML;
	
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
		
		pwdPerduForm utilForm = (pwdPerduForm)form;

		String login = utilForm.getLogin();
		
		response.setContentType("text/html");
		
		sessionLogin = "anonymous";
		String pwd = "anonymous";
		
		Preferences prefs = new IniFile(new File(filename));
		String port = prefs.node("serveur").get("port", null);
		String ip = prefs.node("serveur").get("ip", null);
		
		clientXML = XMLClient.getInstance(ip, port);
		((XMLClient) clientXML).connexion(sessionLogin, pwd);
		
		boolean envoi = ((XMLClient) clientXML).recherchepwd(sessionLogin, login);
		
		if (envoi) {			
			return mapping.findForward("ok");
		} else {
			String erreur = "WARNING: Login incorrect";
			
			session.setAttribute("Resultat" , erreur);			
			return mapping.findForward("failed");
		}	
	}
}