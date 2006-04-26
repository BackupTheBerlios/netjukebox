package proto.serveur;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EnvoiMail {
	/**
	 * Adresse su serveur SMTP
	 */
	static String host = "localhost";
	
	/**
	 * Adresse mail envoyant un message
	 */
	static String from = "mail@netjukebox.no-ip.org";
	
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
	public EnvoiMail(String Login, String Nom, String Prenom, String Mail, String Pwd, String Pays) throws AddressException, MessagingException {
		this.Nom = Nom;
		this.Prenom = Prenom;
		this.to = Mail;
		this.Pwd = Pwd;
		this.Pays = Pays;
			
		//Get system properties
		Properties props = System.getProperties();
		//Setup mail server
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "25"); 
		//Get session
		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(true);
		//Define message
		MimeMessage message = new MimeMessage(session);
		//Set the from address
		message.setFrom(new InternetAddress(from));
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
	}
}