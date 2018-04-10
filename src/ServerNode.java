import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServerNode {

	// queue of msgs - many write, this class reads
	LinkedBlockingQueue<Message> rcv_msgs;
	//lists  of hosts denoted by name
	ArrayList<Integer> hostsUp;
	//id of servernode
	int id;
	// router
	TrafficRouter router;
	boolean isUp;
	
	ArrayList<Arbitrator> arbs;
	doublylinkedList servers;
	
	//event handler
	DelayQueue<LEvent> events;
	
	Hashtable<Integer, LeaseData> last_recieved;
	
	ServerNode(){
		id = 0;
	}
	ServerNode(int id, ArrayList<Integer> hosts, TrafficRouter r, ArrayList<Arbitrator> a, doublylinkedList s)
	{
		this.id = id;
		rcv_msgs = new LinkedBlockingQueue<Message>();
		hostsUp = new ArrayList<Integer>();
		router = r;
		arbs = a;
		last_recieved = new Hashtable<Integer, LeaseData>();
		servers = s;
		
		events = new DelayQueue<LEvent>();
		
		for(Integer h : hosts)
		{
			hostsUp.add(h);
			last_recieved.put(h, new LeaseData());
		}
		
		isUp = true;
		
	}
	
	//third thread to check for lease expiration??
	public class EventHandler extends Thread{  
	    EventHandler(){
	        this.start();
	    } 
	    
	    @Override
	    public void run() {
	    	System.out.println("h" + id + " Event Listener Running...");
	    	while(true) {
	    		
	    		LEvent evt = new LEvent();
	    		try {
	    				evt = events.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		System.out.println("h" + id + " retrieved event: "+ evt.e + " for seq " + evt.seq + " from h" + evt.id);
	    		
	    		switch(evt.e) {
				case ERROR:
					//should never get here
					break;
				case LEASEACK:
					//should never get here
					break;
				case LEASEACK_TO:
					//send failed? to aribitartor
					arbs.forEach(a->a.check(id, evt.id));
					events.put(new LEvent(EType.SENDTOARB_TO, id, 2500, -1, System.currentTimeMillis()+2500));
				case LEASEREQ:
					//should never get here
					break;
				case LEASEREQ_RECHECK:
					long now = System.currentTimeMillis();
					if(now - evt.timeout > 100) {
						router.send(id, evt.id, EType.LEASEREQ, evt.seq);
						events.put(new LEvent(EType.LEASEREQ_RECHECK, evt.id, (int)(now-evt.timeout)/2, evt.seq, evt.timeout));
					}
					break;
				case SENDTOARB:
					//should never get here
					break;
				case SENDTOARB_TO:
					//mark this host down
					isUp = false;
					servers.delete(id);
					break;
				case ARB_DENY:
					//mark this host down
					isUp = false;
					servers.delete(id);
					break;
				case ARB_ACCEPT:
					//take that host down
					servers.delete(evt.seq);
					break;
				default:
					//definitely shouldn't get here
					break;

	    		}
	    		
	    		
	    	}
	    }
	}
	
	public class ServerListener extends Thread{  
	    ServerListener(){
	        this.start();
	    }       
	    @Override
	    public void run() {     
		    	System.out.println("h" + id + " Server Listener running...");
	        	while(true) {
	        		if (isUp) {
			    		//recieve healthchecks as Message object
			    		Message msg = new Message();
						try {
							//use poll for timeout on take
							msg = rcv_msgs.take();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		System.out.printf("Recieved on host h%-2d: %-35s took %-2d ms.\n", id, msg.print(), (System.currentTimeMillis() - msg.sendTime) );
			    		
			    		if (msg.type == EType.LEASEREQ && !msg.msg.contains("invalid")) {
			    			router.send(id, msg.from, EType.LEASEACK, msg.seq);
			    		}
			    		else if (msg.type == EType.LEASEACK && !msg.msg.contains("invalid") && msg.seq == last_recieved.get(msg.from).curr_seq) {
			    			//ignore if sequence is not right
			    			
			    			//remove events from scheduler
			    			for(LEvent e: events) {
			    				if (e.id == msg.from) {
			    					events.remove(e);
			    				}
			    			}
			    		}
			    			
			    		}
			}
	    }
	}
	
	public class ServerMessenger extends Thread{

	    ServerMessenger(){
	        this.start();
	    }       
	    
	    @Override
	    public void run() {    
	    	
		    	System.out.println("h" +id + " Server Listener running...");
			int counter = 0;
			
	    		while (true) {
	    			
	    			if (isUp) {
	    				int range = 1;
	    				
	    				Node ptr = servers.find(id);
	    				for(int i = 0; i < range; i++)
	    				{
	    					router.send(id, ptr.prev.id, EType.LEASEREQ, counter);
	    					events.put(new LEvent(EType.LEASEACK_TO, ptr.prev.id, 2500, counter, System.currentTimeMillis() + 2500));
	    					events.put(new LEvent(EType.LEASEREQ_RECHECK, ptr.prev.id, 2500/2, counter, System.currentTimeMillis() + 2500));
	    					last_recieved.get(ptr.prev.id).lease_begin = System.currentTimeMillis();
	    					last_recieved.get(ptr.prev.id).curr_seq = counter;
	    					ptr = ptr.prev;
	    				}
	    				ptr = servers.find(id);
			    		for(int i = 0; i < range ; i++)
			    		{
			    			router.send(id , ptr.next.id , EType.LEASEREQ, counter);
			    			events.put(new LEvent(EType.LEASEACK_TO, ptr.next.id, 2500, counter, System.currentTimeMillis() + 2500));
			    			events.put(new LEvent(EType.LEASEREQ_RECHECK, ptr.next.id, 2500/2, counter, System.currentTimeMillis() + 2500));
			    			last_recieved.get(ptr.next.id).lease_begin = System.currentTimeMillis();
	    					last_recieved.get(ptr.next.id).curr_seq = counter;
			    			ptr = ptr.next;
			    			
			    		}
			    		counter += 1;
			    		
			    		try {
							Thread.sleep(2500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					}
	    			}
	    		}

	    }
	}
	
	
	 public void startserver() {
	        try {
	        	
	        	System.out.print("Hosts: ");
	        	for (int h: hostsUp)
	        	{
	        		System.out.print("h" + h + ",");
	        	}

	        	new ServerListener();
	        	new ServerMessenger();
	        	new EventHandler();
	        	
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	    }
	

}
