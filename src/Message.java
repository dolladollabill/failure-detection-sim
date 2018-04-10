import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class Message implements Delayed {

	int from;
	int to;
	String msg;
	long delay;
	long sendTime;
	int seq;
	EType type;

	Message(int f, int t, String m, long d, EType e, int seq) {
		long curr = System.currentTimeMillis();
		this.from = f;
		this.to = t;
		this.msg = m;
		this.delay = d + curr;
		this.sendTime = curr;
		this.type = e;
		this.seq = seq;
	}

	Message() {
		this.msg = "invalid";
	}

	String print() {
		return "MESSAGE( " + this.from + " --> " + this.to + " Seq: " + this.seq + " Type: " + this.type + ")";
	}

	@Override
	public int compareTo(Delayed other) {
		if (this.delay < ((Message) other).delay) {
			return -1;
		} else if (this.delay > ((Message) other).delay) {
			return 1;
		}
		return 0;
	}

	@Override
	public long getDelay(TimeUnit tu) {
		long diff = delay - System.currentTimeMillis();
		return diff;
	}

}
