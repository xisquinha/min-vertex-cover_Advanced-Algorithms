import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class implements the algorithm of Maximal Matchin using a Vertex Cover
 * Problem.
 * MaximalMatching
 */
public class MaximalMatching {

  /**
   * The function findMaxMatching calculates a cover for a giver graph.
   * First, a random edge is selected from a list of edges in the graph. The two
   * vertices adjecents to the edge are added to the cover if neither of them are
   * already in it.
   * After that verification the edge is removed from the list.
   * 
   * Then another random edge is selected and the loop is repeated until no more
   * edges are left in the list.
   * 
   * @param graph
   * @return a list with the vertices in the cover
   */
  public ArrayList<Integer> findMaxMatching(Graph graph) {
    ArrayList<Integer> M = new ArrayList<>();

    List<Edge> edges = graph.getEdges();

    while (!edges.isEmpty()) {
      long seed = System.nanoTime();
      Random rand = new Random(seed);
      Edge e = edges.get(rand.nextInt(edges.size()));

      if (!M.contains(e.vertex1) && !M.contains(e.vertex2)) {
        M.add(e.vertex1);
        M.add(e.vertex2);
      }

      edges.remove(e);
    }

    return M;
  }
}
