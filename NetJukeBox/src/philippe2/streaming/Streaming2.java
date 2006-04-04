package philippe2.streaming;

import org.apache.xmlrpc.*;

public class Streaming2 {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: java streaming [portXMLRPC] [portBase]");
			System.exit(-1);
		}

		try {

			// Utilise le pilote Sax d'Apache Xerces
			XmlRpc.setDriver("org.apache.xerces.parsers.SAXParser");

			// D�marre le serveur
			System.out.println("D�marrage du serveur XML-RPC...");
			WebServer server = new WebServer(Integer.parseInt(args[0]));

			// Enregistre la classe du gestionnaire
			server.addHandler("Gestionnaire", new Gestionnaire(Integer.parseInt(args[1])));
			System.out.println("En attente de requ�te ...");

			server.start();
		} catch (ClassNotFoundException e) {
			System.out.println("Impossible de localiser le pilote SAX");
		} catch (Exception e) {
			System.out.println("Impossible de d�marrer le serveur : "+ e.getMessage());
		}
	}
}
