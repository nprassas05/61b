import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;

public class TextBufferList {

	/* internal linked list nodes, which will be text objects in this case */
	public class TextNode {
		public Text text;

		public TextNode next;
		public TextNode prev;

		public TextNode(Text t, TextNode n, TextNode p) {
			text = t;
			next = n;
			prev = p;
		}

		public void setX(int x) {
			text.setX(x);
		}

		public void setY(int y) {
			text.setY(y);
		}

		public char firstChar() {
			return text.getText().charAt(0);
		}

		public int getWidth() {
			return (int) Math.round(text.getLayoutBounds().getWidth());
		}
	}

	/* sentinel node for convenience */
	private TextNode sentinel;

	/* textnode before the cursor, where text will be inserted after */
	private TextNode currentNode;

	/* sentinel will initially point to null */
	public TextBufferList() {
		sentinel = new TextNode(new Text(" "), null, null);
		currentNode = sentinel;
	}
	
	/* insert a new text object after the current cursor position */
	public void insert(Text text) {
		TextNode t = new TextNode(text, currentNode.next, currentNode);
		currentNode.next = t;
		currentNode = t;
	}

	/* return the current node */
	public TextNode getCurrentNode() {
		return currentNode;
	}

	public TextNode getFirstNode() {
		return sentinel.next;
	}

	// /* change the cursor position and cursor related nodes with each type of arrow click */
	// public void leftArrow(Rectangle cursor) {
	// 	cursorXPos = nodeBeforeCursor.getX();
	// 	nodeAfterCursor = nodeBeforeCursor;
	// 	nodeBeforeCursor = nodeBeforeCursor.prev;

	// 	cursor.setX(cursorXPos);
	// 	cursor.setY(cursorYPos);

	// 	if (nodeAfterCursor != null) {
	// 		System.out.println(nodeAfterCursor.getX());
	// 	} else {
	// 		System.out.println("Left Arrow: Still null");
	// 	}
	// }

	// public void downArrow(Rectangle cursor) {
	// 	int xPos = (int) cursor.getX();
	// 	TextNode runner = nodeBeforeCursor;

	// 	while (runner.getY() == cursor.getY()) {
	// 		runner = runner.next;
	// 	}

	// 	while (runner.getX() < xPos) {
	// 		runner = runner.next;
	// 	}

	// 	cursor.setY(runner.getY());
	// 	cursor.setX(runner.getX());
	// }

	// public void upArrow(Rectangle cursor) {
	// 	int xPos = (int) cursor.getX();
	// 	TextNode runner = nodeBeforeCursor;

	// 	while (runner != null && runner.getY() == cursor.getY()) {
	// 		runner = runner.prev;
	// 	}

	// 	while (runner != null && runner.getX() > xPos) {
	// 		runner = runner.prev;
	// 	}

	// 	cursor.setY(runner.getY());
	// 	cursor.setX(runner.getX());
	// }

	// // some sample text
	// public void rightArrow(Rectangle cursor) {
	// 	System.out.println("Before right arrow pressed: " + cursorXPos);
	// 	if (nodeAfterCursor.next != null) {
	// 		cursorXPos = nodeAfterCursor.next.getX();
	// 	}
	// 	System.out.println("After right arrow: " + cursorXPos);
		
	// 	nodeBeforeCursor = nodeAfterCursor;
	// 	nodeAfterCursor = nodeAfterCursor.next;

	// 	cursor.setX(cursorXPos);
	// 	cursor.setY(cursorYPos);
	// }

	// /* for now because of our shitty design, we will pretend that the linked list
	//    is also the rendering engine, and have some rendering methods */
	// public void renderAfterInsertion(Text insertedText) {
	// 	int widthChange = (int) Math.round(insertedText.getLayoutBounds().getWidth());
	// 	TextNode runner = nodeAfterCursor;

	// 	cursorXPos = runner.getX() + widthChange;

	// 	System.out.println(runner.text.getText());

	// 	while (runner != null) {
	// 		runner.setX(runner.getX() + widthChange + 1);
	// 		runner = runner.next;
	// 	}
	// }

	// public void renderYah() {
	// 	int xPos = 5, yPos = 5;

	// 	TextNode runner = sentinel.next;
	// 	while (runner != null) {

	// 	}
	// }  

}