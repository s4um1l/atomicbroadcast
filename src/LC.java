//Implementation for Lamport CLock for the alogirthm of atomic broadcast

public class LC {
int c;
public LC() {
	c=1;//initialize with value 1
	
}
public synchronized int getTime(){
	return c;
}
public synchronized void increase(){
	c=c+1;
}
public void sendMessClock(){
	
	c=c+1;
	//code for piggy banking
}
public synchronized void receiveMessClock(int src,int sentValue){
	//System.out.println("Hey i am entering  here whats your prob");

	c=Math.max(c,sentValue)+1;
	//System.out.println("Hey i left here whats your prob");
	//maximimzing and adding one
}
}
