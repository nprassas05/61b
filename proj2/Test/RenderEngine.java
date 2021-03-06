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

public class RenderEngine {
	public void renderAfterLeftArrow(TextLinkedList textBuffer) {
		TextLinkedList.TextNode runnerNode = textBuffer.nodeAfterCursor();

		while (runnerNode != null) {
			runnerNode.setX(runnerNode.getX() + 1);
			runnerNode = runnerNode.next;
		}
	}

	public void renderWithRightArrowPress() {

	}

	public void renderWithDownArrowPress() {

	}

	public void renderWithUpArrowPress() {

	}
}