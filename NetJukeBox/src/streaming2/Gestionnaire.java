package streaming2;

import java.util.*;

public class Gestionnaire {

	//private String ipAdress;
	private int portBase;
	private Vector streams;

	public Gestionnaire(int portBase) {
		this.portBase = portBase;
		this.streams = new Vector();
	}
	
	public String ajouterCanal(String nomCanal) {
		
		System.out.println("[Serveur] Nouveau canal : "+nomCanal);
		
		//On recherche le canal
		int indice=0;
		while (indice<streams.size() && ((Stream)streams.elementAt(indice)).getNomFlux()!=nomCanal) {
			indice++;
		}
		
		//Si canal inexistant, on le crée
		if (indice>0 && indice>=streams.size()) {
			streams.add(new Stream(nomCanal, portBase));
			portBase +=3;
		}
		
		return "[Streaming] Nouveau canal : "+nomCanal;
	}
	
	public String ajouterAuditeur(String nomCanal, String ipAdress) {
		System.out.println("[Serveur] Nouveau client : "+ipAdress);
		
		//On recherche le canal
		int indice=0;
		while (indice<streams.size() && ((Stream)streams.elementAt(indice)).getNomFlux()!=nomCanal) {
			indice++;
		}
		
		//Si canal inexistant, on le crée
		if (streams.size()==0 || indice>=streams.size()) {
			indice=streams.size();
			this.ajouterCanal(nomCanal);
		}
		
		//On récupère le stream
		Stream stream = (Stream)streams.elementAt(indice);
		
		//On ajoute l'auditeur
		stream.ajouterAuditeur(ipAdress);
		
		//On met à jour la collection de stream
		streams.setElementAt(stream, indice);

		return "[Streaming] Nouvel auditeur "+ ipAdress;
	}
	

	public String startDiffusionDocument(String nomFlux, String filename) {
		System.out.println("[Serveur] Diffusion du document "+filename);
		
		//On recherche le canal
		int indice=0;
		while (indice<streams.size() && ((Stream)streams.elementAt(indice)).getNomFlux()!=nomFlux) {
			indice++;
		}
		
		//Si canal inexistant, on le crée
		if (streams.size()==0 || indice>=streams.size()) {
			indice=streams.size();
			this.ajouterCanal(nomFlux);
		}
		
		//On récupère le stream
		Stream stream = (Stream)streams.elementAt(indice);
		
		//On diffuse
		stream.diffuser(filename);
		
		//On met à jour la collection de stream
		streams.setElementAt(stream, indice);
		
		
		return "[Streaming] Diffusion sur le port "+(portBase+2);
	}

	public boolean stopDiffusionDocument(String filename, String ipAdress) {
		return true;
	}
	
	public boolean stopAll() {
		this.streams.clear();
		return true;
	}
}