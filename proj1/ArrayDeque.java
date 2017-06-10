public class ArrayDeque<Item> {
	private Item[] items;
	private int size;
	int nextFirst;
	int nextLast;

	private static int RFACTOR = 2;

	/* constructor */
	public ArrayDeque() {
		items = (Item[]) new Object[8];
		size = 0;
		nextFirst = items.length / 2;
		nextLast = items.length / 2 + 1;
	}

	/** Resize our backing array so that it is
      * of the given capacity. */
    private void resize(int capacity) {
    	Item[] newItems = (Item[]) new Object[capacity];
    	int start = plusOne(nextFirst);
    	int length1 = Math.min(items.length - start, size);

    	System.arraycopy(items, start, newItems, 0, length1);

    	int length2 = Math.min(start, newItems.length - length1);
    	System.arraycopy(items, 0, newItems, length1, length2);

    	nextFirst = newItems.length - 1;

    	/* nextLast index will be the length of the items array if we are
    	   growing to a larger capacity, but the size variable instead
    	   if we are shrinking the array */
    	nextLast = Math.min(items.length, size);
    	items = newItems;    	
    }

	/* add item to front of the deque */
	public void addFirst(Item x) {
		if (size == items.length) {
			resize(size * RFACTOR);
		}

		items[nextFirst] = x;
		nextFirst = minusOne(nextFirst);
		++size;
		System.out.println("next first = " + nextFirst);
	}

	/* add item to the end of the queue, from users persepctive */
	public void addLast(Item x) {
		if (size == items.length) {
			resize(size * RFACTOR);
		}

		items[nextLast]= x;
		nextLast = (nextLast + 1) % items.length;
		++size;
	}

	/* check if  deque is empty */
	public boolean isEmpty() {
		return size == 0;
	}

	/* size */
	public int size() {
		return size;
	}

	/* usage ratio, the amount of boxes in the array
	   of the deque that are actually being filled */
	public double usageRatio() {
		return (double) size / items.length;
	}

	/* array length, testing purposes */
	public int arrayLength() {
		return items.length;
	}

	/* compute the index immediately before a given index */
	private int minusOne(int index) {
		if (index == 0) {
			return items.length - 1;
		}
		return index - 1;
	}

	/* compute the index immediately after a given index */
	private int plusOne(int index) {
		return (index + 1) % items.length;
	}

	/* print the contents of the deque */
	public void printDeque() {

		int start = (nextFirst + 1) % items.length;
		int end = start + size;

		for (int i = start; i < end; ++i) {
			System.out.print(items[i % items.length] + " ");
		}
		System.out.println();
	}

	/* remove and return first item in deque */
	public Item removeFirst() {
		int first = plusOne(nextFirst);
		Item r = items[first];
		items[first] = null;
		nextFirst = first;
		--size;

		/* continue cutting array size in half until the usage
		   ratio is at least 25% */
		while (usageRatio() < 0.25) {
			resize(items.length / 2);
		}

		return r;
	}

	/* remove and return last item in deque */
	public Item removeLast() {
		int last = minusOne(nextLast);
		//System.out.println("lastIndex: " + lastIndex);
		Item r = items[last];
		items[last] = null;
		nextLast = last;
		--size;

		/* continue cutting array size in half until the usage
		   ratio is at least 25% */
		while (usageRatio() < 0.25) {
			resize(items.length / 2);
		}

		return r;
	}

	/* return item at a specified index in deque */
	public Item get(int index) {
		/* first get the actual index based on our circular
		 * array structure */
		int i = (plusOne(nextFirst) + index) % items.length;

		return items[i];
	} 
}