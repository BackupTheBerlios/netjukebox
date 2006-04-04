package dominique1.serveur;

import org.apache.xmlrpc.*;

public class Serveur {
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: java serveur [adresseStreaming] [portClient] [portStreaming]");
			System.exit(-1);
		}
		try {
			// Utilise le pilote Sax d'Apache Xerces
			XmlRpc.setDriver("org.apache.xerces.parsers.SAXParser");
			// Démarre le serveur
			System.out.println("Démarrage du serveur XML-RPC...");
			//Creation du serveur xmlrpc
			WebServer server = new WebServer(Integer.parseInt(args[1]));
			// Enregistre la classe du gestionnaire
			server.addHandler("Gestionnaire", new Gestionnaire());
			//server.addHandler("Systeme", new Systeme());
			System.out.println("Adresse serveur de streaming : " + args[0]);
			System.out.println("Port du serveur XMLRPC : " + args[1]);
			System.out.println("Port du serveur de streaming : " + args[2]);
			
			System.out.println("En attente de requête ...");

			server.start();
		} catch (ClassNotFoundException e) {
			System.out.println("Impossible de localiser le pilote SAX");
		} catch (Exception e) {
			System.out.println("Impossible de démarrer le serveur : "+ e.getMessage());
		}
	}
}