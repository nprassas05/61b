import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.scene.text.Text;
import javafx.scene.text.Font;

public class FileHandler {
	public static TextBufferList formListFromFile(String fileName) {
		try {
			TextBufferList textBuffer = new TextBufferList();
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			int nextChar;

			while ((nextChar = reader.read()) != -1) {
				Text t = new Text("" + (char) nextChar);
				t.setFont(Font.font("Verdana", 12));
				textBuffer.insert(t);
			}

			reader.close();
			return textBuffer;

		} catch (FileNotFoundException ex) {
			return null;
		} catch (IOException ex) {
			return null;
		}
	}

	public static void saveFile(TextBufferList textBuffer, String fileName) {

	}
}