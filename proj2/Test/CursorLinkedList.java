import javafx.scene.text.Text;

public class TextLinkedList {

	/* internal linked list nodes, which will be text objects in this case */
	private class TextNode {
		private Text text;
		private int xPos;
		private int yPos;

		private TextNode next;

		public TextNode(String s, TextNode t) {
			text = new Text(s);
			next = t;
		}
	}

	/* current x and y positions for cursor position */
	private int cursorXPos = 5;
	private int cursorYPos = 5;

	/* sentinel node for convenience */
	private TextNode sentinel;

	/* textnode before the cursor, where text will be inserted after */
	private TextNode nodeBeforeCursor;

	/* sentinel will initially point to null */
	public TextLinkedList() {
		sentinel = new TextNode("blah", null);
		nodeBeforeCursor = sentinel;	
	}

	/* insert a new text object after the current cursor position */
	public void insert(String s) {
		Text t = new Text(s);
		t.setX(cursorXPos);
		t.setY(cursorYPos);

		cursorXPos = cursorXPos + Math.round(t.getLayoutBounds().getWidth()) + 1;
		t.next = nodeBeforeCursor.next;
		nodeBeforeCursor = t;
	}

	/* get x and y coordinates of the cursor */
	public int getCursorXPos() { return cursorXPos; }
	public int getCursorYPos() { return cursorYPos; }

}