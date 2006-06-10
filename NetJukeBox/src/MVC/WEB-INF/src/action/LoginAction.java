package action;

import java.io.File;
//import java.net.InetAddress;
//import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.servlet.http.*;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ini4j.IniFile;
import plugin.XMLClient;
import form.LoginForm;

public class LoginAction extends Action {
	
	/**
	 * Etat de connection
	 */
	private boolean etatConnecte = false;
	
	/**
	 * Chemin du fichier d'initialisation
	 */
	private String filename = "/home/admindg/Workspace/MVC/WEB-INF/src/plugin/client.ini";
	
	/**
	 * Client XMLRPC
	 */
	//private XMLClient newClient = null;
	
	/**
	 * Connection
	 */
	private Object clientXML;	

	// --------------------------------------------------------- Instance Variables
	// --------------------------------------------------------- Methods
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
		
		LoginForm loginForm = (LoginForm)form;
		String login = loginForm.getLogin();
		String password = loginForm.getPass();
		
		//clientXML = initializeXML();
		
		Preferences prefs = new IniFile(new File(filename));
		String port = prefs.node("serveur").get("port", null);
		String ip = prefs.node("serveur").get("ip", null);
		
		clientXML = XMLClient.getInstance(ip, port);
		etatConnecte = ((XMLClient) clientXML).connexion(login, password);
		HttpSession session = request.getSession(true);
		System.out.println(etatConnecte);
		
		if (etatConnecte) {
			//Création d'une session de connexion
			session.setAttribute("login", login);
			
			//Redirection vers le sommaire
			String ipclient = request.getRemoteAddr();
			System.err.println(ipclient);
			
			return mapping.findForward("ok");    
		} else {
			session.invalidate();
			return mapping.findForward("failed");
		}		
	}
}