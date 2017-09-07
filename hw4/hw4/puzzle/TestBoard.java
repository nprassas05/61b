package hw4.puzzle;

public class TestBoard {
	public static void main(String[] args) {
		Board b1 = new Board( 
			new int[][]{
				{0, 1, 3},
				{4, 2, 5},
				{7, 8, 6}
			}
		);

		System.out.println(b1.toString());
		System.out.println(b1.hamming());
		System.out.println(b1.manhattan());
		System.out.println(b1.isGoal());
	}
}