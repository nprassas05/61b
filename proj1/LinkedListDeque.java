public class LinkedListDeque<Item> {
	/* nested node class */
	private class DNode {
		public Item item;
		public DNode next;
		public DNode prev;

		public DNode() {
			item = null;
		}

		public DNode(Item i, DNode n, DNode p) {
			item = i;
			next = n;
			prev = p;
		}
	}

	private DNode sentinel;
	private int size;

	/* constructor which creates an empty deque */
	public LinkedListDeque() {
		sentinel = new DNode();
		sentinel.next = sentinel;
		sentinel.prev = sentinel;
		size = 0;
	}

	/* add a new node to front of the dequeue */
	public void addFirst(Item x) {
		DNode p = new DNode(x, sentinel.next, sentinel);

		if (size > 0) {
			sentinel.next.prev = p;
		} else {
			sentinel.prev = p;
		}

		sentinel.next = p;
		++size;
	}

	/* add an item to the end of the deque */
	public void addLast(Item x) {
		DNode p =  new DNode(x, sentinel, sentinel.prev);

		if (size > 0) {
			sentinel.prev.next = p;
		} else {
			sentinel.next = p;
		}

		sentinel.prev = p;
		++size;
	}

	public boolean isEmpty() {
		return (size == 0);
	}

	public int size() {
		return size;
	}

	/* print the contents of the Deque, starting from
	 * the front node
	 */
	public void printDeque() {
		System.out.print("[ ");
		DNode p = sentinel.next;
		while (p != sentinel.prev) {
			System.out.print(p.item + " -> ");
			p = p.next;
		}
		System.out.println(p.item + " ]");
	}

	/* remove and return the first item in the deque */
	public Item removeFirst() {
		if (size > 0) {
			DNode r = sentinel.next;
			r.next.prev = sentinel;
			sentinel.next = r.next;
			--size;
			return r.item;
			/* garbage collector will throw out node being pointed
			 * to by r, so we do not have to explicitly set it
			 * to null */
		}
		return null;
	}

	/* remove and return the last item in the deque */
	public Item removeLast() {
		if (size > 0) {
			DNode last = sentinel.prev;
			last.prev.next = sentinel;
			sentinel.prev = last.prev;
			size -= 1;
			return last.item;
		}
		return null;
	}

	/* return the i'th item in the deque, without removing it */
	public Item get(int index) {
		int currentIndex = 0;
		DNode p = sentinel.next;

		while (currentIndex < index && p != sentinel) {
			p = p.next;
			currentIndex += 1;
		}

		return (p != sentinel) ? p.item : null;
	}

	/* helper method for recursive get method */
	private Item getRecursiveHelper(int index, DNode p) {
		if (p == sentinel) {
			return null;
		} else if (index == 0) {
			return p.item;
		}

		return getRecursiveHelper(index - 1, p.next);
	}

	/* same as get method but uses recursion */
	public Item getRecursive(int index) {
		return index >= 0 ? getRecursiveHelper(index, sentinel.next)
			: null;
	} 

}