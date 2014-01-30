import java.io.IOException;
import java.util.ArrayList;


public class Service {
    
public static void main(String[] args){
	ConfigFileRead c=new ConfigFileRead();
	c.readConfig();
	c.printNodes();
    JobReader j=new JobReader();
    try {
		j.readJobs(c.getno());
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    Application a;

	Process p;

		p = new Process(Integer.parseInt(args[0]),c.returnPNodes(),j.getJobs(Integer.parseInt(args[0])));
	
		// TODO Auto-generated catch block
	
   ArrayList<Operation> op=j.getJobs(Integer.parseInt(args[0]));
     for (int it=0;it<op.size();it++)
    	 System.out.println("Saumil Printing the node worklist"+op.get(it).data+op.get(it).type);
  
    
	//System.out.println("back in service");			
  int count=0;
  for (int it=0;it<op.size();it++){
	//	if (Integer.parseInt(args[0])==0){
	   System.out.println("checking value of type"+op.get(it).type);
		p.broadcast(op.get(it));
		//count++;
		//op.remove(0);	
   }
       p.kill();
       p.delivery();
}
  
//	//	p.deliver();
//	}
}
