import java.util.Observable;
/** 
 *  @author Josh Hug
 */

public class MazeCycles extends MazeExplorer {
    /* Inherits public fields: 
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private Maze maze; 
    boolean cycleFound = false;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        distTo[0] = 0;
    }

    private void dfs(int v) {
       if (cycleFound) return;

       marked[v] = true;
   
        for (int w : maze.adj(v)) {
            if (w == edgeTo[v]) continue;

            if (!marked[w]) {
                // edgeTo[w] = v;
                // announce();
                // distTo[w] = distTo[v] + 1;
                // dfs(w);             
            } else {
                cycleFound = true;
                System.out.println(distTo[v] + " " + distTo[w]);
                edgeTo[w] = v;
                announce();
                //distTo[w] = distTo[v] + 1;
                return;
            }
        }

        for (int w : maze.adj(v)) {
            if (w == edgeTo[v]) continue;

            if (!marked[w]) {
                edgeTo[w] = v;
                announce();
                distTo[w] = distTo[v] + 1;
                dfs(w);             
            }
        }
    }

    @Override
    public void solve() {
        dfs(0);
    }
}