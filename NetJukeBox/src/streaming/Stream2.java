package streaming;

import javax.media.MediaLocator;

public class Stream2 {

	// Attributs

	// Constructeur
	public Stream2() {
	}

	public void debut() {
		// Entr�e de l'adresse du fichier
		String FichierAdresse = "file://d:/test.avi";

		// Cr�ation du M�diaLocator � partir de ce fichier
		MediaLocator FichierLocator = new MediaLocator(FichierAdresse);

		// Declaration du Processor
		Processor FichierCessor = null;
		try {
			// Creation du Processor � partir du Medialocator
			FichierCessor = Manager.createProcessor(FichierLocator);
		}

		catch (IOException e){
			System.out.println("Erreur : " + e.getMessage());
		}
		catch (NoProcessorException e){
			System.out.println("Erreur : " + e.getMessage());
		}
	}
	
	public Processor configure(Processor p) {

	       //Attendre tant que le Processor n'est pas configur�.
	       while(p.getState() < Processor.Configured) {
	              //Configuration du Processor
	              p.configure();
	       }
	       return p;
	}
	
	public void SetSupportedFormat(Processor p) {

	       //On met la description du contenu de sortie � RAW_RTP
	       //pour limiter les formats support�s

	       ContentDescriptor cd = new ContentDescriptor(ContentDescriptor.RAW_RTP);
	       p.setContentDescriptor(cd);

	       //On obtient les diff�rentes pistes du processor
	       TrackControl track[] = p.getTrackControls();
	       
	       for(int i = 0 ; i < track.length ; i++) {

	              //on obtient les formats support�s pour cette piste
	              Format suppFormats[] = track[i].getSupportedFormats();

	              //Si il y a au moins un format support�
	              //alors on met le premier

	              if(suppFormats.length > 0)  track[i].setFormat(suppFormats[0]);
	              else track[i].setEnabled(false);
	       }
	}
	
	public Processor realize(Processor p) {

	       //Attendre tant que le Processor n'est pas r�alis�.
	       while(p.getState() < Processor.Realized) {
	              //R�alisation du Processor
	              p.realize();
	       }
	       return p;
	}
	
	public void Demarre(Processor p) {
	       //Demarrage du Processor
	       p.start();
	}
	
	public void createDataSink(Processor p) {

	       //Creation du DataSource correspondant au Processor
	       DataSource WebcamSource = p.getDataOutput();

	       //Adresse de Destination
	       String OutputAddress = "rtp://192.168.3.6:22224/video/1";

	       //Creation du MediaLocator pour l'Adresse de destination
	       MediaLocator OutputLocator = new MediaLocator(OutputAddress);

	       try {

	              //Creation du DataSink
	              DataSink OutputSink =Manager.createDataSink(WebcamSource,OutputLocator);

	              //Ouverture du DataSink
	              OutputSink.open();

	              //Demarrage du DataSink
	              OutputSink.start();
	              System.out.println("Started");

	       }
	       catch(IOException e) {
	       }
	       catch(NoDataSinkException e) {
	       }
	}
	
	public void createRTPManager(Processor p) {

	       //Creation du DataSource correspondant au Processor
	       DataSource OutputSource = p.getDataOutput();

	       //Nouvelle Instance d'un RTPManager
	       RTPManager rtpm = RTPManager.newInstance();

	       try {

	              //Cr�ation d'une SessionAddress
	              //correspondant � l'adresse locale

	              SessionAddress localaddr = new SessionAddress(InetAddress.getLocalHost(),40000);

	              //Initialisation du RTPManager
	              //� partir de la SessionAddresse locale

	              rtpm.initialize(localaddr);
	              
	              //Cr�ation d'une SessionAddress
	              // correspondant � l'adresse de destination       

	              SessionAddress destaddr = new SessionAddress(InetAddress.getByName("192.168.3.6"),22224);

	              //Ajout de cette SessionAddress dans le RTPManager
	              rtpm.addTarget(destaddr);

	              //Creation d'un SendStream � partir du DataSource
	              SendStream ss2 = rtpm.createSendStream(OutputSource,0);

	              //Demarrage du SendStream
	              ss2.start();
	              System.out.println("Started");

	       }
	       catch(UnknownHostException e) {
	       }     
	       catch(IOException e) {
	       }
	       catch(InvalidSessionAddressException e) {
	       }

	       catch(UnsupportedFormatException e) {
	       }

	}
	
	public void createRTPManager2(Processor p)

	{

	       //Creation du DataSource correspondant au Processor

	       DataSource OutputSource = p.getDataOutput();

	             

	       //Nouvelle Instance d'un RTPManager

	       RTPManager rtpm = RTPManager.newInstance();

	             

	       try

	       {

	              //Cr�ation d'une SessionAddress

	              //     correspondant � l'adresse locale

	              SessionAddress localaddr = new SessionAddress

	                     (InetAddress.getLocalHost(),40000);

	              rtpm.initialize(localaddr);

	                    

	              //Cr�ation d'une SessionAddress

	              //correspondant � la premi�re adresse de destination

	              SessionAddress destaddr1 = new SessionAddress

	                     (InetAddress.getByName("192.168.3.6"),22224);

	              //Ajout de la premi�re SessionAddress dans le RTPManager

	              rtpm.addTarget(destaddr1);

	              //Creation d'un premier SendStream � partir du DataSource

	              //     Ce SendStream enverra le flux

	              //     � la premi�re adresse de destination

	              SendStream ss = rtpm.createSendStream(OutputSource,0);

	              //Demarrage du premier SendStream

	              ss.start();

	      

	              //Cr�ation d'une SessionAddress

	              //correspondant � la seconde adresse de destination     

	              SessionAddress destaddr2 = new SessionAddress

	                     (InetAddress.getByName("192.168.3.2"),22224);

	              //Ajout de la seconde SessionAddress dans le RTPManager

	              rtpm.addTarget(destaddr2);

	              //Creation d'un second SendStream � partir du DataSource

	              //     Ce SendStream enverra le flux

	              //     � la premi�re adresse de destination

	              SendStream ss2 = rtpm.createSendStream(OutputSource,0);

	              //Demarrage du second SendStream

	              ss2.start();

	                    

	              System.out.println("Started");

	       }

	       catch(UnknownHostException e)

	       {

	       }     

	       catch(IOException e)

	       {

	       }

	       catch(InvalidSessionAddressException e)

	       {

	       }

	       catch(UnsupportedFormatException e)

	       {

	       }

	}
}
