import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import plugin.XMLClient;

public class MyHttpSessionListener implements HttpSessionListener {
	
	/**
	 * Client XMLRPC
	 */
	private XMLClient clientXML = null;

	/**
	 * Login de l'utilisateur sur la session
	 */
	private String sessionLogin;
	
	public void sessionCreated(HttpSessionEvent e) {		
		System.err.println("Hello new session");
	}
	
	public void sessionDestroyed(HttpSessionEvent e) {
		
		HttpSession session = e.getSession();
		clientXML = XMLClient.getInstance();
		sessionLogin = (String) session.getAttribute("login");

		boolean etatConnecte = !clientXML.deconnexion(sessionLogin);
		System.err.println("ByeBye old session");
	}	
}
