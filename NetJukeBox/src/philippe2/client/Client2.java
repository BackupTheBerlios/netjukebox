package philippe2.client;

import java.io.IOException;
import java.util.Vector;
import java.net.InetAddress;
import java.net.MalformedURLException;

import org.apache.xmlrpc.*;

public class Client2 {

	public static void main(String args[]) {
		if (args.length < 1) {
			System.out.println("Usage: java client2 [adresseServeur] [portServeur] [filename]");
			System.exit(-1);
		}

		try {
			// Utilise lengthpilote Sax d'Apache Xerces
			XmlRpc.setDriver("org.apache.xerces.parsers.SAXParser");

			// Creation du client & identification du serveur
			XmlRpcClient client = new XmlRpcClient("http://"+args[0]+":"+args[1]);

			//Variables
			Vector params;
			String result;
			
			//Création d'un canal
			params = new Vector();
			params.addElement("classic");
			System.out.println("On crée un canal");
			result = (String)client.execute("Gestionnaire.ajouterCanal", params);
			System.out.println("Réponse du serveur : "+result);
			
			//Ajout d'un auditeur
			params = new Vector();
			params.addElement("classic");
			params.addElement("83.156.70.122");
			System.out.println("On ajoute un auditeur");
			result = (String)client.execute("Gestionnaire.ajouterAuditeur", params);
			System.out.println("Réponse du serveur : "+result);
			
			//Diffusion
			params = new Vector();
			params.addElement("classic");
			params.addElement("file://home/philippe/antares.mp3");
			System.out.println("On diffuse");
			result = (String)client.execute("Gestionnaire.diffuserDocument", params);
			System.out.println("Réponse du serveur : "+result);
			

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