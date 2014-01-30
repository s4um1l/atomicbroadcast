import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


public class ConfigFileRead {
	int no_nodes;
	ArrayList<ParticipatingNode> pnodes;
public  void readConfig(){
   	
    no_nodes=0;
     pnodes=new ArrayList<ParticipatingNode>();
	try{
		FileReader f = new FileReader("config1.txt");
		String line=null;
		
	      // Always wrap FileReader in BufferedReader.
        BufferedReader bufferedReader = 
            new BufferedReader(f);
       if  ((line = bufferedReader.readLine()) != null){
    	  
    	   }
       if  ((line = bufferedReader.readLine()) != null){
    	   
    		 String a[]=line.split(" ");
             if (a[0]!="#"){
          	  no_nodes=Integer.parseInt(a[0]);
          // System.out.println(no_nodes);
           }
	   }
       int counter=0;
       line = bufferedReader.readLine();
        while((line = bufferedReader.readLine()) != null && counter<no_nodes) {
       	 String a[]=line.split(" ");

       	 if (a[0]!="#"){
       		 ParticipatingNode p=new ParticipatingNode(a[1], Integer.parseInt(a[2]), Integer.parseInt(a[0]));
       	     pnodes.add(p);
       	 
       	 }
       	 counter++;
        }	

        // Always close files.
        bufferedReader.close();
        
}
	catch(Exception e){
		
	}
}
public void printNodes() {
	// TODO Auto-generated method stub
	 for(ParticipatingNode a:pnodes){
     	System.out.println("###################");
     	System.out.println(a.id);
     	System.out.println(a.Name);
     	System.out.println(a.port);
     	System.out.println("###################");
     	
     }
}
public ArrayList<ParticipatingNode> returnPNodes(){
	return pnodes;
}
public int getno(){
	return no_nodes;
}
}

