import javafx.scene.text.Text;

public class TextLinkedList {

	/* internal linked list nodes, which will be text objects in this case */
	private class TextNode {
		private Text text;
		private int xPos;
		private int yPos;

		private TextNode next;
		//what in the world

		public TextNode(Text s, TextNode t) {
			text = s;
			next = t;
		}

		public void setX(int x) {
			text.setX(x);
		}

		public void setY(int y) {
			text.setY(y);
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
		sentinel = new TextNode(new Text("blah"), null);
		nodeBeforeCursor = sentinel;
		nodeAfterCursor = null;	
	}
	
	/* insert a new text object after the current cursor position */
	public void insert(Text text) {
		TextNode t = new TextNode(text, nodeBeforeCursor.next);
		t.setX(cursorXPos);
		t.setY(cursorYPos);

		cursorXPos = cursorXPos + (int) Math.round(text.getLayoutBounds().getWidth()) + 1;

		nodeAfterCursor = nodeBeforeCursor.next;
		nodeBeforeCursor = t;

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

	/* return the node before the cursor */

}