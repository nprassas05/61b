import javafx.scene.text.Text;

public class Move {
	private MoveType moveType;
	private Text text;

	public Move(Text t, MoveType m) {
		moveType = m;
		text = t;
	}

	public MoveType getMoveType() {
		return moveType;
	}

	public Text getText() {
		return text;
	}
}