public class UndoRedoStack {
	private Move[] moveArray;
	private int size;
	private static final int CAPACITY = 100;

	public UndoRedoStack() {
		size = 0;
		moveArray = new Move[CAPACITY];
	}

	public void push(Move m) {
		if (size < CAPACITY) {
			moveArray[size++] = m;
		}
	}

	public Move pop() {
		if (size == 0) {
			return null;
		}

		return moveArray[--size];
	}
}