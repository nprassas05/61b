import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;

public class TextLinkedList {

	/* internal linked list nodes, which will be text objects in this case */
	public class TextNode {
		private Text text;
		private int xPos;
		private int yPos;

		public TextNode next;
		public TextNode prev;

		public TextNode(Text s) {
			text = s;
		}

		public TextNode(Text s, TextNode n, TextNode p) {
			text = s;
			next = n;
			prev = p;
		}

		public void setX(int x) {
			text.setX(x);
		}

		public void setY(int y) {
			text.setY(y);
		}

		public int getX() {
			return (int) text.getX();
		}

		public int getY() {
			return (int) text.getY();
		}
	}

	/* current x and y positions for cursor position */
	private int cursorXPos = 5;
	private int cursorYPos = 5;

	/* sentinel node for convenience */
	private TextNode sentinel;

	/* textnode before the cursor, where text will be inserted after */
	private TextNode nodeBeforeCursor;
	private TextNode nodeAfterCursor;

	/* sentinel will initially point to null */
	public TextLinkedList() {
		sentinel = new TextNode(new Text("blah"), null, null);
		nodeBeforeCursor = sentinel;
		nodeAfterCursor = sentinel;	
	}
	
	/* insert a new text object after the current cursor position */
	public void insert(Text text) {
		TextNode t = new TextNode(text, nodeBeforeCursor.next, nodeBeforeCursor);
		t.setX(cursorXPos);
		t.setY(cursorYPos);

		cursorXPos = cursorXPos + (int) Math.round(text.getLayoutBounds().getWidth()) + 1;

		nodeBeforeCursor.next = t;
		nodeAfterCursor = t.next;
		nodeBeforeCursor = t;

		if (nodeAfterCursor != null) {
			System.out.println(nodeAfterCursor.getX());
		} else {
			System.out.println("Insert: Still null");
		}

		// /* check if user hit carriage return to go to a new line */
		// if (text.getText().equals("\r")) {
		// 	cursorYPos += text.getLayoutBounds.getHeight();
		// 	cursorXPos = 5;
		// }
	}

	/* set the cursor x and y coordinates */
	public void setCursorCoordinates(double x, double y) {
		cursorXPos = (int) x;
		cursorYPos = (int) y;
	}

	/* get x and y coordinates of the cursor */
	public int getCursorXPos() { return cursorXPos; }
	public int getCursorYPos() { return cursorYPos; }

	/* get the node after the cursor */
	public TextNode nodeAfterCursor() {
		return nodeAfterCursor;
	}

	/* change the cursor position and cursor related nodes with each type of arrow click */
	public void leftArrow(Rectangle cursor) {
		cursorXPos = nodeBeforeCursor.getX();
		nodeAfterCursor = nodeBeforeCursor;
		nodeBeforeCursor = nodeBeforeCursor.prev;

		cursor.setX(cursorXPos);
		cursor.setY(cursorYPos);

		if (nodeAfterCursor != null) {
			System.out.println(nodeAfterCursor.getX());
		} else {
			System.out.println("Left Arrow: Still null");
		}
	}

	// some sample text

	public void rightArrow(Rectangle cursor) {
		System.out.println("Before right arrow pressed: " + cursorXPos);
		if (nodeAfterCursor.next != null) {
			cursorXPos = nodeAfterCursor.next.getX();
		}
		System.out.println("After right arrow: " + cursorXPos);
		
		nodeBeforeCursor = nodeAfterCursor;
		nodeAfterCursor = nodeAfterCursor.next;

		cursor.setX(cursorXPos);
		cursor.setY(cursorYPos);
	}

}