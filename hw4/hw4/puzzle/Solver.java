package hw4.puzzle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;
import java.util.ArrayList;

public class Solver {
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private int priority;
        private SearchNode prev;

        public SearchNode(Board b) {
            board = b;
        }

        @Override
        public int compareTo(SearchNode node) {
            return 0;
        }
    }

    SearchNode startNode;
    MinPQ<SearchNode> pq;

    public Solver(Board initial) {
        pq = new MinPQ<>();
        startNode = new SearchNode(initial);
        pq.insert(startNode);
        aStar();
    }

    /* solve the puzzle using the A* algorithm */
    public void aStar() {

    }

    /* minimum number of moves to solve initial board */
    public int moves() {
        return 0;
    }

    public Iterable<Board> solution() {
        return new ArrayList<>();
    }

    // DO NOT MODIFY MAIN METHOD
    /* Uncomment this method once your Solver and Board classes are ready.
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        Solver solver = new Solver(initial);
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution()) {
            StdOut.println(board);
       }
    }*/
}