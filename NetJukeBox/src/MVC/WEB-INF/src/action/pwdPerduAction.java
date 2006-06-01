package action;

import java.io.File;
import java.net.InetAddress;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.ini4j.IniFile;
import plugin.XMLClient;
import form.NewCanalForm;

public class pwdPerduAction extends Action {

	/**
	 * Chemin du fichier d'initialisation
	 */
	private String filename = "/home/admindg/Workspace/MVC/WEB-INF/src/plugin/client.ini";
	
	/**
	 * Client XMLRPC
	 */
	private XMLClient newClient = null;
	
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
		
		NewCanalForm canalForm = (NewCanalForm)form;

		String login = canalForm.getNom();
		
		response.setContentType("text/html");
		
		clientXML = initializeXML();
		boolean envoi = ((XMLClient) clientXML).recherchepwd(login);
		
		if (envoi) {			
			return mapping.findForward("ok");
		} else {
			String erreur = "WARNING: Login incorrect";
			
			session.setAttribute("Resultat" , erreur);			
			return mapping.findForward("failed");
		}	
	}
	
	/**
	 * Initialisation du client XMLRPC
	 * @return Object
	 */
	private Object initializeXML() {
		
		//Si le client XML n'est pas déjà initialisé
		if (newClient == null) {

			try {
				Preferences prefs = new IniFile(new File(filename));
				String port = prefs.node("serveur").get("port", null);
				String ip = prefs.node("serveur").get("ip", null);
				
				//On initialise le client
				newClient = new XMLClient(ip, port);
			
				try {
					//On essaye de se connecter au serveur XML
					if (newClient.testConnectXML(InetAddress.getLocalHost().getHostAddress())) {
						System.err.println("INFO: Serveur XML contacté avec succès !");
						return newClient;
					}
					else System.err.println("WARNING: Serveur XML injoignable !");
					return false;
				} catch (Exception e) {
					System.err.println("ERREUR: "+e);
					return false;
				}
			} catch (BackingStoreException e1) {
				e1.printStackTrace();
				return false;
			}
		//Sinon, déjà initialisé
		} else {
			System.err.println("WARNING: Client XML déjà initialisé !");
			return false;
		}
	}
}