import java.util.concurrent.Delayed;


public class Message implements Delayed{
	
	String from;
	String to;
	String msg;
	
	Message(String f, String t, String m){
		this.from = f;
		this.to = t;
		this.msg = m;
	}
	
	String print() {
		 return "MESSAGE( FROM:" + this.from + "  TO:" + this.to + "  MSG:" + this.msg + " )";
		 
	}
	
}
	