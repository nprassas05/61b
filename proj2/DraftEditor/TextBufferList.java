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

		public int getX() {
			return (int) text.getX();
		}

		public int getY() {
			return (int) text.getY();
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

	/* remove and return the current node in the list */
	public Text extractCurrentNode() {
		TextNode exNode = currentNode;

		if (currentNode.next != null) {
			currentNode.next.prev = currentNode.prev;
		}
		currentNode.prev.next = currentNode.next;
		currentNode = currentNode.prev;

		return exNode.text;
	}

	/* change the current node */
	public void setCurrentNode(TextNode t) {
		currentNode = t;
	}

	/* return the current node */
	public TextNode getCurrentNode() {
		return currentNode;
	}

	public TextNode getFirstNode() {
		return sentinel.next;
	}

	/* move the current node one place to the left */
	public void goLeft() {
		currentNode = currentNode.prev;
	}

	/* move current node to the right */
	public void goRight() {
		if (currentNode.next != null)
		currentNode = currentNode.next;
	}
}