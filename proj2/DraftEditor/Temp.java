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

import java.util.List;
import java.util.ArrayList;;

public class Temp extends Application {
	TextBufferList textBuffer;
	RenderEngine renderEngine;

    @Override
    public void start(Stage primaryStage) {
    	Group root = new Group();

    	textBuffer = new TextBufferList();
    	renderEngine = new RenderEngine(textBuffer);

    	Text t1 = new Text("a");
    	Text t2 = new Text("b");
    	Text t3 = new Text("c");

    	t1.setTextOrigin(VPos.TOP);
    	t2.setTextOrigin(VPos.TOP);
    	t3.setTextOrigin(VPos.TOP);

    	textBuffer.insert(t1); textBuffer.insert(t2); textBuffer.insert(t3);
    	renderEngine.render();
    	root.getChildren().addAll(t1, t2, t3);

    	Scene scene = new Scene(root, 500, 500, Color.WHITE);
    	primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}