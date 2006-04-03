package serveur2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpc;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

public class Gestionnaire {

	private XmlRpcClient streaming2;
	private int port;
	private String adresseStreaming;

	public Gestionnaire(String adresseStreaming, int port) {
		this.adresseStreaming = adresseStreaming;
		this.port = port;
		try {
			// Utilise lengthpilote Sax d'Apache Xerces
			XmlRpc.setDriver("org.apache.xerces.parsers.SAXParser");

			// Creation du client & identification du serveur
			this.streaming2 = new XmlRpcClient("http://"+adresseStreaming+":"+port);
		} catch (ClassNotFoundException e) {
			System.out.println("Impossible de localiser le pilote Sax");
		} catch (MalformedURLException e) {
			System.out.println("URL non conforme au format du serveur XML-RPC");
		}

	}

	public String ajouterCanal(String nomCanal) {
		Vector params = new Vector();
		params.addElement(nomCanal);
		System.out.println("[Client] Nouveau canal "+nomCanal);
		try {
			String result = (String) this.streaming2.execute(
					"Gestionnaire.ajouterCanal", params);
			System.out.println("Réponse du serveur : " + result);
			return result;
		} catch (MalformedURLException e) {
			System.out.println("URL non conforme au format du serveur XML-RPC");
		} catch (IOException e) {
			System.out.println("IO Exception: " + e.getMessage());
		} catch (XmlRpcException e) {
			System.out.println("Exception E/S : " + e.getMessage());
		}

		return "[Serveur] Canal créé";
	}
	
	public String ajouterAuditeur(String canal, String ip) {
		Vector params = new Vector();
		params.addElement(canal);
		params.addElement(ip);
		System.out.println("[Client] Nouveau auditeur "+ip);
		try {
			String result = (String) this.streaming2.execute(
					"Gestionnaire.ajouterAuditeur", params);
			System.out.println("Réponse du serveur : " + result);
			return result;
		} catch (MalformedURLException e) {
			System.out.println("URL non conforme au format du serveur XML-RPC");
		} catch (IOException e) {
			System.out.println("IO Exception: " + e.getMessage());
		} catch (XmlRpcException e) {
			System.out.println("Exception E/S : " + e.getMessage());
		}

		return "[Serveur] Auditeur ajouté";
	}
	
	public String diffuserDocument(String canal, String filename) {
		Vector params = new Vector();
		params.addElement(canal);
		params.addElement(filename);
		System.out.println("[Client] Diffusion document "+filename);
		try {
			String result = (String) this.streaming2.execute(
					"Gestionnaire.startDiffusionDocument", params);
			System.out.println("Réponse du serveur : " + result);
			return result;
		} catch (MalformedURLException e) {
			System.out.println("URL non conforme au format du serveur XML-RPC");
		} catch (IOException e) {
			System.out.println("IO Exception: " + e.getMessage());
		} catch (XmlRpcException e) {
			System.out.println("Exception E/S : " + e.getMessage());
		}

		return "[Serveur] Diffusion du document";
	}
}