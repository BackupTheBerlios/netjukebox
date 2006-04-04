package serveur;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpc;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

public class Gestionnaire {
	String port;
	String adresse;
	
	public String Gestionnaire(String ipclient, String portclient) {
		this.adresse = ipclient;
		this.port = portclient;
		String result = adresse+":"+port;
		return result;
	}
}