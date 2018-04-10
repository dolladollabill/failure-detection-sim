import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Arbitrator {

	// list of downed servers
	ArrayList<Integer> downed;
	TrafficRouter router;

	Arbitrator(TrafficRouter r) {
		downed = new ArrayList<Integer>();
		router = r;
	}

	void check(int from, int suspect) {
		for (int h : downed) {
			if (from == h) {
				// if the server that sends the arb request with failed(suspect) is already down, the tell it to terminate itself
				router.send(-1, from, EType.ARB_DENY, suspect);
			}
			if (suspect == h) {
				// arb accepts downed suspect
				router.send(-1, from, EType.ARB_ACCEPT, suspect);
			}
		}

		downed.add(suspect);
		router.send(-1, from, EType.ARB_ACCEPT, suspect);

	}

}
