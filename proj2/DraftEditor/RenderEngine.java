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
import java.util.HashSet;

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

	/* keep a stack for undoing and redoing operations */
	private UndoRedoBuffer urBuffer;
	Set<Text> deletedText = new HashSet<>();

	/* track the dimensions and margins of our visible window */
	private int windowHeight = 500;
	private int windowWidth = 500;
	private int marginLeft = 5;
	private int marginRight = 5;

	 /** The font and size of text to display on the screen */
    private int fontSize = 12;
    private String fontName = "Verdana";

	/* Text object simply used for resizing certain things */
    Text arbitraryText = new Text("a");
    int lineHeight = (int) arbitraryText.getLayoutBounds().getHeight();

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
		urBuffer = new UndoRedoBuffer();
		arbitraryText.setFont(Font.font(fontName, fontSize));
	}

	/* insert text object into linked list, textRoot, urBuffer, and re-render */
	public void insertText(Text t) {
		textBuffer.insert(t);
		urBuffer.push(new Move(t, MoveType.INSERT));

		render();
		textRoot.getChildren().add(t);
	}

	public void deleteText() {

	}

	/* undo the previous insertion or deletion move */
	public void undo() {
		Move lastMove = urBuffer.undo();

		if (lastMove == null) {	
			return;
		}

		MoveType type = lastMove.getMoveType();
		Text t = lastMove.getText();
		TextBufferList.TextNode tNode = textBuffer.supervisor(t);
		textBuffer.setCurrentNode(tNode);

		if (type == MoveType.INSERT) {
			textRoot.getChildren().remove(t);
			deletedText.add(t);
		}

		textBuffer.goLeft();
		render();
	}

	public void redo() {
		System.out.println("Doing a redo");
		Move redoMove = urBuffer.redo();

		if (redoMove == null) {
			System.out.println("no redoable moves stored thooooo");
			return;
		} 

		MoveType type = redoMove.getMoveType();
		Text t = redoMove.getText();
		TextBufferList.TextNode tNode = textBuffer.supervisor(t);
		textBuffer.setCurrentNode(tNode.prev);

		if (type == MoveType.INSERT) {
			textRoot.getChildren().add(t);
			deletedText.remove(t);
		}
		
		textBuffer.goRight();
		render();
	}

	public void render() {
		int currentX = 5;
		int currentY = 0;
		numberOfLines = 1;

		double maxPositionRight = windowWidth - marginRight - scrollBar.getLayoutBounds().getWidth();

		/* get the starting point and length of each word in text, storing
		   the result in wordLengthMap */
		wordLengthMap = getWordLengthMap();

		lastNodeOnEachLine.clear();

		/* use a runner node to iterate through the nodes in the text buffer */
		TextBufferList.TextNode runner = textBuffer.getFirstNode();

		while (runner != null) {
			if (deletedText.contains(runner.text)) {
				//System.out.println(runner.text.getText() + "was deleted though");
				runner = runner.next;
				continue;
			}

			if (isStartOfWord(runner.text)) {
				int length = wordLengthMap.get(runner.text);

				/* check if word length is too long to fit on current line */
				/* for now assuming we always have a 500 by 500 pixel window, but this will change */
				if (length + currentX > maxPositionRight) {
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
		// now just 500 once I realized that top and bottom margins are ZERO
		scrollBar.setMax(lineHeight * lastNodeOnEachLine.size() - windowHeight);
		//System.out.println("number of lines in file = " + lastNodeOnEachLine.size() + ", lineHeight = " + lineHeight);

		makeCursorVisible();
	}

	public HashMap<Text, Integer> getWordLengthMap() {
		TextBufferList.TextNode runner = textBuffer.getFirstNode();
		HashMap<Text, Integer> wordLengthMap = new HashMap<>();
		Text currentWord = null;

		while (runner != null) {
			if (deletedText.contains(runner.text)) {
				runner = runner.next;
				continue;
			}

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
			cursor.setY(0);
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
		int lineNum = Math.abs((int) yPos / lineHeight);
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

	/* cursor check variation, won't keep both */
	public void makeCursorVisible() {
		int cursorYPos = (int) (cursor.getY() + textRoot.getLayoutY());
		if (cursorYPos <= 0) {
			scrollBar.setValue(cursor.getY());
		} else if (cursorYPos >  windowHeight - lineHeight) { // if we are at all covering the last line
			scrollBar.setValue(cursor.getY() - windowHeight + lineHeight); // cursor.gety() - windowHeight + lineHeight
		}
	}

	/* re-render after changing either the width or height of the window */
	public void setWindowHeight(int h) {
		windowHeight = h;
		render();
	}

	public void setWindowWidth(int w) {
		windowWidth = w;
		scrollBar.setLayoutX(windowWidth - scrollBar.getLayoutBounds().getWidth());
		render();
	}
}
