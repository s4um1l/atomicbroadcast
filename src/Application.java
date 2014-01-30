import java.util.ArrayList;


public class Application implements Runnable {
	ArrayList<Operation> op;
	ArrayList<Operation> out;
 	int networkstatus;
	Process p;
	public Application(ArrayList<Operation> op,Process p) {
		this.op=op;
	
		this.p=p;
		// TODO Auto-generated constructor stub
	}
public synchronized void  broadcast(Operation o){
	
		p.broadcast(o);
	}

public synchronized void deliver(){
	while (p.out.size()==0)
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	for (int i=0;i<p.out.size();i++)
       System.out.println("queue contains"+p.out.get(i).data+p.out.get(i).type);		
}
@Override
public void run() {
	// TODO Auto-generated method stub
	
	broadcast(op.get(0));
	deliver();
}
}
