//Arguments : 192.168.0.2 10000 ==> @ ip et port du serveur xmlrpc
package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.net.InetAddress;
import java.net.MalformedURLException;

import org.apache.xmlrpc.*;

public class Client {
	public static void main(String args[]) {
		try {
			// Utilise lengthpilote Sax d'Apache Xerces
			XmlRpc.setDriver("org.apache.xerces.parsers.SAXParser");
			// Creation du client & identification du serveur
			XmlRpcClient client = new XmlRpcClient("http://55.24.1.51:10000");
			// Création de la requête
			Vector params = new Vector();
			
			//Connexion au serveur xmlrpc
			System.out.println("Veuillez saisir votre Login");
			String Login, Passwd;
			BufferedReader entree = new BufferedReader(new InputStreamReader(System.in));
			Login = entree.readLine();
			//System.out.println("login : " + Login);
			params.addElement(Login);
			System.out.println("Veuillez saisir votre mot de passe");
			entree = new BufferedReader(new InputStreamReader(System.in));
			Passwd = entree.readLine();
			//System.out.println("Pwd : " + Passwd);
			params.addElement(Passwd);
			String result = (String)client.execute("Gestionnaire.Connexion", params);
			System.out.println("Réponse du serveur : " + result);
			
				
			
			
			
			/**
			//Ecouter un programme			 
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
			*/
			
			
			
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