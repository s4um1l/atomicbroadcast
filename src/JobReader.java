import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class JobReader {
	Operation oper[][];
 public void readJobs(int no_nodes) throws NumberFormatException, IOException{
		FileReader f1 = new FileReader("jobs.txt");
		
       oper=new Operation[no_nodes][];
    	int last =0;
        BufferedReader bufferedReader1 = 
                new BufferedReader(f1);
        String line=new String();
           while((line = bufferedReader1.readLine()) != null){
//        	   System.out.println(line);2
        	   String[] a =line.split(" ");
        	   int i=Integer.parseInt(a[0]);
        	   if (Integer.parseInt(a[1])>0)
        	   last =i;
        	   oper[i]=new Operation[Integer.parseInt(a[1])];
        	   for (int it=0;it<Integer.parseInt(a[1]);it++){
        	   try {
				if((line = bufferedReader1.readLine()) != null){
					   String[] b= line.split(" ");
				   Operation temp=new Operation();
				   temp.type=Integer.parseInt(b[0]);
				   System.out.println("check while reading"+temp.type);
				   if (temp.type==0){
				   temp.data=Integer.parseInt(b[1]);
				   				   //System.out.println(temp.data);
				   //System.out.println(temp.lc);
				   }
				   else
					   temp.data=0;

  
				   oper[i][it]=temp;
				   }
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	   
        	   }
        	   
           }
 }
 public ArrayList<Operation> getJobs(int i){
	 ArrayList<Operation>op=new ArrayList<Operation>();
	  for (int it=0;it<oper[i].length;it++)
          op.add(oper[i][it]);
	  
	 return op;
    }
	 
	 
	 
}
