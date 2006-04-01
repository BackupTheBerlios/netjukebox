package streaming2;

import java.io.IOException;
import javax.media.rtp.SendStream;

/**
 * Auditeur d'un flux de diffusion
 * @author Philippe Bollard
 */
public class Auditeur {

	/**
	 * Adresse IP de l'auditeur
	 */
	private String ip;
	
	/**
	 * Flux de diffusion associ� � l'auditeur
	 */
	private SendStream flux=null;
	
	
	/**
	 * Cr�e un auditeur
	 * @param ip
	 */
	public Auditeur(String ip) {
		this.ip = ip;
	}
	
	/**
	 * Attribue le flux de diffusion � l'auditeur
	 * @param flux
	 */
	public void setFlux(SendStream flux) {
		this.flux = flux;
	}
	
	/**
	 * Retourne le flux de diffusion de l'auditeur
	 * @return
	 */
	public SendStream getFlux() {
		return flux;
	}
	
	/**
	 * Retourne l'IP de l'auditeur
	 * @return
	 */
	public String getIp() {
		return ip;
	}
	
	/**
	 * D�marre le flux de diffusion de l'auditeur
	 */
	public void startFlux() {
		try {
			flux.start();
		} catch (IOException e) {
			// TODO Bloc catch auto-g�n�r�
			e.printStackTrace();
		}
	}
	
	/**
	 * Arr�te le flux de diffusion de l'auditeur
	 */
	public void stopFlux() {
		try {
			flux.stop();
		} catch (IOException e) {
			// TODO Bloc catch auto-g�n�r�
			e.printStackTrace();
		}
	}
}
