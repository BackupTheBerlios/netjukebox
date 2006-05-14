package proto.serveur;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
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
	 *  Cr�e le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(Serveur.class);
	
	 /**
	 * Point d'entr�e du serveur
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		logger.debug("D�marrage: main");
		
		//Fichier de configuration de la journalisation
		PropertyConfigurator.configure("src/proto/serveur/log4j.prop");
		//PropertyConfigurator.configure("/home/netjukebox/Workspace/NetJukeBox/proto/serveur/log4j.prop");
		//PropertyConfigurator.configure("C:/Documents and Settings/Marie Rubini/Mes documents/workspace/NetJukeBox/proto/serveur/log4j.prop");
		
		//USAGE : java Serveur [filename.ini]
		
		//Fichier d'initialisation par d�faut (si pas de param�tres)
		String filename = args.length > 0 ? args[0] : "src/proto/serveur/serveur.ini";
		//String filename = args.length > 0 ? args[0] : "/home/netjukebox/Workspace/NetJukeBox/proto/serveur/serveur.ini";
		Preferences prefs = new IniFile(new File(filename));

		try {
			// Utilise le pilote Sax d'Apache Xerces
			XmlRpc.setDriver("org.apache.xerces.parsers.SAXParser");
			
			// D�marre le serveur
			logger.info("D�marrage du serveur XML-RPC...");
			
			//Creation du serveur xmlrpc
			WebServer server = new WebServer(Integer.parseInt(prefs.node("xmlrpc").get("port", null)));
			
			// Enregistre la classe du gestionnaire
			server.addHandler("Systeme", new Systeme(prefs));
			
			//Trace dans la console
			logger.info("Port du serveur XMLRPC : " + prefs.node("xmlrpc").get("port", null));
			logger.info("Adresse serveur de streaming : " + prefs.node("streaming").get("ip", null));
			logger.info("Port du serveur de streaming : " + prefs.node("streaming").get("port", null));
			
			logger.info("En attente de requ�te ...");

			server.start();
		} catch (ClassNotFoundException e) {
			logger.fatal("Arr�t: main | Impossible de localiser le pilote SAX");
		} /*catch (Exception e) {
			System.out.println("Impossible de d�marrer le serveur : "+ e.getMessage());
		}*/
		logger.debug("Arr�t: main");
	}
}