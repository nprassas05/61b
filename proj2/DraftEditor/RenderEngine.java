import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ScrollBar;
import javafx.scene.shape.Rectangle;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;

public class RenderEngine {
	private TextBufferList textBuffer;

	/* use a map for tracking the start of each word in text entered by user,
	   as well as the length of each word corresponding to that starting letter */
	private Map<Text, Integer> wordLengthMap;
	private Rectangle cursor;
	ScrollBar scrollBar;
	Group textRoot;
	ArrayList<TextBufferList.TextNode> lastNodeOnEachLine = new ArrayList<>();
	int currentLine = 0;
	int numberOfLines = 1;
	int numLinesCovered = 0;  // number of lines hidden from the top by scroll bar

	 /** The font and size of text to display on the screen */
    private int fontSize = 12;
    private String fontName = "Verdana";

	/* Text object simply used for resizing certain things */
    Text arbitraryText = new Text("a");
    int lineHeight = (int) arbitraryText.getLayoutBounds().getHeight();

	public RenderEngine(TextBufferList tb) {
		textBuffer = tb;
		wordLengthMap = new HashMap<>();
		arbitraryText.setFont(Font.font(fontName, fontSize));
	}

	public RenderEngine(TextBufferList tb, Rectangle r) {
		textBuffer = tb;
		cursor = r;
		wordLengthMap = new HashMap<>();
		arbitraryText.setFont(Font.font(fontName, fontSize));
	}

	public RenderEngine(TextBufferList tb, Rectangle r, ScrollBar s, Group root) {
		textBuffer = tb;
		cursor = r;
		scrollBar = s;
		textRoot = root;
		wordLengthMap = new HashMap<>();
		arbitraryText.setFont(Font.font(fontName, fontSize));
	}

	public void render() {
		int currentX = 5;
		int currentY = 5;
		numberOfLines = 1;

		/* get the starting point and length of each word in text, storing
		   the result in wordLengthMap */
		wordLengthMap = getWordLengthMap();

		lastNodeOnEachLine.clear();

		/* use a runner node to iterate through the nodes in the text buffer */
		TextBufferList.TextNode runner = textBuffer.getFirstNode();

		while (runner != null) {
			if (isStartOfWord(runner.text)) {
				int length = wordLengthMap.get(runner.text);

				/* check if word length is too long to fit on current line */
				/* for now assuming we always have a 500 by 500 pixel window, but this will change */
				if (length + currentX > 495 - scrollBar.getLayoutBounds().getWidth()) {
					currentX = 5;
					currentY += lineHeight; /////////// @@@@@@@@
					lastNodeOnEachLine.add(runner.prev);
					++numberOfLines;
				}
			}

			runner.setX(currentX);
			runner.setY(currentY);

			/* check for new line character */
			if (runner.firstChar() == '\n') {
				currentX = 5;
				currentY += lineHeight; /////////// @@@@@@@@
				lastNodeOnEachLine.add(runner);
				runner = runner.next;
				++numberOfLines;
				
				continue;
			}

			currentX += runner.getWidth() + 1;

			if (runner.next == null) {
				lastNodeOnEachLine.add(runner);
			}

			runner = runner.next;
		}

		adjustCursor();

		// 490 accounts for the 5 pixel vertical margin on top and bottom, 500 - (2 * 5)
		scrollBar.setMax(lineHeight * lastNodeOnEachLine.size() - 490);
		//System.out.println("number of lines in file = " + lastNodeOnEachLine.size() + ", lineHeight = " + lineHeight);

		makeCursorVisible();

	}

	public HashMap<Text, Integer> getWordLengthMap() {
		TextBufferList.TextNode runner = textBuffer.getFirstNode();
		HashMap<Text, Integer> wordLengthMap = new HashMap<>();
		Text currentWord = null;

		while (runner != null) {
			if (runner.firstChar() != ' ' && runner.prev.firstChar() == ' ') {
				currentWord = runner.text;
				wordLengthMap.put(runner.text, runner.getWidth());
			} else if (runner.firstChar() != ' ' && currentWord != null) {
				int currentLength = wordLengthMap.get(currentWord);
				int runnerLength = runner.getWidth();
				wordLengthMap.put(currentWord, currentLength + runnerLength + 1); // trying extra 1 for pixel between letters
			}

			runner = runner.next;
		}

		return wordLengthMap;
	}

	public boolean isStartOfWord(Text t) {
		return wordLengthMap.containsKey(t);
	}

