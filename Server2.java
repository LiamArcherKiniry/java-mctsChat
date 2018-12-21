import java.net.*;
import java.io.*;
import java.util.*;

/** Server used to send messages
     Nov 26
     Author: Liam Kiniry
*/
import java.net.*;
import java.io.*;

public class Server2{ 
    Vector<PrintWriter> clientList = new Vector<PrintWriter>();

      public static void main(String[] args){

		new Server2();
   }
   
   public Server2(){
   
      try{
         /** create server socket */
			ServerSocket ss = new ServerSocket(12345);
			while(true){
            /** listen ro socket and accept input*/
				Socket cs = ss.accept();
            
            /** start inner ThreadsClass */
				RunableThread ts = new RunableThread(cs);
				ts.start();
			}
		}catch (Exception e)
         {
           e.printStackTrace();
         }

   }
   public class RunableThread extends Thread{
      /** socket to be created in thread at port */
      Socket cs;
      /** reads input from client */
		BufferedReader brIN;
      /** writed output to client */
		PrintWriter pwOUT;
      /** ThreadsClass constructor
            @socket on which to operate/listen
       */
		public RunableThread(Socket ClientSocket){
         /** name*/
         super("Server thread");
         /** Set socket to match the socket passed as parrameter*/
			cs = ClientSocket;
         
         
			try{
				brIN = new BufferedReader(new InputStreamReader(cs.getInputStream()));
				pwOUT = new PrintWriter(new OutputStreamWriter(cs.getOutputStream()));
            clientList.add(pwOUT);
            //pwOUT.println("Welcome");
			}catch(IOException ioe){
				System.out.println(ioe.toString());
			}
		}

      public void run(){
         try{
         while(true){
               
               /** While there is not data to be read wait */
               while(brIN.ready() == false){
   
               try
               {
                  Thread.sleep(1);
               }
                  catch (InterruptedException e)
                  {
                     e.printStackTrace();
                  }
               } 
               /** When there is data to be read */
               /** takes the input from the client selection and puts it into firstMessage */
					String firstMessage = brIN.readLine();
					if(firstMessage.length() != 0){
						broadcastMessage(firstMessage); 
						//pwOUT.flush();
					
					}else {
                  /** if nothing entered */
						pwOUT.println("");
					pwOUT.flush();	
					}
					pwOUT.flush();
					
				}
			}catch(NumberFormatException e){
				e.printStackTrace();
			}catch(NullPointerException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
      private void broadcastMessage(String msg)
      {
         for(PrintWriter p : clientList)
         {
            try
            {
               p.println(msg);
               p.flush();
            }
            catch(Exception ex)
            {
               ex.printStackTrace();
            }
         }
      }

         
    }
      
   
}