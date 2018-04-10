
public class LeaseData {
	int curr_seq;
	long lease_begin;

	LeaseData() {
		curr_seq = -1;
		lease_begin = -1;
	}

	LeaseData(int s, int b) {
		curr_seq = s;
		lease_begin = b;
	}

}
