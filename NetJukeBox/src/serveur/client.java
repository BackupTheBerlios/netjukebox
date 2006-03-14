package serveur;

import java.io.IOException;
import java.util.Vector;
import java.net.MalformedURLException;

import org.apache.xmlrpc.*;

public class client {

	public static void main(String args[]) {
		if (args.length < 1) {
			System.out.println("Usage: java Client [votre nom]");
			System.exit(-1);
		}

		try {
			// Utilise lengthpilote Sax d'Apache Xerces
			XmlRpc.setDriver("org.apache.xerces.parsers.SAXParser");

			// Creation du client & identification du serveur
			XmlRpcClient client = new XmlRpcClient("http://localhost:10000");

			// Cr�ation de la requ�te
			Vector params = new Vector();
			params.addElement(args[0]);

			// Adresse la requ�te et affiche les r�sultats
			String result = (String) client.execute("Gestionnaire.sayHello",
					params);
			System.out.println("R�ponse du serveur : " + result);

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