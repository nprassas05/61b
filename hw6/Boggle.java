import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;

public class Boggle {
	private char[][] boggleBoard;
	Set<String> foundWords = new HashSet<>();
	TrieST<Integer> trieDict = new TrieST<>();
	boolean[][] visited;
	private MaxPQ<SpecialString> wordPQ;
	private List<Position>[][] neighbors;

	int numRows;
	int numColumns;
	int k;

	public Boggle(char[][] b, List<String> wordList, int k) {
		boggleBoard = b;
		numRows = b.length;
		numColumns = b[0].length;
		this.k = k;
		visited = new boolean[numRows][numColumns];
		neighbors = (List<Position>[][]) new List[numRows][numColumns];
		fillNeighbors(numRows, numColumns);
		wordPQ = new MaxPQ<>();

		for (String s: wordList) {
			trieDict.put(s, s.length());
		}
	}

	/* run a depth first search on each character cube to
	   get all the words */
	public void solve() {
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				//resetVisited();
				depthFirstSolve(i, j, "");
				

				// for (int y = 0; y < numRows; y++) {
				// 	for (int b = 0; b < numColumns; b++) {
				// 		System.out.print(visited[y][b] + " ");
				// 	}
				// 	System.out.println();
				// }
				// System.out.println();
				//System.out.println("-------------------------------------\n");
				
			}
		}

		int i = k;
		while (i-- > 0) {
			System.out.println(wordPQ.delMax());
		}
	}

	void depthFirstSolve(int i, int j, String s) {
		visited[i][j] = true;

		char c = boggleBoard[i][j];
		s += c;
		//System.out.println("we have " + s);
		if (((Queue<String>) trieDict.keysWithPrefix(s)).size() == 0) {
			visited[i][j] = false;
			//System.out.println(i + ", " + j + " no go: " + s);
			return;
		}

		if (trieDict.contains(s) && !foundWords.contains(s)) {
			//System.out.println("adding " + s);
			wordPQ.insert(new SpecialString(s));
			foundWords.add(s);
		} 
		
		List<Position> neibs = neighbors[i][j];
		for (Position p: neibs) {
				//System.out.print("neib of " + i + "" + j + ": " + p.row + "" + p.column);
				if (!visited[p.row][p.column]) {
					//System.out.println("--> not visited yet good");
					depthFirstSolve(p.row, p.column, s);
				} else {
					//System.out.println("--> shucks already visited");
				}
		}
		visited[i][j] = false;
	}

	/* on a boggle board, any cube letter can have at most
	   8 neighbors.  We will check the neighboring positions
	   for a given starting position, and return an array
	   of neighbors whose indices do not fall out of the bounds
	   of the board */
	public List<Position> getValidNeighbors(int i, int j) {
		List<Position> neighbors = new ArrayList<>();

		int[] iPosNeighbors = new int[8];
		int[] jPosNeighbors = new int[8];

		iPosNeighbors[0] = i; jPosNeighbors[0] = j - 1;
		iPosNeighbors[1] = i - 1; jPosNeighbors[1] = j - 1;
		iPosNeighbors[2] = i - 1; jPosNeighbors[2] = j;
		iPosNeighbors[3] = i - 1; jPosNeighbors[3] = j + 1;
		iPosNeighbors[4] = i; jPosNeighbors[4] = j + 1;
		iPosNeighbors[5] = i + 1; jPosNeighbors[5] = j + 1;
		iPosNeighbors[6] = i + 1; jPosNeighbors[6] = j;
		iPosNeighbors[7] = i + 1; jPosNeighbors[7] = j - 1;

		for (int t = 0; t < 8; ++t) {
			int iPos = iPosNeighbors[t];
			int jPos = jPosNeighbors[t];

			if (iPos >= 0 && iPos < numColumns && jPos >= 0 && jPos < numRows) {
				neighbors.add(new Position(iPos, jPos));
			}
		}

		return neighbors;	
	}

	/* on a boggle board, any cube letter can have at most
	   8 neighbors.  We will check the neighboring positions
	   for a given starting position, and return an array
	   of neighbors whose indices do not fall out of the bounds
	   of the board */
	public static List<Position> getHUH(int i, int j) {
		List<Position> neighbors = new ArrayList<>();

		int[] iPosNeighbors = new int[8];
		int[] jPosNeighbors = new int[8];

		iPosNeighbors[0] = i; jPosNeighbors[0] = j - 1;
		iPosNeighbors[1] = i - 1; jPosNeighbors[1] = j - 1;
		iPosNeighbors[2] = i - 1; jPosNeighbors[2] = j;
		iPosNeighbors[3] = i - 1; jPosNeighbors[3] = j + 1;
		iPosNeighbors[4] = i; jPosNeighbors[4] = j + 1;
		iPosNeighbors[5] = i + 1; jPosNeighbors[5] = j + 1;
		iPosNeighbors[6] = i + 1; jPosNeighbors[6] = j;
		iPosNeighbors[7] = i + 1; jPosNeighbors[7] = j - 1;

		for (int t = 0; t < 8; ++t) {
			int iPos = iPosNeighbors[t];
			int jPos = jPosNeighbors[t];

			if (iPos >= 0 && iPos < 4 && jPos >= 0 && jPos < 4) {
				neighbors.add(new Position(iPos, jPos));
			}
		}

		return neighbors;	
	}

	public void resetVisited() {
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				visited[i][j] = false;
			}
		}
	}

	public void fillNeighbors(int numRows, int numColumns) {
		for (int i = 0; i < numRows; ++i) {
			for (int j = 0; j < numColumns; ++j) {
				neighbors[i][j] = getValidNeighbors(i, j);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		Scanner input = new Scanner(System.in);

		char[][] arr = new char[100][100];
		for (int i = 0; i  <100; i++) {
			String s = input.nextLine();
			for (int j = 0; j < 100; j++) {
				arr[i][j] = s.charAt(j);
			}
		}

		// char[][] arr2 = {
		// 	{'n','e','s','s'},
		// 	{'t','a','c','k'},
		// 	{'b','m','u','h'},
		// 	{'e','s','f','t'}
		// };

		List<String> dict = Files.readAllLines(Paths.get("words"));

		Boggle boggle = new Boggle(arr, dict, 7);
		boggle.solve();

		// for (int i = 0; i < 4; i++) {
		// 	for (int j = 0; j < 4; j++) {
		// 		System.out.print(i + ", " + j + ": ");
		// 		List<Position> pList = getHUH(i, j);
		// 		for (Position pos: pList) {
		// 			System.out.print(pos.row + "" + pos.column + " ");
		// 		}
		// 		System.out.println();
		// 	}
		// }
		
	}
}