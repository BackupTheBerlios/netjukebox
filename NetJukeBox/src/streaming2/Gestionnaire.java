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

	public String startDiffusionDocument(String nomFlux, String filename, String ipAdress) {
		System.out.println("[Serveur] Nouveau client : "+ipAdress);
		System.out.println("[Serveur] Diffusion du document "+filename);
		Stream stream = new Stream(nomFlux, this.portBase);
		stream.ajouterAuditeur(ipAdress);
		stream.diffuser(filename);
		this.streams.add(stream);
		return "[Streaming] Diffusion sur le port "+(portBase+2);
	}
	/*
	public String startDiffusionDocument2(String filename, String ipAdress) {
		System.out.println("Diffusion du document "+filename);
		Stream2 stream = new Stream2(filename, ipAdress, this.portBase);
		//stream.start();
		this.streams.add(stream);
		return "[Streaming] Diffusion";
	}
	*/
	public boolean stopDiffusionDocument(String filename, String ipAdress) {
		return true;
	}
	
	public boolean stopAll() {
		this.streams.clear();
		return true;
	}
}