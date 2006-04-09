package Serveur;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpc;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

public class Gestionnaire {
	String Login;
	String Pwd;
	
	public String Connexion(String Login, String Pwd) {        
		this.Login = Login;
		this.Pwd = Pwd;
		if (Login != "toto" && Pwd != "toto") {
			String result = "Connexion refusée";
			return result;
		}
		else {
			String result = "Connexion établie";
			return result;
		} 
    }
	
	String portcanal;
	String adresse;
	public String Ecouter(String ipclient, String portclient) {
		this.adresse = ipclient;
		this.portcanal = portclient;
		String result = adresse+":"+portcanal;
		return result;
	}
	
	
	
	
}