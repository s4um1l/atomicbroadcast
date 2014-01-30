import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.SctpChannel;
import com.sun.nio.sctp.SctpServerChannel;

//Class to handle cons
public class MakeCon {
	/* a method which ensures all servers are online before their respective clients and that conncections are properly 
	nodes with no operations dont get their servers started */
	 public static void createServer(int port,ArrayList<ParticipatingNode> pnod) throws IOException {
	        // Create SCTP Server Object
	        SctpServerChannel serverSock;
	        
	        
	        // Binding Object into Server port

	        serverSock = SctpServerChannel.open();
	        InetSocketAddress serverAddr = new InetSocketAddress(port);
	        serverSock.bind(serverAddr);
	        
	       
	        System.out.println("Bound port: 55522");
	        System.out.println("Waiting for connection ...");
	        
	        // Waiting for Connection from client
	        boolean Listening = true;
	        // Create buffer to send or receive message
	        int counter=0;
	        ArrayList<SctpChannel> listClients = new ArrayList<SctpChannel>(); 
	        while(Listening)
	        {
	            // Receive a connection from client and accept it
	            SctpChannel clientSock = serverSock.accept();
	            listClients.add(clientSock);
	            System.out.println("Received Connection");
	            // Now server will communication to this Client via clientSock
	            ByteBuffer byteBuffer;
	            byteBuffer = ByteBuffer.allocate(512);
	            String Message = "MESSAGE FROM SERVER INITIATOR";
	            MessageSender.sendMessage(clientSock,Message);
	         
	            System.out.println(MessageSender.receiveMessage(clientSock));
	      
	            counter++;
	            System.out.println(pnod.size()-1+" "+counter);
	            
	            if (counter>=pnod.size()-1){
	                Listening=false;
	            	SctpChannel[] list= listClients.toArray(new SctpChannel[listClients.size()]);
	            	for(int i=0;i<list.length;i++){
	            		MessageSender.sendMessage(list[i],"Done recieved all");
	
	            	}
	            		
	            	
	          }
	        }
	        System.out.println("ALL systems are go and online");
	    }
	 public static  void createClient(String name,int port) throws IOException {
	       ByteBuffer byteBuffer;
	       byteBuffer = ByteBuffer.allocate(512);

	       // Create ClientSock and connect to sever
	       SctpChannel ClientSock;
	       InetSocketAddress serverAddr = new InetSocketAddress(name,port); 
	       ClientSock = SctpChannel.open();
	       ClientSock.connect(serverAddr, 0, 0);
	       
	       System.out.println("Create Connection Successfully");
	      
	       System.out.println("Receive Message from Server Initiator:");
	       System.out.println(MessageSender.receiveMessage(ClientSock));
	       MessageSender.sendMessage(ClientSock,"Ready to go");
	       System.out.println("Receive Message from Server Initiator:");
	       System.out.println(MessageSender.receiveMessage(ClientSock));

	    }
	

}