	/* return the node between two nodes that is closer
	   to a given x position */
	public TextBufferList.TextNode closerNode(
							TextBufferList.TextNode nodeA, 
							TextBufferList.TextNode nodeB,
							int xPos) {

		int distA = Math.abs(nodeA.getX() + nodeA.getWidth() - xPos);
		int distB = Math.abs(nodeB.getX() + nodeB.getWidth() - xPos);

		return distA < distB ? nodeA : nodeB;
	}

	/* move an iteration node on a line until it gets close enough to
	   the target xPosition, and change the cursor and current node
	   values accordingly */
	public void moveCursor(TextBufferList.TextNode runner, int targetXPos) {
		while (runner.getX() > targetXPos) {
			runner = runner.prev;
		}

		TextBufferList.TextNode closestNode = closerNode(runner, runner.prev, targetXPos);
		textBuffer.setCurrentNode(closestNode);
		adjustCursor();
	}

	/* change the cursor position and text buffer after
	   the user presses left arrow key */
	public void leftArrow() {
		cursor.setX(textBuffer.currentX());
		cursor.setY(textBuffer.currentY());
		textBuffer.goLeft();
	}

	/* change cursor and text buffer after right arrow key */
	public void rightArrow() {
		textBuffer.goRight();
		adjustCursor();
	}

	/* change cursor and text buffer after up arrow key */
	public void upArrow() {
		if (currentLine <= 0) {
			return;
		}

		int currentX = (int) cursor.getX();
		TextBufferList.TextNode runner = lastNodeOnEachLine.get(currentLine - 1);
		moveCursor(runner, currentX);

		/// 
		makeCursorVisible();
	}

	/* down arrow key */
	public void downArrow() {
		if (currentLine >= lastNodeOnEachLine.size() - 1) {
			return;
		}

		int currentX = (int) cursor.getX();
		TextBufferList.TextNode runner = lastNodeOnEachLine.get(currentLine + 1);
		moveCursor(runner, currentX);

		///
		makeCursorVisible();
	}

	/* adjust the cursor to be positioned correctly
	   in the text document */
	public void adjustCursor() {
		TextBufferList.TextNode currNode = textBuffer.getCurrentNode();

		if (textBuffer.size() <= 0 || currNode == textBuffer.frontSentinel) {
			cursor.setX(5);
			cursor.setY(5);
		} else if (currNode.text.getText().charAt(0) == '\n') {
			cursor.setX(5);
			cursor.setY(currNode.getY() + lineHeight);
		} else {
			int xPos = currNode.getX() + currNode.getWidth() + 1;
			cursor.setX(xPos);
			cursor.setY(currNode.getY());
		}

		setCurrentLine(); // do I need this? examine further
	}

	public void setCurrentLine() {
		currentLine = (int) cursor.getY() / lineHeight;
	}

	/* get the closest line according to a y position */
	public int getClosestLineNum(double yPos) {
		numLinesCovered = (int) (-(textRoot.getLayoutY() + 5) / lineHeight);

		int lineNum = Math.abs((int) (yPos - 5) / lineHeight);
		return lineNum < numberOfLines ? lineNum : numberOfLines - 1;
	}

	/* test method for seeing current line, remove later */
	public void printCurrentLine() {
		System.out.println(currentLine);
	}

	/* change the cursor to be closest to where the user mouse clicked */
	public void handleMouseClick(double clickedX, double clickedY) {
		int lineNum = getClosestLineNum(clickedY);
		System.out.println("line number = " + lineNum);
		TextBufferList.TextNode runner = lastNodeOnEachLine.get(lineNum);

		moveCursor(runner, (int) clickedX);
	}

	/* increase or decrease the font size of the text by the given change
	   amount.  A negative number would be provided by the Editor class
	   in the case of decreasing the font size */
	public void resize(int changeAmount) {
		fontSize += changeAmount;

		for (Text t: textBuffer) {
			t.setFont(Font.font(fontName, fontSize));
		}

		arbitraryText.setFont(Font.font(fontName, fontSize));
		lineHeight = (int) arbitraryText.getLayoutBounds().getHeight();
		cursor.setHeight(arbitraryText.getLayoutBounds().getHeight());

		render();
	}

	/* check if cursor is within the visible window */
	public boolean cursorIsVisible() {
		int cusorYPos = (int) (cursor.getY() + textRoot.getLayoutY());
		if (cusorYPos > 495 || cusorYPos < 5) {
			return false;
		}
		return true;
	}

	/* cursor check variation, won't keep both */
	public void makeCursorVisible() {
		int cursorYPos = (int) (cursor.getY() + textRoot.getLayoutY());
		if (cursorYPos < 5) {
			scrollBar.setValue(cursor.getY() - 5);
		} else if (cursorYPos > 495) {
			scrollBar.setValue(cursor.getY() - 480);
		}
	}
}
