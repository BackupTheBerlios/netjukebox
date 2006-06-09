package action;

import java.io.File;
import java.util.prefs.Preferences;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.ini4j.IniFile;

import form.NewUserForm;
import plugin.XMLClient;

public class NewUserAction extends Action {
	
	/**
	 * Client XMLRPC
	 */
	private XMLClient clientXML = null;
	
	/**
	 * Login de l'utilisateur sur la session
	 */
	private String sessionLogin;
	
	/**
	 * Chemin du fichier d'initialisation
	 */
	private String filename = "/home/admindg/Workspace/MVC/WEB-INF/src/plugin/client.ini";
	
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
		
		if (sessionLogin==null){
			sessionLogin = "anonymous";
			String pwd = "anonymous";
			
			Preferences prefs = new IniFile(new File(filename));
			String port = prefs.node("serveur").get("port", null);
			String ip = prefs.node("serveur").get("ip", null);
			
			clientXML = XMLClient.getInstance(ip, port);
			((XMLClient) clientXML).connexion(sessionLogin, pwd);
		}
		
		NewUserForm userForm = (NewUserForm)form;
		
		String login = userForm.getLogin();
		String nom = userForm.getNom();
		String prenom = userForm.getPrenom();
		String password = userForm.getPass();
		String mail = userForm.getMail();
		String pays = userForm.getPays();
		
		response.setContentType("text/html");
		
		boolean cree = clientXML.inscription(sessionLogin, login, password, mail, nom, prenom, pays);
		
		if (cree) {
			String result = "INFO: Utilisateur créé";
			
			session.setAttribute("Resultat" , result);
			return mapping.findForward("ok");
		} else {
			String erreur = "ERREUR: Utilisateur non créé";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}	
		
		
	}
}
