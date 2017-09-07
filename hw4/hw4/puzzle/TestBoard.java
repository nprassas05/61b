package hw4.puzzle;

import java.util.ArrayList;
import java.util.Stack;

public class TestBoard {
	public static void main(String[] args) {
		Board b1 = new Board( 
			new int[][]{
				{0, 1, 3},
				{4, 2, 5},
				{7, 8, 6}
			}
		);

		//System.out.println("ytyth".equals(null));

		// System.out.println(b1.toString());
		// System.out.println(b1.hamming());
		// System.out.println(b1.manhattan());
		// System.out.println(b1.isGoal());

		//Solver t = new Solver(b1);
		ArrayList<Integer> a = new ArrayList<>();
		a.add(0, 9);

		Stack<Integer> s = new Stack<>();
		s.push(55);
		s.push(34);
		s.push(70);
		for (int n: s) System.out.println(n);
	}
}