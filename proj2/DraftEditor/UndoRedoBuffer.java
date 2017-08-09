public class UndoRedoBuffer {
	private Move[] undoStack;
	private Move[] redoStack;
	private int undoSize;
	private int redoSize;
	private static final int CAPACITY = 100;

	public UndoRedoBuffer() {
		undoSize = 0;
		redoSize = 0;
		undoStack = new Move[CAPACITY];
		redoStack = new Move[CAPACITY];
	}

	public void push(Move m) {
		if (undoSize < CAPACITY) {
			undoStack[undoSize++] = m;
		}
	}

	public Move pop() {
		if (undoSize == 0) {
			return null;
		}

		return undoStack[--undoSize];
	}

	public Move redo() {
		if (redoSize == 0) {
			System.out.println("Null in the REDO method itself");
			return null;
		}

		Move topMove = redoStack[--redoSize];
		undoStack[undoSize++] = topMove;
		return topMove;
	}

	public Move undo() {
		if (undoSize == 0) {
			return null;
		}

		Move topMove = undoStack[--undoSize];
		redoStack[redoSize++] = topMove;
		System.out.println("Redo size = " + redoSize);
		return topMove;
	}
}
