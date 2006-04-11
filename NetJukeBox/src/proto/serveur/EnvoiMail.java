package proto.serveur;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EnvoiMail {	
	static String host = "smtp.serveur.fr";
	static String from = "prenom.nom@mail.fr";
	public String to;
	public String Login;
	public String Nom;
	public String Prenom;
	public String Pwd;
	public String Pays;

	public EnvoiMail(String Login, String Nom, String Prenom, String Mail, String Pwd, String Pays) throws AddressException, MessagingException {
		this.Login = Login;
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
		message.setText("Votre login = " + Login + 
						" Votre  Nom = " + Nom +
						" Votre Prenom = " + Prenom +
						" Votre adresse Mail = " + to +
						" Votre mot de passe = " + Pwd + 
						" Votre Pays = " + Pays);
		//Send message
		Transport.send(message);
	}

	public static void main (String[] args) throws AddressException, MessagingException {
		new EnvoiMail("Login1", "Nom1" ,"Prenom1" , "adresse@mail.com", "MotDePasse", "Pays");
		System.out.println("Mail envoyé");
	}
}