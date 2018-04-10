import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class LEvent implements Delayed {
	EType e;
	int seq;
	int id;
	long timeout;
	long delay;

	LEvent(EType event, int i, int d, int s, long t) {
		long curr = System.currentTimeMillis();
		e = event;
		seq = s;
		id = i;
		delay = curr + d;
		timeout = t;
	}
	
	LEvent(){
		e = EType.ERROR;
	}

	@Override
	public int compareTo(Delayed other) {
		if (this.delay < ((LEvent) other).delay) {
			return -1;
		} else if (this.delay > ((LEvent) other).delay) {
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
