package Sample.testRTP;
// Michael Smith
// RTP Player v 2.0
// import necessary packages
import javax.swing.* ;
import java.awt.event.* ;
import java.io.*;
import java.awt.*;
import java.awt.Container ;
import java.net.*;
import java.awt.event.*;
import java.util.Vector;
import javax.media.*;
import javax.media.rtp.*;
import javax.media.rtp.event.*;
import javax.media.rtp.rtcp.*;
import javax.media.protocol.*;
import javax.media.protocol.DataSource;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.Format;
import javax.media.format.FormatChangeEvent;
import javax.media.control.BufferControl;

public class RTPPlayer2 implements ReceiveStreamListener, SessionListener,
				   ControllerListener
{
	String sessions[] = {"one"} ;
	RTPManager rtpManager[] = null ;
	Vector playerWindows = null ;
	Object dataSync = new Object() ;
	boolean dataReceived = false ;
	static String guiAddress ;
	static int guiPort ;


	// Constructor
	public RTPPlayer2()
	{

		Address address = new Address() ;
		
	}

	class Address extends JFrame
	{
		public Address() {
		super( "RTP Player" ) ;
		JPanel address, field ;
		final JLabel ipLabel, port ;
		final JTextField firstThree, secondThree, thirdThree, fourthThree, portField ;
		JButton OK ;

		Container c = getContentPane() ;
		c.setLayout( new FlowLayout() ) ;
		address = new JPanel() ;
		address.setLayout( new BorderLayout() ) ;

		field = new JPanel() ;
		field.setLayout( new FlowLayout() ) ;

		ipLabel = new JLabel( "IP Address: " ) ;
		port = new JLabel( "port: " ) ;
		firstThree = new JTextField( 5 ) ;
		secondThree = new JTextField( 5 ) ;
		thirdThree = new JTextField( 5 ) ;
		fourthThree = new JTextField( 5 ) ;
		portField = new JTextField( 5 ) ;
		OK = new JButton( "OK" ) ;
		OK.addActionListener( new ActionListener() {
				      public void actionPerformed( ActionEvent e )
				      {
				       	String first, second, three, four, ip, portS ;
					first = firstThree.getText() ;
					second = secondThree.getText() ;
					three = thirdThree.getText() ;
					four = fourthThree.getText() ;
					portS = portField.getText() ;
					ip = new String( first +"." +second +"." +three +"." +four ) ;

					guiPort = Integer.parseInt( portS ) ;
					guiAddress = ip ;
					System.out.println( "IP address: " +guiAddress ) ; } }	) ;
		field.add( ipLabel ) ;
		field.add( firstThree ) ;
		field.add( secondThree ) ;
		field.add( thirdThree ) ;
		field.add( fourthThree ) ;
		field.add( port ) ;
		field.add( portField ) ;

		address.add( OK, BorderLayout.SOUTH ) ;
		address.add( field, BorderLayout.CENTER ) ;
		c.add( address ) ;
		System.out.println( "In Address constructor, ready to show" ) ;
		setSize( 450, 100 ) ;
		show() ;

		}
	}

	public boolean create()
	{
		if( guiAddress != null) {
		System.out.println( "guiAddress wasn't null" ) ;
		System.out.println( "guiAddress is: " +guiAddress ) ;
		try {
			InetAddress ipAddr ;
			// Session Address for client, me
			SessionAddress clientAddress = new SessionAddress() ;
			// Session Address for server, RTPServer2
			SessionAddress serverAddress ;

			//Address address ;
			// For each instance, create an RTP Manager

			rtpManager = new RTPManager[ sessions.length ] ;
			// Make playerwindows
			playerWindows = new Vector() ;

			// Open RTP sessions
			for( int i = 0 ; i < sessions.length ; i++ )
			{


				// Create new instances of RTP Managers and add stream listeners to handle
				// events

				rtpManager[i] = ( RTPManager ) RTPManager.newInstance() ;
				rtpManager[i].addSessionListener( this ) ;
				rtpManager[i].addReceiveStreamListener( this ) ;
				System.out.println( "Added receive stream" ) ;
				// Get IP address of server from input string
				ipAddr = InetAddress.getByName( guiAddress ) ;
				System.out.println( "ipAddr: " +ipAddr.toString() ) ;
				System.out.println( "local host: " + InetAddress.getLocalHost().toString() ) ;
				clientAddress = new SessionAddress( InetAddress.getLocalHost(), guiPort ) ;
				serverAddress = new SessionAddress( ipAddr, guiPort ) ;
				System.out.println( "Session Addresses created" ) ;
				rtpManager[i].initialize( clientAddress ) ;
				System.out.println( "Session created for " +guiAddress ) ;
				// Create and associate the buffers for the data that is received
				BufferControl buffer = ( BufferControl ) rtpManager[i].getControl( "javax.media.control.BufferControl" );
				if( buffer != null )
					buffer.setBufferLength( 400 ) ;
				rtpManager[i].addTarget( serverAddress ) ;
			}

		} catch ( Exception e ) {
			System.out.println( "Error: Could not create RTP Session because " +e.getMessage() ) ;
			return false ;
		}

		// Wait for data to arrive, from AVReceive

		long then = System.currentTimeMillis();
		long waitingPeriod = 30000;  // wait for a maximum of 30 secs.

		try{
		    synchronized (dataSync) {
			while (!dataReceived &&
				( System.currentTimeMillis() - then < waitingPeriod ) ) {
			    if (!dataReceived)
				System.err.println("  - Waiting for RTP data to arrive...");
			    dataSync.wait(1000);
				}
			}
		} catch (Exception e) {}

		if (!dataReceived) {
			System.err.println("No RTP data was received.");
			quit();
			return false;
		}

			return true;
		} return false ;} 


		public boolean finished()
		{
			return playerWindows.size() == 0 ;
		}

		public void quit()
		{
			// When the session is over, we want to close all the
			// players and sessions
			for( int i = 0 ; i < playerWindows.size() ; i++ )
			{
				try {
					( ( PlayerWindow ) playerWindows.elementAt( i ) ).close() ;
				} catch ( Exception e ) {}
			}

			playerWindows.removeAllElements() ;

			for( int i = 0 ; i < rtpManager.length ; i++ )
			{
				rtpManager[i].removeTargets( "Session over" ) ;
				rtpManager[i].dispose() ;
				rtpManager[i] = null ;
			}
		}
		// Methods to find a specified Player or Stream
		PlayerWindow find( Player p )
		{
			for( int i = 0 ; i < playerWindows.size() ; i++ )
			{
				PlayerWindow pWindow = ( PlayerWindow ) playerWindows.elementAt( i ) ;
					return pWindow ;
			}
			return null ;

		}

		PlayerWindow find( ReceiveStream stream )
		{
			for( int i = 0 ; i < playerWindows.size() ; i++ )
			{
				PlayerWindow pWindow = (PlayerWindow)playerWindows.elementAt(i);
	    			if (pWindow.stream == stream)
				return pWindow;
			}
			return null;
    		}

		// The SessionListener update method, it listens for new joining users
		public synchronized void update( SessionEvent event )
		{
			if( event instanceof NewParticipantEvent )
			{
				Participant participant = ( ( NewParticipantEvent) event ).getParticipant() ;
				System.out.println( "Participant " +participant.getCNAME() +" joined" ) ;
			}
		}

		public synchronized void update( ReceiveStreamEvent event )
		{
			RTPManager manager = ( RTPManager ) event.getSource() ;
			Participant participant = event.getParticipant() ;
			ReceiveStream stream = event.getReceiveStream() ;

			if( event instanceof RemotePayloadChangeEvent )
			{
				System.out.println( "Error: Payload Change" );
				System.exit( -1 ) ;

			// If new stream, create player for that stream and associate datasource
			} else if ( event instanceof NewReceiveStreamEvent )
			  {
				try {
					stream = ( ( NewReceiveStreamEvent ) event ).getReceiveStream() ;
					DataSource dataSource = stream.getDataSource() ;

					// Get Formats of the New Stream
					RTPControl control = ( RTPControl ) dataSource.getControl( "javax.media.rtp.RTPControl" ) ;
					if( control != null ) {
						System.out.println( "New RTP Stream: " +control.getFormat() ) ;
					} else {
						System.out.println( "New Stream of unknown format" ) ;
					}

					if( participant == null )
					{
						System.out.println( "User of RTP Session unknown" );
					} else {
						System.out.println( "User of RTP Session: " +participant.getCNAME() ) ;
					}

					// Now that we associated the DataSource with the Stream, the player to handle
					// the media can be created

					Player player = javax.media.Manager.createPlayer( dataSource ) ;
					if( player == null )
						return ;
					System.out.println( "player created and linked to datasource" ) ;
					// Add controllerListener to catch Controller changes
					player.addControllerListener( this ) ;
					player.realize() ;
					System.out.println( "player realized" ) ;
					// Call PlayerWindow class to make a GUI for stream
					PlayerWindow pWindow = new PlayerWindow( player, stream ) ;
					// Add the created player to the Vector
					playerWindows.addElement( pWindow ) ;

					// Notify create() that a new stream has arrived
					synchronized( dataSync )
					{
						dataReceived = true ;
						dataSync.notifyAll() ;
					}
				} catch ( Exception e ) {
					System.out.println( "NewReceiveException " +e.getMessage() ) ; }

			// This event is when a stream that was previously unidentifed becomes identified with a
			// participant.  When an RTCP packet arrives that has an SSRC that matches the one without
			// a participant arrives, this event is generated
			} else if( event instanceof StreamMappedEvent )
				{
					if( stream != null && stream.getDataSource() != null )
					{
						DataSource dataSource = stream.getDataSource() ;
						// Find out formats
						RTPControl control = ( RTPControl ) dataSource.getControl( "javax.media.rtp.RTPControl" ) ;
						if( control != null )
						{
							System.out.println( "Previously unidentified stream now associated with participant" ) ;
							System.out.println( "with format " +control.getFormat() +" from user: " +participant.getCNAME() ) ;
						}
					}
			// If this is an instant of the server ending the session, receive the Bye event and quit
			} else if( event instanceof ByeEvent )
				{
					System.out.println( "Stream ended, goodbye - from: " +participant.getCNAME() ) ;
					PlayerWindow pWindow = find( stream ) ;
					if( pWindow != null )
					{
						pWindow.close() ;
						playerWindows.removeElement( pWindow ) ;
					}
			}
		}

		public synchronized void controllerUpdate( ControllerEvent control )
		{
			Player player = ( Player ) control.getSourceController() ;
			// If player wasn't created successfully from controller, return

			if( player == null ) {
				System.out.println( "Player is null" ) ;
				return ; }

			if( control instanceof RealizeCompleteEvent )
			{
				PlayerWindow pWindow = find( player ) ;
				if ( pWindow != null )
				{
					pWindow.create() ;
				}
				pWindow.setVisible( true ) ;
				player.start() ;
			}

			if( control instanceof ControllerErrorEvent )
			{
				player.removeControllerListener( this ) ;
				PlayerWindow pWindow = find( player ) ;
				if( pWindow != null )
				{
					playerWindows.removeElement( pWindow ) ;
				}
				System.out.println( "Error in ControllerErrorEvent: " +control ) ;
			}
		}


		// GUI components
		class PlayerWindow extends Frame
		{
			Player player ;
			ReceiveStream stream ;

			PlayerWindow( Player p, ReceiveStream s )
			{
				player = p ;
				stream = s ;
			}

			public void create()
			{
				add( new PlayerPanel( player ) ) ;
			}

			public void close()
			{
				player.close() ;
				setVisible( false ) ;
				dispose() ;
			}

			public void addNotify()
			{
				super.addNotify() ;
				pack() ;
			}
		}
		// Class taken from AVReceive.java
		class PlayerPanel extends Panel
		{

			Component vc, cc;

			PlayerPanel(Player p) {
			    setLayout(new BorderLayout());
		    	if ((vc = p.getVisualComponent()) != null)
				add("Center", vc);
		   	 if ((cc = p.getControlPanelComponent()) != null)
				add("South", cc);
			}

			public Dimension getPreferredSize() {
			    int w = 0, h = 0;
			    if (vc != null) {
				Dimension size = vc.getPreferredSize();
				w = size.width;
				h = size.height;
			    }
			    if (cc != null) {
				Dimension size = cc.getPreferredSize();
				if (w == 0)
				    w = size.width;
				h += size.height;
			    }
			    if (w < 160)
				w = 160;
			    return new Dimension(w, h);
			}
	    	}

	public static void main( String args[] )
	{
		int i = 1 ;
		RTPPlayer2 player = new RTPPlayer2() ;
		System.out.println( "RTPPlayer instantiated OK" ) ;
		while( guiAddress == null )
		{}
			player.create() ;
		
		try {
			while( !player.finished() )
			{
				System.out.println( "- Not done yet - time " +i +" seconds") ;
				i++ ;
				Thread.sleep( 1000 ) ;
			}
		} catch ( Exception e ) {}
	}

}