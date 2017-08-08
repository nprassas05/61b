import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.Group;
import javafx.geometry.VPos;

public class FileHandler {
	/* form a text buffer list from the contents of a file, or
	   return an empty list if that file does not exist */
	public static TextBufferList formListFromFile(String fileName, Group root) {
		TextBufferList textBuffer = new TextBufferList();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			int nextChar;

			while ((nextChar = reader.read()) != -1) {
				Text t = new Text("" + (char) nextChar);
				t.setTextOrigin(VPos.TOP);
				t.setFont(Font.font("Verdana", 12));
				textBuffer.insert(t);
				root.getChildren().add(t);
			}

			textBuffer.setCurrentNode(textBuffer.frontSentinel);
			
			reader.close();
			return textBuffer;

		} catch (FileNotFoundException ex) {
			//System.out.println("File not found exception occured");
		} catch (IOException ex) {
			System.out.println("IOException occured");
		}

		return textBuffer;
	}

	/* safe the contents of a text buffer list to a file */
	public static void saveFile(TextBufferList textBuffer, String fileName) {
		try {
			FileWriter writer = new FileWriter(fileName);
			for (Text t: textBuffer) {
				writer.write(t.getText().charAt(0));
			}
			writer.close();
		} catch (IOException ex) {

		}
	}
}