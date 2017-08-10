import javafx.scene.text.Text;

public class Move {
	private MoveType moveType;
	private Text text;
	
	/* text node that has either been inserted or deleted
       in the given move */
	public TextBufferList.TextNode tNode;

	/* keep a reference to the node before the node
	   that an operation refers to.  This will make
	   reinserting text into the text buffer much
	   easier upon a redo operation */
	public TextBufferList.TextNode markerNode;

	public Move(TextBufferList.TextNode t, TextBufferList.TextNode m, MoveType mType) {
		tNode = t;
		markerNode = m;
		moveType = mType;
		text = tNode.text;
	}
 
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
