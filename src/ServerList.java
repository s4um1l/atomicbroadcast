
public class ServerList {
final int maxSize=50;//asssumming no more than 50 nodes
private int[] id = new int[maxSize];
private String[] names= new String[maxSize];
private int[] ports = new int[maxSize];
private int listsize=0;
int search(int idfind){
	for(int i=0;i<listsize;i++)
		if(id[i]==idfind)
			return i;
	return -1;
	
}
int insert(int id,String name,int portNo){
	if ((search(id)==-1) && (listsize<maxSize)){
		this.id[listsize]=id;
		this.names[listsize]=name;
		this.ports[listsize]=portNo;
		listsize++;
		return 1;
		
	}
	else{
		return 0;
		}
}

int getPort(int index){
	return ports[index];
}
String getName(int index){
	return names[index];
}
}