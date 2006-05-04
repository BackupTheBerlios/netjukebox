package proto.serveur;

import java.io.File;
import java.util.prefs.Preferences;
import org.apache.xmlrpc.WebServer;
import org.apache.xmlrpc.XmlRpc;
import org.ini4j.IniFile;

/**
 * Classe d'initialisation du serveur
 */
public class Serveur {
	
	/**
	 * Point d'entrée du serveur
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		//USAGE : java Serveur [filename.ini]
		
		//Fichier d'initialisation par défaut (si pas de paramètres)
		String filename = args.length > 0 ? args[0] : "src/proto/serveur/serveur.ini";
		//String filename = args.length > 0 ? args[0] : "/home/admindg/Workspace/NetJukeBox/proto/serveur/serveur.ini";
		Preferences prefs = new IniFile(new File(filename));
		
		try {
			// Utilise le pilote Sax d'Apache Xerces
			XmlRpc.setDriver("org.apache.xerces.parsers.SAXParser");
			
			// Démarre le serveur
			System.out.println("Démarrage du serveur XML-RPC...");
			
			//Creation du serveur xmlrpc
			WebServer server = new WebServer(Integer.parseInt(prefs.node("xmlrpc").get("port", null)));
			
			// Enregistre la classe du gestionnaire
			server.addHandler("Systeme", new Systeme(prefs));
			
			//Trace dans la console
			System.out.println("Port du serveur XMLRPC : " + prefs.node("xmlrpc").get("port", null));
			System.out.println("Adresse serveur de streaming : " + prefs.node("streaming").get("ip", null));
			System.out.println("Port du serveur de streaming : " + prefs.node("streaming").get("port", null));
			
			System.out.println("En attente de requête ...");

			server.start();
		} catch (ClassNotFoundException e) {
			System.out.println("Impossible de localiser le pilote SAX");
		} /*catch (Exception e) {
			System.out.println("Impossible de démarrer le serveur : "+ e.getMessage());
		}*/
	}
}