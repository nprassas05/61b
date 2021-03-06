import javafx.application.Application;
import javafx.application.Application.Parameters;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.ScrollBar;
import javafx.geometry.Orientation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.List;

public class Editor extends Application {
    Group root;
    Group textRoot;

    TextBufferList textBuffer;

    /* the name of file being edited */
    private String fileName;

    /* cursor will start at coordinates (5, 5) and have a width of 1 pixel, the 20 will change eventually */
    private final Rectangle cursor = new Rectangle(5.0, 0.0, 1, 20);

    /* keep an internal text rendering engine */
    private RenderEngine renderEngine;

    private static int WINDOW_WIDTH = 500;
    private static int WINDOW_HEIGHT = 500;

    /** An EventHandler to handle keys that get pressed. */
    private class KeyEventHandler implements EventHandler<KeyEvent> {
        private static final int STARTING_FONT_SIZE = 12;
        private static final int STARTING_TEXT_POSITION_X = 5;
        private static final int STARTING_TEXT_POSITION_Y = 0;

        /* Text object simply used for resizing certain things */
        Text arbitraryText = new Text("a");

        /** The font and size of text to display on the screen */
        private int fontSize = STARTING_FONT_SIZE;
        private String fontName = "Verdana";

        KeyEventHandler(final Group root, int windowWidth, int windowHeight) {
            arbitraryText.setFont(Font.font(fontName, fontSize));
        }

        @Override
        public void handle(KeyEvent keyEvent) {
        	if (keyEvent.isShortcutDown()) {
        		KeyCode code = keyEvent.getCode();
        		
        		if (code == KeyCode.EQUALS || code == KeyCode.PLUS) {
        			fontSize += 4;
        			renderEngine.resize(4);
        		} else if (code == KeyCode.MINUS && fontSize >= 8) {
        			fontSize -= 4;
        			renderEngine.resize(-4);
        		} else if (code == KeyCode.S) {
                    FileHandler.saveFile(textBuffer, fileName);
                } else if (code == KeyCode.Z) { 
                    renderEngine.undo();
                } else if (code == KeyCode.Y) {
						 renderEngine.redo();
                }
        	} else if (keyEvent.getEventType() == KeyEvent.KEY_TYPED) {
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
                    renderEngine.insertText(t);
                    keyEvent.consume();
                }
            } else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                // Arrow keys should be processed using the KEY_PRESSED event, because KEY_PRESSED
                // events have a code that we can check (KEY_TYPED events don't have an associated
                // KeyCode).
                KeyCode code = keyEvent.getCode();
                
                if (code == KeyCode.BACK_SPACE && textBuffer.size() > 0) {
                    renderEngine.deleteText();
                } else if (code == KeyCode.LEFT) {
                    renderEngine.leftArrow();
                } else if (code == KeyCode.RIGHT) {
                    renderEngine.rightArrow();
                } else if (code == KeyCode.DOWN) {
                    renderEngine.downArrow();
                } else if (code == KeyCode.UP) {
                    renderEngine.upArrow();
                }
            }
        }
    }

    /** An event handler that displays the current position of the mouse whenever it is clicked. */
    private class MouseClickEventHandler implements EventHandler<MouseEvent> {
        MouseClickEventHandler() {}

        @Override
        public void handle(MouseEvent mouseEvent) {
            // Because we registered this EventHandler using setOnMouseClicked, it will only called
            // with mouse events of type MouseEvent.MOUSE_CLICKED.  A mouse clicked event is
            // generated anytime the mouse is pressed and released on the same JavaFX node.
            double mousePressedX = mouseEvent.getX();
            double mousePressedY = mouseEvent.getY();
            System.out.println("mouse y pos clicked = " + (mousePressedY - textRoot.getLayoutY()));
            renderEngine.handleMouseClick(mousePressedX, mousePressedY - textRoot.getLayoutY());
        }
    }

    private ScrollBar getNewScrollBar() {
         // Make a vertical scroll bar on the right side of the screen.
        ScrollBar scrollBar = new ScrollBar();
        scrollBar.setOrientation(Orientation.VERTICAL);
        // Set the height of the scroll bar so that it fills the whole window.
        scrollBar.setPrefHeight(WINDOW_HEIGHT);
        scrollBar.setLayoutX(WINDOW_WIDTH - scrollBar.getLayoutBounds().getWidth());

        // Set the range of the scroll bar.
        scrollBar.setMin(0);
        //scrollBar.setMax(5500);

        /** When the scroll bar changes position, change the height of Josh. */
        scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue,
                    Number newValue) {

                textRoot.setLayoutY(textRoot.getLayoutY() - newValue.doubleValue() + oldValue.doubleValue());
                System.out.println("layout y = " + textRoot.getLayoutY());
            }
        });

        return scrollBar;
    }

    private Scene getNewScene() {
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.WHITE);

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldScreenWidth,
                    Number newScreenWidth) {
                // Re-compute Allen's width.
                int newAllenWidth = newScreenWidth.intValue();
                renderEngine.setWindowWidth(newAllenWidth);
            }
        });

        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldScreenHeight,
                    Number newScreenHeight) {
                int newAllenHeight = newScreenHeight.intValue();
                renderEngine.setWindowHeight(newAllenHeight);
            }
        });

        return scene;
    }

    @Override
    public void start(Stage primaryStage) {
        // Create a Node that will be the parent of all things displayed on the screen.
        root = new Group();
        textRoot = new Group();

        /* create continuous blinking cursor event handler */
        cursor.setHeight(new Text("a").getLayoutBounds().getHeight());
        CursorBlinkEventHandler cursorBlinker = new CursorBlinkEventHandler(cursor);

        // The Scene represents the window: its height and width will be the height and width
        // of the window displayed.
        Scene scene = getNewScene();

        // To get information about what keys the user is pressing, create an EventHandler.
        // EventHandler subclasses must override the "handle" function, which will be called
        // by javafx.
        EventHandler<KeyEvent> keyEventHandler =
                new KeyEventHandler(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Register the event handler to be called for all KEY_PRESSED and KEY_TYPED events.
        scene.setOnKeyTyped(keyEventHandler);
        scene.setOnKeyPressed(keyEventHandler);
        scene.setOnMouseClicked(new MouseClickEventHandler());

        primaryStage.setTitle("Editor");

         // Make a vertical scroll bar on the right side of the screen.
        ScrollBar scrollBar = getNewScrollBar();

        // add nodes to their parents
        textRoot.getChildren().add(cursor);
        root.getChildren().addAll(textRoot, scrollBar);

        /* render the text in the file being opened if that file
           was existant beforehand */
        List<String> params = getParameters().getRaw();
        fileName = params.get(0);
        textBuffer = FileHandler.formListFromFile(fileName, textRoot);
        
        renderEngine = new RenderEngine(textBuffer, cursor, scrollBar, textRoot);
        renderEngine.render();

        System.out.println("layout y = " + textRoot.getLayoutY());

        // This is boilerplate, necessary to setup the window where things are displayed.
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Error: Please provide one argument for file to edit");
            System.exit(1);
        }

        launch(args);
    }
}
