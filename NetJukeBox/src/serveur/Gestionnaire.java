package serveur;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpc;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

public class Gestionnaire {

	private XmlRpcClient streaming;
	private int port;
	private String adresse;

	public Gestionnaire(String adresse, int port) {
		this.adresse = adresse;
		this.port = port;
		try {
			// Utilise lengthpilote Sax d'Apache Xerces
			XmlRpc.setDriver("org.apache.xerces.parsers.SAXParser");

			// Creation du client & identification du serveur
			this.streaming = new XmlRpcClient("http://"+adresse+":"+port);
		} catch (ClassNotFoundException e) {
			System.out.println("Impossible de localiser le pilote Sax");
		} catch (MalformedURLException e) {
			System.out.println("URL non conforme au format du serveur XML-RPC");
		}

	}

	/**
	 * Lancer la diffusion d'un document
	 * @param filename
	 * @return
	 */
	public String startDiffusionDocument(String filename, String adresse) {
		Vector params = new Vector();
		params.addElement(filename);
		params.addElement(adresse);
		System.out.println("[Client] Nouveau client "+adresse);
		System.out.println("[Client] Demande de diffusion du document "+filename);
		try {
			String result = (String) this.streaming.execute(
					"Gestionnaire.startDiffusionDocument", params);
			System.out.println("Réponse du serveur : " + result);
		} catch (MalformedURLException e) {
			System.out.println("URL non conforme au format du serveur XML-RPC");
		} catch (IOException e) {
			System.out.println("IO Exception: " + e.getMessage());
		} catch (XmlRpcException e) {
			System.out.println("Exception E/S : " + e.getMessage());
		}

		return "[Serveur] Diffusion";
	}

	/**
	 * Stopper la diffusion d'un document
	 * @param filename
	 * @return
	 */
	public boolean stopDiffusionDocument(String filename, String adresse) {
		Vector params = new Vector();
		params.addElement(filename);
		params.addElement(adresse);
		try {
			String result = (String) this.streaming.execute(
					"Gestionnaire.stopDiffusionDocument", params);
			System.out.println("Réponse du serveur : " + result);
		} catch (MalformedURLException e) {
			System.out.println("URL non conforme au format du serveur XML-RPC");
		} catch (IOException e) {
			System.out.println("IO Exception: " + e.getMessage());
		} catch (XmlRpcException e) {
			System.out.println("Exception E/S : " + e.getMessage());
		}

		return true;
	}
	
	/*
	 * public String sayHello(String name) { return "Bonjour " + name; }
	 */
}