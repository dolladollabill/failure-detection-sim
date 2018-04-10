import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.DelayQueue;

public class TrafficRouter {
	// hashmap of server nodes
	HashMap<Integer, ServerNode> server_nodes;
	Random r;
	long mean;
	long var;

	// queue of msgs - many write, this class reads
	DelayQueue<Message> router_msgs;

	TrafficRouter() {
		router_msgs = new DelayQueue<Message>();
		this.r = new Random();
		mean = 50;
		var = 25;

	}

	public void update(HashMap<Integer, ServerNode> sn) {
		this.server_nodes = sn;
	}

	public void send(int from, int to, EType e, int seq) {
		//give message delay a gaussian distribution
		long delay = (long) (r.nextGaussian() * var + mean);
		router_msgs.put(new Message(from, to, "" + seq, delay, e, seq));
	}

	public class RouterListener extends Thread {

		RouterListener() {
			this.start();
		}

		@Override
		public void run() {
			System.out.println("RouterListener running... ");
			while (true) {

				// recieve messages from all the servers
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
			// new RouterMessenger();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

};
