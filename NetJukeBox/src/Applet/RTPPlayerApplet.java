package Applet;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.media.Controller;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Manager;
import javax.media.Player;
import javax.media.RealizeCompleteEvent;
import javax.media.SizeChangeEvent;
import javax.media.protocol.DataSource;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.ReceiveStreamListener;
import javax.media.rtp.SessionAddress;
import javax.media.rtp.SessionManager;
import javax.media.rtp.SessionManagerException;
import javax.media.rtp.event.NewReceiveStreamEvent;
import javax.media.rtp.event.ReceiveStreamEvent;
import javax.media.rtp.event.RemotePayloadChangeEvent;
import javax.media.rtp.rtcp.SourceDescription;

import com.sun.media.rtp.RTPSessionMgr;
import com.sun.media.ui.PlayerWindow;

// This RTP applet will allow a user to playback streams for one audio
// session and one  video session. Video and Audio RTP monitors are
// also available for displaying RTCP statistics of this
// session.Methods
// StartSessionManager() will take care of starting the session and
// registering this applet as an RTP Session Listener.
// Method RTPSessionUpdate() will process all the RTPEvents sent by
// the SessionManager.
public class RTPPlayerApplet  extends Applet implements
ControllerListener, ReceiveStreamListener, ActionListener{
  
    
    InetAddress destaddr;
    String address;
    String portstr;
    String media;
    SessionManager audiomgr = null;
    Component visualComponent = null;
    Component controlComponent = null;
    Panel panel = null;
    Button audiobutton = null;
    //Button videobutton = null;
    GridBagLayout gridbag = null;
    GridBagConstraints c = null;
    //ParticipantListWindow audiogui = null;
    int width = 320;
    int height =0;
    Vector playerlist = new Vector();
    
    //Instance du player
    Player player = null;
   
    
    public void init(){
        setLayout( new BorderLayout() );
        Panel buttonPanel = new Panel();
        buttonPanel.setLayout( new FlowLayout() );
        add("North", buttonPanel);
        media = getParameter("audio");
        if (media.equals("On")){
            address = getParameter("audiosession");
            portstr = getParameter("audioport");
            StartSessionManager(address,
                                StrToInt(portstr),
                                "audio");
            if (audiomgr == null){
                System.err.println("null audio manager");
                return;
            }
            //audiogui = new ParticipantListWindow(audiomgr);
            // add a button for the audio RTP monitor
            audiobutton = new Button("Audio RTP Monitor");
            audiobutton.addActionListener(this);
            buttonPanel.add(audiobutton);
        }
    }// end of constructor

    public void start(){
        if (player == null) {
        	new PlayerWindow(player);
        }
    }
    // applet has been stopped, stop and deallocate all the RTP players.
    public void stop(){
        if (player != null) {
        	player.close();
        }
    }

    // applet has been destroyed by the browser. Close the Session
    // Manager. 
    public void destroy(){
        // close the video and audio RTP SessionManagers
        String reason = "Shutdown RTP Player";
  
        if (audiomgr != null){
            audiomgr.closeSession(reason);
            audiomgr = null;
        }
        super.destroy();
    }
            
   
    public void actionPerformed(ActionEvent event){
        Button button = (Button)event.getSource();
        if ((button == audiobutton) && (audiomgr != null)) ;
            //audiogui = new ParticipantListWindow(audiomgr);
            //audiogui.Show();
    }
    
    public String getAddress(){
        return  address;
    }
    
    public int getPort(){
        // the port has to be returned as an integer
        return StrToInt(portstr);
    }
    
    public String getMedia(){
        return media;
    }
    
    private int StrToInt(String str){
        if (str == null)
            return -1;
        Integer retint = new Integer(str);
        return  retint.intValue();
    }

    public synchronized void controllerUpdate(ControllerEvent event) {
        Player player = null;
        Controller controller = (Controller)event.getSource();
        if (controller instanceof Player)
            player  =(Player)event.getSource();
        
        if (player == null)
            return;
        
        
        if (event instanceof RealizeCompleteEvent) {
            // add the player's control component to the applet
            if (( controlComponent = 
                  player.getControlPanelComponent()) != null){
                height += controlComponent.getPreferredSize().height;
                if (panel == null) {
                    panel = new Panel();
                    panel.setLayout(new BorderLayout());
                }
                repositionPanel(width, height);
                panel.add("South", controlComponent);
                panel.validate();
            }
            
            if (panel != null){
                add("Center", panel);
                invalidate();
            }
        }

        if (event instanceof SizeChangeEvent) {
            if (panel != null){
                SizeChangeEvent sce = (SizeChangeEvent) event;
                int nooWidth = sce.getWidth();
                int nooHeight = sce.getHeight();
                
                // Add the height of the default control component
                if (controlComponent != null)
                    nooHeight += controlComponent.getPreferredSize().height;
                
                // Set the new panel bounds and redraw
                repositionPanel(nooWidth, nooHeight);
            }
        }
        validate();
    }

    /**
     * The video/control component panel needs to be repositioned to sit
     * in the middle of the applet window.
     */
    void repositionPanel(int width, int height) {
        panel.setBounds(0,
                        0,
                        width,
                        height);
        panel.validate();
    }

    public void update( ReceiveStreamEvent event){
        SessionManager source =(SessionManager)event.getSource();

        // create a new player if a new recvstream is detected
        if (event instanceof NewReceiveStreamEvent){
            try{
                ReceiveStream stream = ((NewReceiveStreamEvent)event).getReceiveStream();
                DataSource dsource = stream.getDataSource();
                if (player!=null) {
                	player.stop();
                	player.setSource(dsource);
                	player.start();
                }
            }catch (Exception e){
            	System.err.println("RTPPlayerApplet Exception " + e.getMessage());
            	e.printStackTrace();
            }
            /*if (source == audiomgr){
                if (playerlist != null)
                        playerlist.addElement((Object)newplayer);
                new PlayerWindow(newplayer);
            }*/
        }// if (event instanceof NewReceiveStreamEvent)
        
    }// end of RTPSessionUpdate
        
    private SessionManager StartSessionManager(String destaddrstr,
                                                  int port,
                                                  String media){
        // this method create a new RTPSessionMgr and adds this applet
        // as a SessionListener, before calling initSession() and startSession()
        SessionManager mymgr = new RTPSessionMgr();
        /*if (media.equals("video"))
            videomgr = mymgr;*/
        if (media.equals("audio"))
            audiomgr = mymgr;
        if (mymgr == null)
            return null;
        mymgr.addReceiveStreamListener(this);
        //if (media.equals("audio"))
        //  EncodingUtil.Init((SessionManager)mymgr);
        
        // for initSession() we must generate a CNAME and fill in the
        // RTP Session address and port
        String cname = mymgr.generateCNAME();
        String username = "jmf-user";

        SessionAddress localaddr = new SessionAddress();
        
        try{
            destaddr = InetAddress.getByName(destaddrstr);
        }catch (UnknownHostException e){
            System.err.println("inetaddress " + e.getMessage());
            e.printStackTrace();
        }    
        SessionAddress sessaddr = new SessionAddress(destaddr,
                                                           port,
                                                           destaddr,
                                                           port+1);
        
        SourceDescription[] userdesclist = new SourceDescription[4];
        int i;
        for(i=0; i< userdesclist.length;i++){
            if (i == 0){
                userdesclist[i] = new
                    SourceDescription(SourceDescription.SOURCE_DESC_EMAIL,
                                    "jmf-user@sun.com",
                                    1,
                                    false);
                continue;
            }

            if (i == 1){
                userdesclist[i] = new
              SourceDescription(SourceDescription.SOURCE_DESC_NAME,
                                    username,
                                    1,
                                    false);
                continue;
            }
            if ( i == 2){
                userdesclist[i] = new 
                    SourceDescription(SourceDescription.SOURCE_DESC_CNAME,
                                          cname,
                                      1,
                                      false);
                continue;
            }
            if (i == 3){ 
                userdesclist[i] = new
            SourceDescription(SourceDescription.SOURCE_DESC_TOOL,
                                  "JMF RTP Player v2.0",
                                  1,
                                  false);
                continue;
            }
        }// end of for
        
        // call initSession() and startSession() of the RTPsessionManager
        try{
            mymgr.initSession(localaddr,
                              mymgr.generateSSRC(),
                              userdesclist,
                              0.05,
                              0.25);
            mymgr.startSession(sessaddr,1,null);
        }catch (SessionManagerException e){
          System.err.println("RTPPlayerApplet: RTPSM Exception " + e.getMessage());
          e.printStackTrace();
          return null;
        }catch (IOException e){
           System.err.println("RTPPlayerApplet: IO Exception " + e.getMessage());
           e.printStackTrace();
           return null;
        }
        
        return mymgr;
    }       

}// end of class
