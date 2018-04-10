import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*
		 * BASIC TEST ArrayList<String> hosts = new ArrayList<String>();
		 * 
		 * hosts.add("h1"); hosts.add("h2");
		 * 
		 * TrafficRouter router = new TrafficRouter(); HashMap<String, ServerNode>
		 * servers = new HashMap<String, ServerNode>(); ServerNode h1 = new
		 * ServerNode("h1", hosts, router); ServerNode h2 = new ServerNode("h2", hosts,
		 * router);
		 * 
		 * servers.put(h1.name,sh1); servers.put(h2.name, h2);
		 * 
		 * router.update(servers);
		 * 
		 * router.startrouter(); h1.startserver(); h2.startserver();
		 */
		int num = 5;
		ArrayList<Integer> hosts = new ArrayList<Integer>();
		for (int i = 0; i < num; i++) {
			hosts.add(i);
		}

		TrafficRouter router = new TrafficRouter();
		
		ArrayList<Arbitrator> arbitrators;
		for(int i = 0; i < 5; i++) {
			Arbitrator arb = new Arbitrator(router);
			//arbitrators.add(arb);
		}
		
		
		doublylinkedList dll = new doublylinkedList();
		HashMap<Integer, ServerNode> servers = new HashMap<Integer, ServerNode>();
		
		for (Integer h : hosts) {
			ServerNode temp = new ServerNode(h, hosts, router, arbitrators, dll);
			servers.put(h, temp);
			dll.insert(temp, h);
		}

		router.update(servers);
		router.startrouter();

		for (int h : hosts) {
			// add some delay
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			servers.get(h).startserver();
		}

		System.out.println("TEST STARTED");
		// TODO: add mechanism to send interrupts to threads here

		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
