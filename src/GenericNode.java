//import java.util.ArrayList;
//import java.util.Hashtable;
//import java.util.Random;
//import java.util.concurrent.LinkedBlockingQueue;
//
//
//public class ServerNode {
//
//	// queue of msgs - many write, this class reads
//	LinkedBlockingQueue<Message> rcv_msgs;
//	//lists  of hosts denoted by name
//	ArrayList<Integer> hostsUp;
//	//id of servernode
//	int id;
//	// router
//	TrafficRouter router;
//	Random r;
//	long mean;
//	long var;
//	boolean isUp;
//	
//	Arbitrator arb;
//	
//	Hashtable<Integer, Integer> last_recieved;
//	
//		id = 0;
//	}
//	ServerNode(int id, ArrayList<Integer> hosts, TrafficRouter r, Arbitrator a)
//	{
//		this.id = id;
//		rcv_msgs = new LinkedBlockingQueue<Message>();
//		hostsUp = new ArrayList<Integer>();
//		router = r;
//		arb = a;
//		last_recieved = new Hashtable<Integer, Integer>();
//		
//		for(Integer h : hosts)
//		{
//			hostsUp.add(h);
//			last_recieved.put(h, Integer.MAX_VALUE);
//		}
//		
//		this.r = new Random();
//		mean = 50;
//		var = 25;
//		
//		isUp = true;
//		
//	}
//	
//	//third thread to check for lease expiration??
//	
//	
//	public class ServerListener extends Thread{  
//	    ServerListener(){
//	        this.start();
//	    }       
//	    @Override
//	    public void run() {     
//	    	System.out.println("h" + id + " Server Listener running...");
//        	while(true) {
//        		
//        		if (isUp) {
//		    		//recieve healthchecks as Message object
//		    		Message msg;
//					try {
//						//use poll for timeout on take
//						msg = rcv_msgs.take();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						msg = new Message(-1, -1, "", 0);
//					}
//		    		System.out.printf("Recieved on host h%-2d: %-35s took %-2d ms.\n", id, msg.print(), (System.currentTimeMillis() - msg.sendTime) );
//		    		
//		    		if (!msg.msg.contains("ACK")) {
//		        		long delay = (long)(r.nextGaussian()*var + mean);
//		        		Message ack = new Message(msg.to, msg.from, msg.msg + " ACK", delay);
//		    			router.send(ack);
//		    		}
//        		}
//
//			}
//	    }
//	}
//	
//	public class ServerMessenger extends Thread{
//
//	    ServerMessenger(){
//	        this.start();
//	    }       
//	    
//	    @Override
//	    public void run() {    
//	    	
//	    	System.out.println("h" +id + " Server Listener running...");
//			int counter = 0;
//    		while (true) {
//    			
//    			if (isUp) {
//		    		for(int h: hostsUp)
//		    		{
//		    			//rcv_msgs.offer(new Message(name, ""));
//		    			
//		    			//give message delay a gaussian distribution
//		    			long delay = (long)(r.nextGaussian()*var + mean);
//		    			router.send(new Message(id , h , "" + counter, delay));
//		    			
//		    		}
//		    		try {
//						Thread.sleep(2500);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//		    		
//		    		counter = counter + 1;
//    			}
//    		}
//
//	    }
//	}
//	
//	
//	 public void startserver() {
//	        try {
//	        	
//	        	System.out.print("Hosts: ");
//	        	for (int h: hostsUp)
//	        	{
//	        		System.out.print("h" + h + ",");
//	        	}
//
//	        	new ServerListener();
//	        	new ServerMessenger();
//	        	
//	        } catch (Exception e) {
//	        	e.printStackTrace();
//	        }
//	    }
//	
//
//}

public class GenericNode {
	// TODO
	int filler;
}
