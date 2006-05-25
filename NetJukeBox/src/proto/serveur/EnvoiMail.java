package proto.serveur;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.log4j.Logger;

public class EnvoiMail {
	
	/**
	 * Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(EnvoiMail.class);
	/**
	 * Adresse du serveur SMTP
	 */
	private String Host; // = "192.168.0.2";
	
	/**
	 * Port du serveur SMTP
	 */
	private String Port; // = "25";
	
	/**
	 * Adresse mail envoyant un message
	 */
	private String From; // = "mail@netjukebox.no-ip.org";
	
	/**
	 * Destinataire du mail
	 */
	public String to;
	
	/**
	 * Login à retourner
	 */
	public String Login;
	
	/**
	 * Nom de l'usager
	 */
	public String Nom;
	
	/**
	 * Prenom de l'usager
	 */
	public String Prenom;
	
	/**
	 * Mot de passe de l'utilisateur
	 */
	public String Pwd;
	
	/**
	 * Pays de l'usager
	 */
	public String Pays;

	/**
	 * Envoi un mail à l'usager venant de s'inscrire au NetJukeBox
	 * @param Login
	 * @param Nom
	 * @param Prenom
	 * @param Mail
	 * @param Pwd
	 * @param Pays
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public EnvoiMail(String host, String port, String from, String login, String nom, String prenom, String mail, String pwd, String pays) throws AddressException, MessagingException {
		logger.debug("Démarrage : EnvoiMail");			
		
		this.Host = host;
		this.Port = port;
		this.From = from;
		this.Login = login;
		this.Nom = nom;
		this.Prenom = prenom;
		this.to = mail;
		this.Pwd = pwd;
		this.Pays = pays;
			
		//Get system properties
		Properties props = System.getProperties();
		//Setup mail server
		props.put("mail.smtp.host", Host);
		props.put("mail.smtp.port", Port); 
		//Get session
		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(true);
		//Define message
		MimeMessage message = new MimeMessage(session);
		//Set the from address
		message.setFrom(new InternetAddress(From));
		//Set the to address
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		//Set the subject
		message.setSubject("Inscription au NetJukeBox");
		//Set the content
		message.setText("Confirmation d'inscription au NETJUKEBOX :" + '\n' +
						'\n' +
						"Votre login = " + Login + '\n' +
						"Votre  Nom = " + Nom + '\n' +
						"Votre Prenom = " + Prenom + '\n' +
						"Votre adresse Mail = " + to + '\n' +
						"Votre mot de passe = " + Pwd +  '\n' +
						"Votre Pays = " + Pays);
		//Send message
		Transport.send(message);
		logger.debug("Démarrage : EnvoiMail");
	}
}