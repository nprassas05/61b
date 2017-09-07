package hw4.puzzle;

public class Board {
    private int N;
    private int[][] boardTiles;
    int hammingDistance;
    int manhattanDistance;

    public Board(int[][] tiles) {
        N = tiles.length;
        boardTiles = new int[N][N];
        setUpBoard(tiles);
    }

    private void setUpBoard(int[][] tiles) {
        hammingDistance = 0;
        manhattanDistance = 0;

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                boardTiles[i][j] = tiles[i][j];

                if (tiles[i][j] == 0) {
                    continue;
                }

                int expectedItem = expectedItem(i, j);
                int actualItem = boardTiles[i][j];

                if (expectedItem != actualItem) {
                    ++hammingDistance;

                    int expectedRow = expectedRow(actualItem);
                    int expectedColumn = expectedColumn(actualItem);

                    manhattanDistance += Math.abs(expectedRow - i) + 
                        Math.abs(expectedColumn - j);
                }       

            }
        }
    }

    public int tileAt(int i, int j) {
        if (i >= N || j >= N) {
            throw new IndexOutOfBoundsException("Invalid row and column provided");
        }
        return boardTiles[i][j];
    }

    public int size() {
        return N;
    }

    public int hamming() {
        return hammingDistance;
    }

    public int manhattan() {
        return manhattanDistance;
    }

    private int expectedItem(int i, int j) {
        if (i == (N - 1) && j == (N - 1)) {
            return 0;
        }
        return (N * i) + j + 1;
    }

    private int expectedRow(int item) {
        int row = item / N;

        if (item % N == 0) {
            row = row - 1;
        }
        return row;
    }

    private int expectedColumn(int item) {
        if (item % N == 0) {
            return N - 1;
        }
        
        return (item % N) - 1;
    }

    /* check if board is in the finished order */
    public boolean isGoal() {
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                int expectedItem = expectedItem(i, j);

                if (boardTiles[i][j] != expectedItem) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }

        Board yBoard = (Board) y;

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (tileAt(i, j) != yBoard.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%1d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
}