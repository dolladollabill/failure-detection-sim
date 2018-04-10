class Node {
	int id;
	ServerNode n;
	Node next, prev;

	/* Constructor */
	public Node(ServerNode nd, int id) {
		next = null;
		prev = null;
		n = nd;
		this.id = id;
	}

	/* Constructor */
	public Node(int d, ServerNode nd, Node n, Node p) {
		id = d;
		next = n;
		prev = p;

	}

	/* Function to set link to next node */
	public void setLinkNext(Node n) {
		next = n;
	}

	/* Function to set link to previous node */
	public void setLinkPrev(Node p) {
		prev = p;
	}

	/* Funtion to get link to next node */
	public Node getLinkNext() {
		return next;
	}

	/* Function to get link to previous node */
	public Node getLinkPrev() {
		return prev;
	}

	/* Function to set id to node */
	public void setid(int d) {
		id = d;
	}

	/* Function to get id from node */
	public int getid() {
		return id;
	}
}

/* Class linkedList */
class doublylinkedList {
	Node start;
	Node end;
	public int size;

	/* Constructor */
	public doublylinkedList() {
		start = null;
		end = null;
		size = 0;
	}

	/* Function to check if list is empty */
	public boolean isEmpty() {
		return start == null;
	}

	/* Function to get size of list */
	public int getSize() {
		return size;
	}

	/* Function to insert element at begining */
	public void insertAtStart(ServerNode nd, int id) {
		Node nptr = new Node(nd, id);
		if (start == null) {
			nptr.setLinkNext(nptr);
			nptr.setLinkPrev(nptr);
			start = nptr;
			end = start;
		} else {
			nptr.setLinkPrev(end);
			end.setLinkNext(nptr);
			start.setLinkPrev(nptr);
			nptr.setLinkNext(start);
			start = nptr;
		}
		size++;
	}

	/* Function to insert element at end */
	public void insertAtEnd(ServerNode nd, int id) {
		Node nptr = new Node(nd, id);
		if (start == null) {
			nptr.setLinkNext(nptr);
			nptr.setLinkPrev(nptr);
			start = nptr;
			end = start;
		} else {
			nptr.setLinkPrev(end);
			end.setLinkNext(nptr);
			start.setLinkPrev(nptr);
			nptr.setLinkNext(start);
			end = nptr;
		}
		size++;
	}

	/* Function to insert element at position */
	public void insert(ServerNode nd, int id) {

		if (size == 0) {
			insertAtStart(nd, id);
			return;
		}
		if (id < start.id) {
			insertAtStart(nd, id);
			return;
		}
		if (id > end.id) {
			insertAtEnd(nd, id);
			return;
		}

		Node nptr = new Node(nd, id);
		Node ptr = start;

		while (ptr != end) {
			if (ptr.next.id > id) {
				Node tmp = ptr.getLinkNext();
				ptr.setLinkNext(nptr);
				nptr.setLinkPrev(ptr);
				nptr.setLinkNext(tmp);
				tmp.setLinkPrev(nptr);
			}
			ptr = ptr.next;
		}
		size++;
	}

	/* Function to delete node at position */
	public void deleteAtPos(int pos) {
		if (pos == 1) {
			if (size == 1) {
				start = null;
				end = null;
				size = 0;
				return;
			}
			start = start.getLinkNext();
			start.setLinkPrev(end);
			end.setLinkNext(start);
			size--;
			return;
		}
		if (pos == size) {
			end = end.getLinkPrev();
			end.setLinkNext(start);
			start.setLinkPrev(end);
			size--;
		}
		Node ptr = start.getLinkNext();
		for (int i = 2; i <= size; i++) {
			if (i == pos) {
				Node p = ptr.getLinkPrev();
				Node n = ptr.getLinkNext();

				p.setLinkNext(n);
				n.setLinkPrev(p);
				size--;
				return;
			}
			ptr = ptr.getLinkNext();
		}
	}

	/* Function to delete node at position */
	public void delete(int id) {
		if (id == start.id) {
			if (size == 1) {
				start = null;
				end = null;
				size = 0;
				return;
			}
			start = start.getLinkNext();
			start.setLinkPrev(end);
			end.setLinkNext(start);
			size--;
			return;
		}
		if (id == end.id) {
			end = end.getLinkPrev();
			end.setLinkNext(start);
			start.setLinkPrev(end);
			size--;
			return;
		}

		Node ptr = start;

		// TODO: fix me!
		while (ptr != end) {
			if (id == ptr.id) {
				Node n = ptr.next;
				Node p = ptr.prev;

				System.out.println("prev: " + p.id + "curr: " + ptr.id + " next:" + n.id);

				p.next = n;
				n.prev = p;
				size--;
				return;
			}
			ptr = ptr.next;
		}
	}
	
	public Node find(int find_id) {
		Node ptr = start;
		while(start != end) {
			if(ptr.id == find_id)
				return ptr;
			ptr = ptr.next;
		}
		if (end.id == find_id)
			return end;
		
		return null;
	}

	/* Function to display status of list */
	public void display() {
		System.out.print("\nCircular Doubly Linked List = ");
		Node ptr = start;
		if (size == 0) {
			System.out.print("empty\n");
			return;
		}
		if (start.getLinkNext() == start) {
			System.out.print(start.getid() + " <-> " + ptr.getid() + "\n");
			return;
		}
		System.out.print(start.getid() + " <-> ");
		ptr = start.getLinkNext();
		while (ptr.getLinkNext() != start) {
			System.out.print(ptr.getid() + " <-> ");
			ptr = ptr.getLinkNext();
		}
		System.out.print(ptr.getid() + " <-> ");
		ptr = ptr.getLinkNext();
		System.out.print(ptr.getid() + "\n");
	}

}

class testlist {
	public static void main(String[] args) {

		doublylinkedList l = new doublylinkedList();
		ServerNode s = new ServerNode();

		l.insert(s, 5);
		l.insert(s, 25);
		l.insert(s, 17);
		l.insert(s, 44);
		l.insert(s, 0);
		
		System.out.println(l.find(17).next.id);

		l.display();

//		l.delete(0);
//		l.display();
//		l.delete(44);
//		l.display();

	}
}
