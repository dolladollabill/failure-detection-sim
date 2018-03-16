import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServerNode {

	// queue of msgs - many write, this class reads
	LinkedBlockingQueue<Message> rcv_msgs;
	//lists  of hosts denoted by name
	ArrayList<String> hostsUp;
	//name of servernode
	String name;
	// router
	TrafficRouter router;
	
	ServerNode(String name, ArrayList<String> hosts, TrafficRouter r)
	{
		this.name = name;
		rcv_msgs = new LinkedBlockingQueue<Message>();
		hostsUp = new ArrayList<String>();
		router = r;
		for( String h : hosts)
		{
			hostsUp.add(h);
		}
		
	}
	
	public class ServerListener extends Thread{  
	    ServerListener(){
	        this.start();
	    }       
	    @Override
	    public void run() {     
	    	System.out.println(name + " Server Listener running...");
        	while(true) {
        		
        		//recieve healthchecks as Message object
        		Message msg;
				try {
					//use poll for timeout on take
					msg = rcv_msgs.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg = new Message("queue failure", "", "");
				}
        		System.out.println("Recieved on host " + name + ": " + msg.print() );


			}
	    }
	}
	
	
	public class ServerMessenger extends Thread{

	    ServerMessenger(){
	        this.start();
	    }       
	    
	    @Override
	    public void run() {    
	    	
	    	System.out.println(name + " Server Listener running...");
			int counter = 0;
    		while (true) {
    			
    			
	    		for(String h: hostsUp)
	    		{
	    			//rcv_msgs.offer(new Message(name, ""));
	    			router.send(new Message(name, h , "" + counter));
	    			
	    		}
	    		try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    		counter = counter + 1;
	            
    		}

	    }
	}
	
	
	 public void startserver() {
	        try {
	        	
	        	System.out.print("Hosts: ");
	        	for (String h: hostsUp)
	        	{
	        		System.out.print(h + ",");
	        	}

	        	new ServerListener();
	        	new ServerMessenger();
	        	
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	    }
	

}
