package proto.client;
//Arguments : 192.168.0.2 10000

import java.io.IOException;
import java.util.Vector;
import java.net.InetAddress;
import java.net.MalformedURLException;

import org.apache.xmlrpc.*;

public class Client {
	public static void main(String args[]) {
		if (args.length < 1) {
			System.out.println("Usage: java client [adresseServeur] [portServeur]");
			System.exit(-1);
		}
		try {
			// Utilise lengthpilote Sax d'Apache Xerces
			XmlRpc.setDriver("org.apache.xerces.parsers.SAXParser");
			// Creation du client & identification du serveur
			XmlRpcClient client = new XmlRpcClient("http://"+args[0]+":"+args[1]);
			// Création de la requête
			Vector params = new Vector();
			// Récupération de l'adresse ip du client
			String AdresseIp = InetAddress.getLocalHost().getHostAddress ();
			params.addElement(AdresseIp);
			String port = "22224";
			params.addElement(port);
			System.out.println(AdresseIp);
			// Adresse la requête et affiche les résultats
			String result = (String)client.execute("Gestionnaire.Gestionnaire", params);
			//System.out.println("Réponse du serveur : " + result);
			//Démarage du client streaming
			new RTPClient(result);
			System.out.println("Client Streaming démarré");
		} catch (ClassNotFoundException e) {
			System.out.println("Impossible de localiser le pilote Sax");
		} catch (MalformedURLException e) {
			System.out.println("URL non conforme au format du serveur XML-RPC");
		} catch (IOException e) {
			System.out.println("IO Exception: " + e.getMessage());
		} catch (XmlRpcException e) {
			System.out.println("Exception E/S : " + e.getMessage());
		}
	}
}