import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.net.*;

/** 		GUI Client
      multithread messaging program
      Liam Kiniry PE 11

**/

public class guiClient extends JFrame {
	private JTextArea jtaSendText;
	private JTextArea jtaRecvText;
	private JPanel 	jpTextPanel;
	private JButton 	jbSend;
	private JButton 	jbExit;
	private JPanel 	jpButtonPanel;
	private JPanel 	jpRadioPanel;
	private ButtonGroup 	bgCmds;
	private JRadioButton jrbEncrypt;
	private JRadioButton jrbDecrypt;
	private JRadioButton jrbError;
   public static int PORT_NUMBER = 12345;
   public String aName;


	
	public static void main(String [] args) throws UnknownHostException{
		// test the arguments - need a host to talk to
    
      InetAddress localhost = InetAddress.getLocalHost();
      // hostName = args[0];
		// if(args.length <= 0) {
// 			System.out.println("You must specify a host.");
// 			System.exit(1);
// 		}
		
		// create the client
      String hostName = localhost.getHostName();
		guiClient cc = new guiClient((String.valueOf(hostName)),args[0]);	
      
      // setup the socket connection to the host
		
      
		
	}


	/* Constructor sets up the GUI */
	public guiClient(String host, String myName) {
		// setup the frame for the display
      String hostName = host;
       aName = myName;
		setTitle(aName);
		setSize(400,400);
      		   Socket s = null;
		BufferedReader in = null;
		PrintWriter out = null;
		// setup the frame components
		// Text areas first
		jtaSendText = new JTextArea("Send text",10,50);
		jtaSendText.setBorder(new EtchedBorder());
		jtaRecvText = new JTextArea("",10,50);
		jtaRecvText.setBorder(new EtchedBorder());
		jpTextPanel = new JPanel();
		jpTextPanel.setLayout(new GridLayout(2,1));
		// place the text areas in JScrollPanes
		JScrollPane jbSendPane = new JScrollPane(jtaSendText);
		JScrollPane recvPane = new JScrollPane(jtaRecvText);
		jpTextPanel.add(jbSendPane);
		jpTextPanel.add(recvPane);
		
		// Buttons send & next
		jbSend = new JButton("Send");
		jbExit = new JButton("Exit");
		jpButtonPanel = new JPanel();
		jpButtonPanel.add(jbSend);
		jpButtonPanel.add(jbExit);
		
		// handle the jbExit button
		jbExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}	
		});
	
		// [X] close handler
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.exit(0);	
			}	
		});
	
		// Radiobuttons last
		jrbEncrypt = new JRadioButton("Encrypt");
		jrbDecrypt = new JRadioButton("Decrypt");
		jrbError = new JRadioButton("Error");
		bgCmds = new ButtonGroup();
		bgCmds.add(jrbEncrypt);
		bgCmds.add(jrbDecrypt);
		bgCmds.add(jrbError);

		
				
		// now add the components to the frame

		add(jpButtonPanel,BorderLayout.SOUTH);
		add(jpTextPanel,BorderLayout.CENTER);
		
		setLocationRelativeTo(null);
		setVisible(true);

      try{
			s = new Socket(hostName,PORT_NUMBER);	// Change this to the interface's PORT_NUMBER
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                  //Do receiving in separate class
         ReceiveClass rc = new ReceiveClass(in,jtaRecvText);
         rc.start();
			out = new PrintWriter(s.getOutputStream());
         
		}
		catch(UnknownHostException uhe) {
			//jtaRecvText.setText("Unable to connect to host.");
			return;
		}
		catch(IOException ie) {
			//jtaRecvText.setText("IOException communicating with host.");
			return;
		}
      // handle the jbSend button
		SendHandler sh = new SendHandler(s,aName,host,jtaSendText,jtaRecvText, in, out);
		jbSend.addActionListener(sh);


     
	}
      public class ReceiveClass extends Thread
   {
      BufferedReader br;
      JTextArea jtaRecvText;
      
      public ReceiveClass(BufferedReader _br,JTextArea rt)
      {
         br = _br;
         this.jtaRecvText = rt;
      }
      
      public void run()
      {
         while(true)
         {
            try
            {
               String msg = br.readLine();
               jtaRecvText.append(br.readLine() + "\n");
               
            }
            catch(Exception ex)
            {
               ex.printStackTrace();
            }
         
         }
      }
   }


}

/**
	This class contains the code that communicates with the server
	and gets resulting data back. It will be called as a result of the
	jbSend button being pressed.
*/
class SendHandler implements ActionListener{
	String host;
   int PORT_NUMBER = 12345;
	JTextArea jtaSendText;
	JTextArea jtaRecvText;
   String uName;
   BufferedReader in;
   PrintWriter out;

	
	/** constructor sets attributes to references to the client */
	public SendHandler(Socket s, String name, String h, JTextArea st, JTextArea rt, BufferedReader br, PrintWriter pw) {
		host = h;
		jtaSendText = st;
		jtaRecvText = rt;
      uName = name;
      int PORT_NUMBER = 12345;
      in = br;
      out = pw;
      Socket g = s;
			
	}
	
	/**  performs all the connection work  */
	public void actionPerformed(ActionEvent ae) {
   


				try{
			// Send over the text
			//jtaRecvText.append("\n"); // clear text area
			   String orig = jtaSendText.getText();
				out.println((uName) +": "+ orig);
				out.flush();
				
	
						}
		catch(Exception ie) {
			jtaRecvText.setText("IOException communicating with host.");
			return;
		}
	}

}