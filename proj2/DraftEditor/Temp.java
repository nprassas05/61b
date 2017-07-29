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

public class Temp extends Application {
    Group root;
    TextBufferList textBuffer;

    /* cursor will start at coordinates (5, 5) and have a width of 1 pixel, the 20 will change eventually */
    private final Rectangle cursor = new Rectangle(5.0, 5.0, 1, 20);

    /* keep an internal text rendering engine */
    private RenderEngine renderEngine;

    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;

    /** An EventHandler to handle keys that get pressed. */
    private class KeyEventHandler implements EventHandler<KeyEvent> {
        private static final int STARTING_FONT_SIZE = 12;
        private static final int STARTING_TEXT_POSITION_X = 5;
        private static final int STARTING_TEXT_POSITION_Y = 5;

        /* Text object simply used for resizing certain things */
        Text arbitraryText = new Text("a");

        /** The font and size of text to display on the screen */
        private int fontSize = STARTING_FONT_SIZE;
        private String fontName = "Verdana";

        KeyEventHandler(final Group root, int windowWidth, int windowHeight) {
            textBuffer = new TextBufferList();
            renderEngine = new RenderEngine(textBuffer, cursor);

            arbitraryText.setFont(Font.font(fontName, fontSize));
        }

        @Override
        public void handle(KeyEvent keyEvent) {
            if (keyEvent.getEventType() == KeyEvent.KEY_TYPED) {
                // Use the KEY_TYPED event rather than KEY_PRESSED for letter keys, because with
                // the KEY_TYPED event, javafx handles the "Shift" key and associated
                // capitalization.
                String characterTyped = keyEvent.getCharacter();
                
                /* check if user entered carriage return to go to new line */
                if (characterTyped.length() > 0 && characterTyped.charAt(0) == '\r') {
                    textBuffer.insert(new Text("\n"));
                    renderEngine.render();
                } else if (characterTyped.length() > 0 && characterTyped.charAt(0) != 8) {
                    // Ignore control keys, which have non-zero length, as well as the backspace
                    // key, which is represented as a character of value = 8 on Windows.

                    /* create new text object and set its formatting properties */
                    Text t = new Text(characterTyped);
                    t.setTextOrigin(VPos.TOP);
                    t.setFont(Font.font(fontName, fontSize));

                    /* add the text object to buffer as well as the root node */
                    textBuffer.insert(t);
                    renderEngine.render();
                    root.getChildren().add(t);


                    keyEvent.consume();
                }
            } else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                // Arrow keys should be processed using the KEY_PRESSED event, because KEY_PRESSED
                // events have a code that we can check (KEY_TYPED events don't have an associated
                // KeyCode).
                KeyCode code = keyEvent.getCode();
                
                if (code == KeyCode.BACK_SPACE) {
                    Text deletedText = textBuffer.extractCurrentNode();
                    root.getChildren().remove(deletedText);
                    renderEngine.render();
                } else if (code == KeyCode.LEFT) {
                    renderEngine.leftArrow();
                } else if (code == KeyCode.RIGHT) {
                    renderEngine.rightArrow();
                } else if (code == KeyCode.DOWN) {
                    // textBuffer.downArrow(cursor);
                } else if (code == KeyCode.UP) {
                    // textBuffer.upArrow(cursor);
                }
            }
        }
    }

    /* reset the x and y dimenions of the cursor according to values
       stored in our linked list */
    public void adjustCursor() {
        
    }   

    @Override
    public void start(Stage primaryStage) {
        // Create a Node that will be the parent of all things displayed on the screen.
        root = new Group();

        /* create continuous blinking cursor event handler */
        cursor.setHeight(new Text("a").getLayoutBounds().getHeight());
        CursorBlinkEventHandler cursorBlinker = new CursorBlinkEventHandler(cursor);

        /* add the cursor to root so it can be displayed first */
        root.getChildren().add(cursor);

        // The Scene represents the window: its height and width will be the height and width
        // of the window displayed.
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.WHITE);

        // To get information about what keys the user is pressing, create an EventHandler.
        // EventHandler subclasses must override the "handle" function, which will be called
        // by javafx.
        EventHandler<KeyEvent> keyEventHandler =
                new KeyEventHandler(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        // Register the event handler to be called for all KEY_PRESSED and KEY_TYPED events.
        scene.setOnKeyTyped(keyEventHandler);
        scene.setOnKeyPressed(keyEventHandler);

        primaryStage.setTitle("Editor Say WHATTTT");

        // This is boilerplate, necessary to setup the window where things are displayed.
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}