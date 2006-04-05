package philippe3.serveur;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpc;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

public class Gestionnaire {

	private int port;

	private String adresse;

	private Vector canaux;

	private String publicite;

	public Gestionnaire(String ipclient, int portclient) {
		this.adresse = ipclient;
		this.port = portclient;
		this.canaux = new Vector();
		this.publicite = "file://home/philippe/njb/pub.mp3";
	}

	public String ajouterCanal(String nom) {
		System.out.println("Création d'un canal : " + nom);
		RTPServer rtp = new RTPServer(adresse, port, nom, publicite);
		this.canaux.addElement(rtp);
		System.out.println("Canal créé, id : "+canaux.size());
		return Integer.toString(canaux.size()-1);
	}

	public String programmer(String id, Vector medias) {
		int idCanal = Integer.valueOf(id).intValue();
		System.out.println("Progammation d'une liste de medias sur le canal "
				+ idCanal);
		((RTPServer) canaux.elementAt(idCanal)).programmer(medias);
		System.out.println("Canal programmé");
		return "Canal programmé";
	}

	public String diffuser(String id) {
		int idCanal = Integer.valueOf(id).intValue();
		System.out.println("Diffusion du canal : " + idCanal);
		((RTPServer) canaux.elementAt(idCanal)).diffuser();
		System.out.println("Le canal diffuse");
		return "Canal diffuse";
	}

}