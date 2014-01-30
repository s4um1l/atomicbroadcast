import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;

import com.sun.nio.sctp.SctpChannel;
import com.sun.nio.sctp.SctpServerChannel;

//this class acts a base netowrk responsible for broadcast based algorithm
public class Process {
	LC  lamp_clock;
	File file;
	File file1;
	int num_okay2;
	FileWriter fileWritter ;
	BufferedWriter bufferWritter ;
	FileWriter fileWritter1 ;
	BufferedWriter bufferWritter1 ;
	ArrayList<Operation> out;
	ArrayList<SctpChannel> defered;
	ArrayList<Client> c1;
	private Application a;
	int node_id;
	int netconnect;
	int servact;
	int curr_ts;
	int num_okay;
	int flag;
	Server s;
	ArrayList<Operation> op; //array of operation
	ArrayList<ParticipatingNode> pnodes; //participating nodes information
	public class ClientListener implements Runnable{
		SctpChannel clientsock;
		private Server a;
		public  ClientListener(SctpChannel clientsock,Server a){
			this.clientsock=clientsock;
			this.a=a;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (a.active){
				try {
					// System.out.println("Server:Inside srvmsghndlr netconnect is "+netconnect);

					String s=MessageSender.receiveMessage(clientsock);
					//   System.out.println(s+"clientlistener");

					String [] m=s.split(";");

					if (m[0].equals("done")){
						num_okay2++;
						if (num_okay2==pnodes.size()-1){
							//	System.out.println("They are equal we are near");
							a.serverNotify();

						}
					}


					if (m[1].equals("okay")){
						num_okay++;
						lamp_clock.receiveMessClock(Integer.parseInt(m[0]), Integer.parseInt(m[0]));
						//	System.out.println(num_okay+"heck value");
						if (num_okay==pnodes.size()-1){
							//	System.out.println("They are equal we are near");
							a.serverNotify();

						}
					}

				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}
	public class Server implements Runnable{
		SctpServerChannel serverSock;
		boolean active;

		ArrayList<ParticipatingNode> pnodes;
		ArrayList<SctpChannel> clientList;
		ArrayList<Operation> op;
		boolean connected;//to see if connected to all neighbors
		LC lc;
		int name;
		public synchronized void release_broadcast() throws CharacterCodingException{

			curr_ts=Integer.MAX_VALUE;
			//System.out.println("defered size"+defered.size());
			while(defered.size()!=0){
				SctpChannel id=defered.get(0);
				defered.remove(0);
				String m=lamp_clock.getTime()+";"+"okay"+";"+node_id+";";
				MessageSender.sendMessage(id, m);
			}
		}
		public Server(int i,ArrayList<ParticipatingNode> pnodes,ArrayList<Operation> op){
			this.op=op;
			active=true;
			curr_ts=Integer.MAX_VALUE;
			num_okay=0;
			connected=false;
			clientList=new ArrayList<SctpChannel>();
			this.pnodes=pnodes;
			lc =new LC();
			//System.out.println(o[1].data);
			name=i;

			System.out.println("Hi!! I am process "+i);
			System.out.println("Initiating server at "+pnodes.get(i).Name);
			//starting server at i node

			try{
				// Binding Object into Server port

				serverSock = SctpServerChannel.open();
				InetSocketAddress serverAddr = new InetSocketAddress(pnodes.get(i).port);
				serverSock.bind(serverAddr);
				System.out.println("Bound port:"+pnodes.get(i).port);
				System.out.println("Waiting for connection to occur");
			}
			catch(Exception e){
				System.out.println(e+"Exiting");
				System.exit(2);
			}
		}
		public synchronized void serverNotify() {

			// TODO Auto-generated method stub
			//	 System.out.println("I notify");
			notify();
		}
		public synchronized void req_broad(Operation o){
			lamp_clock.increase();
			curr_ts=lamp_clock.getTime();//curr ts updated
			//	System.out.println("Server:"+curr_ts+"lets broadcast"+clientList.size());
			try{
			fileWritter1 = new FileWriter(file1.getName(),true);
			bufferWritter1 = new BufferedWriter(fileWritter1);
			bufferWritter1.write("Request by"+node_id);
			bufferWritter1.close();
			}
			catch(Exception e){}
			
			for (int i=0;i<clientList.size();i++){
				MessageSender.broadcast("request",curr_ts, node_id,clientList.get(i));}
			num_okay=0;
			while (num_okay<pnodes.size()-1){
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//   System.out.println("Server:entered cs");
			num_okay2=0;
			try {
				fileWritter = new FileWriter(file.getName(),true);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			bufferWritter = new BufferedWriter(fileWritter);
			//code to operate on each node
			out.add(o);
			System.out.println("Check by saumil"+o.type);
			if (o.type==0){
				try {
					bufferWritter.write("Push "+o.data);
					System.out.println("Push "+o.data);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}}
			else{
			//	System.out.println("Reaching Here");
				try {
					bufferWritter.write("Pop ");
					System.out.println("Pop");

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}}
			try {
				bufferWritter.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (int i=0;i<clientList.size();i++){
				try {
					MessageSender.sendMessage(clientList.get(i),"operate;"+o.data+";"+o.type+";");
				} catch (CharacterCodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				wait();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}//until reply
			//release cs 
			try {
				//   System.out.println("Server:broadcasting defered one"); 

				release_broadcast();
			} catch (CharacterCodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public void  run(){

			try{
				// Binding Object into Server port

				//accept and handle clients number of clients is equal to number of  nodes -1
				int counter=0;
				// Create buffer to send or receive message
				//handles sending messages to clients
				//System.out.println("workers");

				while(counter<pnodes.size()-1  )
				{
					//System.out.println("received message"+counter);

					// Receive a connection from client and accept it
					SctpChannel clientSock = serverSock.accept();
					clientList.add(clientSock);

					//    System.out.println("received message"+counter);
					String mess="Starting Message from Server";
					//  System.out.println("Waiting before message"+counter);

					MessageSender.sendMessage(clientSock, mess);
					MessageSender.sendMessage(clientSock, "ok;");

					boolean act=false;
					while(act==false){
						Thread.sleep(2000);
						String s=MessageSender.receiveMessage(clientSock);

						String[] m=s.split(";");
						if (m[0].equals("activechannel")){
							act=true;
							//			System.out.println("Activated Channel");

						}
						// System.out.println("Stuck in loop  server");
					}
					servact++;
					//			            WHILE(ACT==FALSE){
					//			            IF(CLIENTLIST.SIZE()==PNODES.SIZE()-2)
					//				            MESSAGESENDER.SENDMESSAGE(CLIENTSOCK, "OK;");
					//			               
					//			        	String [] m=MessageSender.receiveMessage(clientSock).split(";");
					//						if (m[0].equals("activechannel")){
					//							act=true;
					//							System.out.println("Activated Channel");
					//							
					//						}
					//			            }
					//			            servact++;
					ClientListener cl=new ClientListener(clientSock, this);
					new Thread(cl).start();
					// System.out.println("Waiting in Server side"+counter);
					counter++;
					//  Thread.sleep(200);

					// Now server will communication to this Client via clientSock

				}
				// System.out.println("exiting server run");
				netconnect=1;
				connected=true;
			}
			catch(Exception e){
				System.out.println(e);
			}

		}


	}
	public  class Client implements Runnable {
		SctpChannel ClientSock;
		InetSocketAddress serverAddr;
		int collected;
		private int node_name;
		int connectedto;
		public Client(String string, int port, int i,int j) {
			// TODO Auto-generated constructor stub
			collected=0;
			connectedto=i;
			node_name=j;
			System.out.println("Hey gonna connect to "+i+string+port);
			try{
				// Create ClientSock and connect to sever

				serverAddr = new InetSocketAddress(string,port); 

				ClientSock = SctpChannel.open();
				ClientSock.connect(serverAddr, 0, 0);

			}
			catch(Exception e){}

		}


		@Override
		public void run() {
			// TODO Auto-generated method stub


			try{
				//   System.out.println("Create Connection Successfully" +connectedto);
				//    System.out.println("Waiting in Client side");

				System.out.println(MessageSender.receiveMessage(ClientSock));
				String m =(MessageSender.receiveMessage(ClientSock));

				String s[]=m.split(";");
				while (!s[0].equals("quit"))
				{

					clientMsgHandler(m);
					Thread.sleep(200);
					//	 System.out.println("Inside clntmsghndlr netconnect is "+netconnect);

					m =(MessageSender.receiveMessage(ClientSock));
					s=m.split(";");
					System.out.println(m);
				}
			}
			catch(Exception e){}
			System.out.println("quitting client"+node_name);
		}

		private void clientMsgHandler(String m) throws CharacterCodingException {
			// TODO Auto-generated method stub
			try {
				fileWritter = new FileWriter(file.getName(),true);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			bufferWritter = new BufferedWriter(fileWritter);
			//		System.out.println("I am reaching here"+m);
			String[] mess=m.split(";");
			if (mess[0].equals("ok")){
				//		System.out.println("changed bhai"+connectedto);
				collected=2;
				MessageSender.sendMessage(ClientSock, "activechannel;");

			}
			else{
				if (netconnect==1){
					//System.out.println("I am reaching herethe "+mess[1]);
					if (mess[0].equals("operate")){

						if (mess[2].equals("0")){
							try {
								System.out.println("Push "+mess[1]);
								bufferWritter.write("Push "+mess[1]);
								out.add(new Operation(0,Integer.parseInt(mess[1])));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						else{
							try {
								System.out.println("Pop");
								bufferWritter.write("Pop ");
								out.add(new Operation(1,0));

							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						try {
							bufferWritter.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						MessageSender.sendMessage(ClientSock, "done;");

					}
					else{
						int ts=Integer.parseInt(mess[0]);
						//System.out.println("I am reaching herethere "+mess[0]);

						lamp_clock.receiveMessClock(Integer.parseInt(mess[0]), ts);
						//System.out.println("I am reaching herethererwe "+mess[2]);

						String abc=mess[1];
						//System.out.println("type="+abc);
						//System.out.println("type="+abc.length());

						int source=Integer.parseInt(mess[2]);
						//System.out.println("type="+abc);
						String co="request";
						//System.out.println("type="+"request"+abc+co);
						if ("request".equals(abc)){
							System.out.println("request accepted");
							try{
							fileWritter1 = new FileWriter(file1.getName(),true);
							bufferWritter1 = new BufferedWriter(fileWritter1);
							bufferWritter1.write("Request by"+mess[2]);
							bufferWritter1.close();}
							catch(Exception e){
								
							}
							if (curr_ts==Integer.MAX_VALUE||ts<curr_ts||(ts==curr_ts)&& (source<node_id)){//no request of its own or requests smaller than it
								String message=lamp_clock.getTime()+";"+"okay"+";"+node_id+";";
								MessageSender.sendMessage(ClientSock, message);

							}
							else
								defered.add(ClientSock);
							//	System.out.println("I am defering this request:"+m+lamp_clock.getTime());

						}
					}
				}
			}
		}
	}



	public  Process(int i,ArrayList<ParticipatingNode> pnodes,ArrayList<Operation> op){
		file =new File("random"+i+".txt");
		file1= new File("request"+i+".txt");
		out=new ArrayList<Operation>();
		
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
			try {
				file1.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
        num_okay2=0;
		lamp_clock=new LC();
		this.op=op;
		node_id=i;
		servact=0;
		flag=pnodes.size()-1;
		netconnect=0;
		defered=new ArrayList<SctpChannel>();
		curr_ts=Integer.MAX_VALUE;
		this.pnodes=pnodes;
		//check if that server has some operation if not no need of servers 
		if (op.size()>0)
			s=new Server(i,pnodes,op);
		this.a=a;
		new Thread(s).start();
		if (i==0){
			try {
				MakeCon.createServer(55522,pnodes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try {
				MakeCon.createClient(pnodes.get(0).Name,55522);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		//create clients to bind to other servers
		c1= new ArrayList<Client>();

		for (int j=0;j<pnodes.size();j++)
		{
			if (j!=i)//not same as the process
				c1.add(new Client(pnodes.get(j).Name,pnodes.get(j).port,j,i));


		}

		for (int j=0;j<c1.size();j++){
			new Thread(c1.get(j)).start();
		}

		//		while(netconnect==0){
		//			int flagged=1;

		//			for(int it=0;it<c1.size();it++){
		//				int count=0;
		//				if (c1.get(it).collected==2)
		//					 count=1;
		//				else
		//					count=0;
		//				flagged=flagged*count;
		//			}	
		int count=0;
		if (servact==c1.size())
			netconnect=1;
		//		}
		//System.out.println("Done with construc");			
	}

	public synchronized void broadcast(Operation o){

		while(netconnect!=1)
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		s.req_broad(o);


	}
	public synchronized void delivery(){
		while (out.size()==0){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i=0;i<out.size();i++)
			System.out.println("queue contains"+out.get(i).data+out.get(i).type);		
	}
	/*
	public synchronized void release_broadcast(){
		curr_ts=Integer.MAX_VALUE;
		while(defered.size()!=0){
			int id=defered.get(0);
			defered.remove(0);
			MessageSender.sendMess(id,"okay",lamp_clock.getTime(),pnodes);
		}
	}
	public synchronized void handle(Message m,int src,String type){
		int ts=m.getTS();
		lamp_clock.receiveMessClock(src, ts);
		if (type.equals("request")){
			if (curr_ts==Integer.MAX_VALUE||ts<curr_ts||(ts==curr_ts)&& (src<node_id)){//no request of its own or requests smaller than it
			   MessageSender.sendMess(src,"okay",lamp_clock.getTime(),pnodes);
			}
			else
				defered.add(src);
			}
		else if (type.equals("okay")){
			num_okay++;
			if (num_okay==pnodes.size()-1)
				notify();


		}

	}

	 */

	public void kill() {
		// TODO Auto-generated method stub
		s.active=false;
		try {
			bufferWritter.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i=0;i<s.clientList.size();i++)
			try {
				MessageSender.sendMessage(s.clientList.get(i), "quit;");
			} catch (CharacterCodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
