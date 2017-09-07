package hw4.puzzle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class Solver {
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private int hammingPriority;
        private int manhattanPriority;
        private SearchNode prev;

        public SearchNode(Board b, int m, SearchNode p) {
            board = b;
            moves = m;
            prev = p;
            hammingPriority = b.hamming() + moves;
            manhattanPriority = b.manhattan() + moves;
        }

        @Override
        public int compareTo(SearchNode node) {
            if (manhattanPriority > node.manhattanPriority) {
                return 1;
            } else if (manhattanPriority < node.manhattanPriority) {
                return -1;
            } else {
                return 0;
            }
        }

        public int moves() { return moves; }
    }

    SearchNode startNode;
    MinPQ<SearchNode> pq;
    LinkedList<Board> shortestPathList = new LinkedList<>();
    int moves;

    public Solver(Board initial) {
        pq = new MinPQ<>();
        startNode = new SearchNode(initial, 0, null);
        pq.insert(startNode);
        aStar();
    }

    /* solve the puzzle using the A* algorithm */
    public void aStar() {
        SearchNode sNode = null;

        while (!pq.isEmpty()) {
            sNode = pq.delMin();
            if (sNode.board.isGoal()) {
                //System.out.println(sNode.board.toString());
                break;
            }

            for (Board b: BoardUtils.neighbors(sNode.board)) {
                //System.out.println("look at neighbor");
                if (sNode.prev == null || !b.equals(sNode.prev.board)) {
                    int currentNumMoves = sNode.moves();
                    SearchNode t = new SearchNode(b, currentNumMoves + 1, sNode);
                    pq.insert(t);
                }
            }
        }

        moves = sNode.moves;
        /* record the shortest path in array list, moving backwards
           from the goal board */
        while (sNode != null) {
            shortestPathList.addFirst(sNode.board);
            sNode = sNode.prev;
        }
    }

    /* minimum number of moves to solve initial board */
    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        return shortestPathList;
    }

    // DO NOT MODIFY MAIN METHOD
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
    }
}