import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

public class TextBufferList implements Iterable<Text> {
	private int size;
	
	/* test, keep a hashmap for quick retrieval of a node */
	public Map<Text, TextNode> nodeMap = new HashMap<>();

	/* internal linked list nodes, which will be text objects in this case */
	public class TextNode {
		public Text text;
		public boolean deleted = false;

		public TextNode next;
		public TextNode prev;

		public TextNode(Text t) {
			this(t, null, null);
		}

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

	/* frontSentinel node for convenience */
	public TextNode frontSentinel;

	/* textnode before the cursor, where text will be inserted after */
	private TextNode currentNode;

	/* frontSentinel will initially point to null */
	public TextBufferList() {
		frontSentinel = new TextNode(new Text(5, 5, " "), null, null);
		currentNode = frontSentinel;
		size = 0;
	}
	
	/* insert a new text object after the current cursor position */
	public void insert(Text text) {
		TextNode t = new TextNode(text, currentNode.next, currentNode);
		currentNode.next = t;
		currentNode = t;
		++size;

		nodeMap.put(text, t);
	}

	/* overloaded insert method, inserting a created node as opposed
	 * to text objects
	 */
	public void insert(TextNode tNode) {
		tNode.next = currentNode.next;
		tNode.prev = currentNode;

		currentNode.next = tNode;
		currentNode = tNode;
		++size;
	}

	/* remove and return the current node in the list */
	public Text extractCurrentNode() {
		TextNode exNode = currentNode;

		if (currentNode.next != null) {
			currentNode.next.prev = currentNode.prev;
		}
		currentNode.prev.next = currentNode.next;
		currentNode = currentNode.prev;
		--size;

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
		return frontSentinel.next;
	}

	/* return the node containing a particular text object */
	public TextNode supervisor(Text t) {
		return nodeMap.get(t);
	}

	/* move the current node one place to the left */
	public void goLeft() {
		if (currentNode != frontSentinel) {
			currentNode = currentNode.prev;
		}
	}

	/* move current node to the right */
	public void goRight() {
		if (currentNode.next != null) {
			currentNode = currentNode.next;
		}
	}

	public int size() {
		return size;
	}

	/* return the x and y coordinates of the current node */
	public int currentX() { return currentNode.getX(); }
	public int currentY() { return currentNode.getY(); }

	/* class to iterate through each text node */
	private class TextBufferIterator implements Iterator<Text> {
		TextNode runner;

		public TextBufferIterator() {
			runner = frontSentinel.next;
		}

		public Text next() {
			Text t = runner.text;
			runner = runner.next;
			return t;
		}

		public boolean hasNext() {
			return runner != null;
		}
	}

	/* return iterator */
	@Override
	public Iterator<Text> iterator() {
		return new TextBufferIterator();
	}
}
