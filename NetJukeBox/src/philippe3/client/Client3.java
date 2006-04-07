package philippe3.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpc;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

public class Client3 {

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
			
			//Paramètres des requêtes
			Vector params;
			String result;
			
			//Création d'un canal
			params = new Vector();
			params.addElement("classic");
			
			System.out.println("Création du canal 'classic'");
			String idCanal = (String)client.execute("Gestionnaire.ajouterCanal", params);
			System.out.println("[Retour] : Canal créé, id: "+idCanal);
			
			//Programmation de document
			Vector medias = new Vector();
			medias.addElement("file://home/philippe/njb/1.mp3");
			medias.addElement("file://home/philippe/njb/2.mp3");
			medias.addElement("file://home/philippe/njb/3.mp3");
			params = new Vector();
			params.addElement(idCanal);
			params.addElement(medias);
			
			System.out.println("Programmation d'une liste de documents pour le canal 'classic'");
			result = (String)client.execute("Gestionnaire.programmer", params);
			System.out.println("[Retour] : "+result);
			
			//Diffusion
			params = new Vector();
			params.addElement(idCanal);
			
			System.out.println("Lancer la diffusion du canal 'classic'");
			result = (String)client.execute("Gestionnaire.diffuser", params);
			System.out.println("[Retour] : "+result);
			
			//Création d'un canal
			params = new Vector();
			params.addElement("jazz");
			
			System.out.println("Création du canal 'jazz'");
			String idCanal2 = (String)client.execute("Gestionnaire.ajouterCanal", params);
			System.out.println("[Retour] : Canal créé, id: "+idCanal2);
			
			//Diffusion
			params = new Vector();
			params.addElement(idCanal2);
			
			System.out.println("Lancer la diffusion du canal 'jazz'");
			result = (String)client.execute("Gestionnaire.diffuser", params);
			System.out.println("[Retour] : "+result);
			

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