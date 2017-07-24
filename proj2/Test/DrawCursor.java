import javafx.application.Application;
import javafx.animation.KeyFrame;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;
import java.util.ArrayList;


public class DrawCursor extends Application {
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;

    Group root;
    private final Rectangle cursor = new Rectangle(1, 20); // size dimensions

    private static final int STARTING_FONT_SIZE = 20;
    private static final int STARTING_TEXT_POSITION_X = 5;
    private static final int STARTING_TEXT_POSITION_Y = 5;
    private String fontName = "Verdana";

    /** The Text to display on the screen. */
    private static Text displayText = new Text(STARTING_TEXT_POSITION_X, STARTING_TEXT_POSITION_Y, "|");

    /** An EventHandler to handle changing the color of the rectangle. */
    private class RectangleBlinkEventHandler implements EventHandler<ActionEvent> {
        int currentIndex = 0;
        String[] blinkOptions = {"", "|"};

        RectangleBlinkEventHandler() {
            changeColor();
        }

        private void changeColor() {
            if (currentIndex == 1) {
                cursor.setHeight(20);
                cursor.setWidth(1);
                currentIndex = 0;
            } else {
                cursor.setHeight(0);
                cursor.setWidth(0);
                currentIndex = 1;
            }
        }

        @Override
        public void handle(ActionEvent event) {
            changeColor();
        }
    }

    /** Make the cursor symbol blink **/
    public void makeRectangleColorChange() {
        // Create a Timeline that will call the "handle" function of RectangleBlinkEventHandler
        // every 1 second.
        final Timeline timeline = new Timeline();

        // The cursor should continue blinking forever.
        timeline.setCycleCount(Timeline.INDEFINITE);
        RectangleBlinkEventHandler cursorChange = new RectangleBlinkEventHandler();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), cursorChange);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
    
    @Override
    public void start(Stage primaryStage) {
        displayText.setTextOrigin(VPos.TOP);
        displayText.setFont(Font.font(fontName, STARTING_FONT_SIZE));
         
        // Create a Node that will be the parent of all things displayed on the screen.
        root = new Group();
        // The Scene represents the window: its height and width will be the height and width
        // of the window displayed.
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.WHITE);

        cursor.setX(5);
        cursor.setY(5);
        root.getChildren().add(cursor);
        makeRectangleColorChange();

        primaryStage.setTitle("Draw Cursor in Middle of Screen");

        // This is boilerplate, necessary to setup the window where things are displayed.
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
