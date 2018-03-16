import java.util.HashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrafficRouter {
	
	// hashmap of server nodes
	HashMap<String, ServerNode> server_nodes;
	
	// queue of msgs - many write, this class reads
	DelayQueue<Message> router_msgs;
	
	TrafficRouter(){
		 router_msgs = new DelayQueue<Message>();
	}
	
	public void update(HashMap<String, ServerNode> sn){
		this.server_nodes = sn;
	}
	
	public void send(Message m)
	{
		router_msgs.offer(m);
	}
	
	public class RouterListener extends Thread{  
	    RouterListener(){
	        this.start();
	    }       
	    @Override
	    public void run() {     
	    	System.out.println("RouterListener running... ");
        	while(true) {
        		
        		//recieve messages from all the servers
        		try {
					Message m = router_msgs.take();
					server_nodes.get(m.to).rcv_msgs.offer(m);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
			}
	    }
	}
	
	
	 public void startrouter() {
	        try {

	        	new RouterListener();
	        	//new RouterMessenger();
	        	
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	    }
	
	
	
	
}
