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

public class RenderEngine {
	private TextBufferList textBuffer;

	/* use a map for tracking the start of each word in text entered by user,
	   as well as the length of each word corresponding to that starting letter */
	private Map<Text, Integer> wordLengthMap;

	public RenderEngine(TextBufferList tb) {
		textBuffer = tb;
		wordLengthMap = new HashMap<>();
	}

	public void render() {
		int currentX = 5;
		int currentY = 5;

		/* get the starting point and length of each word in text, storing
		   the result in wordLengthMap */
		wordLengthMap = getWordLengthMap();

		/* use a runner node to iterate through the nodes in the text buffer */
		TextBufferList.TextNode runner = textBuffer.getFirstNode();

		while (runner != null) {
			if (isStartOfWord(runner.text)) {
				int length = wordLengthMap.get(runner.text);

				//System.out.println("(" + runner.text.getText() + ", " + length + ", " + currentX + ") ");

				/* check if word length is too long to fit on current line */
				/* for now assuming we always have a 500 by 500 pixel window, but this will change */
				if (length + currentX > 495) {
					currentX = 5;
					currentY += 30; /////////// @@@@@@@@
					//System.out.println(runner.firstChar() + " is too long");
				}
			}

			
			runner.setX(currentX);
			runner.setY(currentY);
			currentX += runner.getWidth() + 1;

			//System.out.println("currentX = " + currentX);
			
			runner = runner.next;
		}
		//sSystem.out.println();
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
}