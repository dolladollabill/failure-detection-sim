import java.util.ArrayList;
import java.util.HashMap;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/* BASIC TEST
		ArrayList<String> hosts = new ArrayList<String>();
		
		hosts.add("h1");
		hosts.add("h2");
		
		TrafficRouter router = new TrafficRouter();
		HashMap<String, ServerNode> servers = new HashMap<String, ServerNode>();
		ServerNode h1 = new ServerNode("h1", hosts, router);
		ServerNode h2 = new ServerNode("h2", hosts, router);
		
		servers.put(h1.name, h1);
		servers.put(h2.name, h2);
		
		router.update(servers);
		
		router.startrouter();
		h1.startserver();
		h2.startserver();
		*/
		int num = 3;
		ArrayList<String> hosts = new ArrayList<String>();
		for(int i = 0; i < num; i++)
		{
			hosts.add("h" + i);
		}
		
		TrafficRouter router = new TrafficRouter();
		HashMap<String, ServerNode> servers = new HashMap<String, ServerNode>();
		for(String h: hosts)
		{
			servers.put(h, new ServerNode(h, hosts, router));
		}
		
		router.update(servers);
		router.startrouter();
		
		for(String h: hosts)
		{
			servers.get(h).startserver();
		}
		
		
		//TODO: add mechanism to send interrupts to threads here
		
		
	}

}
