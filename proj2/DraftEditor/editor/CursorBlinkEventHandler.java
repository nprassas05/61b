import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class CursorBlinkEventHandler implements EventHandler<ActionEvent> {
	int currentIndex = 0;
	int cursorWidth = 1;
	Rectangle cursor;

	CursorBlinkEventHandler(Rectangle cursor) {
		this.cursor = cursor;
    	continiousBlink();
    }

	/* change the cursor to either appear or dissapear depending
	   on its current state */
	private void blink() {
	    if (currentIndex == 1) {
	        cursor.setWidth(1);
	    } else {
	        cursor.setWidth(0);
	    }

	    currentIndex = (currentIndex + 1) % 2;
    }

    @Override
        public void handle(ActionEvent event) {
        blink();
    }

     /** Make the cursor symbol blink every 0.5 seconds **/
    public void continiousBlink() {
        final Timeline timeline = new Timeline();

        // The cursor should continue blinking forever.
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), this);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
}