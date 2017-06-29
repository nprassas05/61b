public interface Deque<Item> {
	public void printDeque();
	public Item get(int i);
	public Item removeFirst();
	public Item removeLast();
	public int size();	
	public boolean isEmpty();
	public void addFirst(Item x);
	public void addLast(Item x);
}