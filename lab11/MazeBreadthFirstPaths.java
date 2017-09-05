import java.util.Observable;
import java.util.Queue;
import java.util.LinkedList;
/**
 *  @author Josh Hug
 */

public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze; 

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;       
    }

    /** Conducts a breadth first search of the maze starting at vertex x. */
    private void bfs(int source) {
        /* Your code here. */
        Queue<Integer> vertexQueue = new LinkedList<>();
        marked[source] = true;
        vertexQueue.offer(source);
        announce();

        while (!vertexQueue.isEmpty()) {
            int v = vertexQueue.poll();
            if (v == t) return;
            announce();

            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    vertexQueue.offer(w);           
                }
            }
        }
    }

    @Override
    public void solve() {
        bfs(s);
    }
}