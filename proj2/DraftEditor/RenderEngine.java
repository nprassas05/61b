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
	ArrayList<TextBufferList.TextNode> lastNodeOnEachLine = new ArrayList<>();
	int currentLine = 0;
	int numberOfLines = 1;

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
				if (length + currentX > 495) {
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

	public boolean tooLongForCurrentLine(Text t) {
		int length = wordLengthMap.get(t);
		return false;
	}

	/* change the cursor position and text buffer after
	   the user presses left arrow key */
	public void leftArrow() {
		TextBufferList.TextNode currNode = textBuffer.getCurrentNode();
		cursor.setX(currNode.getX());
		cursor.setY(currNode.getY());
		
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
		while (runner.getX() > currentX) {
			runner = runner.prev;
		}

		textBuffer.setCurrentNode(runner);
		adjustCursor();
	}

	/* down arrow key */
	public void downArrow() {
		if (currentLine >= lastNodeOnEachLine.size() - 1) {
			return;
		}

		int currentX = (int) cursor.getX(); System.out.println("currentX = " + currentX);
		TextBufferList.TextNode runner = lastNodeOnEachLine.get(currentLine);
		
		runner = runner.next;
		while (runner.getX() < currentX && runner.next != null) {
			// System.out.println("node text " + runner.text.getText() + " " + runner.getX());
			runner = runner.next;
		}

		textBuffer.setCurrentNode(runner);
		adjustCursor();

	}

	/* adjust the cursor to be positioned correctly
	   in the text document */
	public void adjustCursor() {
		TextBufferList.TextNode currNode = textBuffer.getCurrentNode();

		if (currNode.text.getText().charAt(0) == '\n') {
			cursor.setX(5);
			cursor.setY(currNode.getY() + lineHeight);
		} else {
			int xPos = currNode.getX() + currNode.getWidth() + 1;
			cursor.setX(xPos);
			cursor.setY(currNode.getY());
		}

		setCurrentLine();
	}

	public void setCurrentLine() {
		currentLine = (int) cursor.getY() / lineHeight;
	}

	/* get the closest line according to a y position */
	public int getClosestLineNum(double yPos) {
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
		System.out.println(lineNum);
		TextBufferList.TextNode runner = lastNodeOnEachLine.get(lineNum);
		
		while (runner.getX() > clickedX) {
			runner = runner.prev;
		}

		textBuffer.setCurrentNode(runner);
		adjustCursor();
	}
}