import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.SctpChannel;


public class MessageSender {
	 public static void  sendMess(int dest, String string, int ts, ArrayList<ParticipatingNode> pnodes){
		 
		
	 }
	  public static  void sendMessage(SctpChannel clientSock, String Message) throws CharacterCodingException
	    {
		//  System.out.println(Message+"new string");
	        // prepare byte buffer to send massage
	        ByteBuffer sendBuffer = ByteBuffer.allocate(512);
	        sendBuffer.clear();
	        //Reset a pointer to point to the start of buffer 
	        sendBuffer.put(Message.getBytes());
	        sendBuffer.flip();

	        
	        try {
	            //Send a message in the channel 
	            MessageInfo messageInfo = MessageInfo.createOutgoing(null,0);
	            clientSock.send(sendBuffer, messageInfo);
	        } catch (IOException ex) {
	            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
	        }
}
	 public static String byteToString(ByteBuffer byteBuffer)
	    {
	 		byteBuffer.position(0);
	 		byteBuffer.limit(512);
	 		byte[] bufArr = new byte[byteBuffer.remaining()];
	 		byteBuffer.get(bufArr);
	 		return new String(bufArr);
	    }
	 public static  String receiveMessage(SctpChannel clientSock) throws IOException{
		  ByteBuffer byteBuffer;
          byteBuffer = ByteBuffer.allocate(512);
          MessageInfo messageInfo  = clientSock.receive(byteBuffer,null,null);
          String message = byteToString(byteBuffer);
        //  System.out.println("Received Message:");
       //   System.out.println(message);
          return message;
	 }
	public static void broadcast(String string, int curr_ts,int src, SctpChannel sctpChannel) {
		// TODO  Auto-generated method stub
		String message=curr_ts+";"+string+";"+src+";";
		try {
			sendMessage(sctpChannel, message);
		} catch (CharacterCodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				}
	  
}
