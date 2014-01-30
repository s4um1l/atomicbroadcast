
public class Message {
int src,dest;
String type;// type =request reply work wait etc as design goes to be updated 
String message;
public Message(int s,int d ,String ty,String msg){
	src=s;
	dest=d;
	type=ty;
	message=msg;
			
}
public int getSrcId(){
	return src;
}
public int getDestId(){
	return dest;
}
public String getType(){
	return type;
}
public String getMessage(){
	return message;
}
public int getTS(){
	String a[]=message.split(";");
	return Integer.parseInt(a[0]);
	//TS;TYPE;SRC;DST
}
}
