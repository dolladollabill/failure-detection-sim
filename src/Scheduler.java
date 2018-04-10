
import java.util.concurrent.DelayQueue;

public class Scheduler {

	public static void main(String[] args) {
		DelayQueue<LEvent> testq = new DelayQueue<LEvent>();
		testq.put(new LEvent(EType.LEASEREQ, 2, 1000, 5, 0));
		testq.put(new LEvent(EType.LEASEREQ, 3, 2000, 5, 0));
		testq.put(new LEvent(EType.LEASEREQ, 5, 1000, 5, 0));
		testq.put(new LEvent(EType.LEASEREQ, 7, 5000, 7, 0));
		testq.put(new LEvent(EType.LEASEREQ, 8, 10000, 5, 0));

		for (LEvent e : testq) {
			if (e.id == 7) {
				testq.remove(e);
			}
		}

		while (true) {
			try {
				System.out.println(testq.take().id);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
