package algorithms;

import java.util.*;

/**
 * This class implements the algorithm of Maximal Matchin using a Vertex Cover
 * Problem.
 * algorithms.MaximalMatching
 */
public class MaximalMatching {

  /**
   * The function findMaxMatching calculates a cover for a giver graph.
   * First, a random edge is selected from a list of edges in the graph. The two
   * vertices adjacent to the edge are added to the cover if neither of them are
   * already in it.
   * After that verification the edge is removed from the list.
   * 
   * Then another random edge is selected and the loop is repeated until no more
   * edges are left in the list.
   * 
   * @param graph - the graph whose vertex cover will be calculated
   * @return a list with the vertices in the cover
   */
  public ArrayList<Integer> findMaxMatching(GraphMatching graph) {
    Set<Integer> M = new HashSet<>();

    List<Edge> edges = new ArrayList<>(graph.getEdges());

    Collections.shuffle(edges);

    for (Edge e : edges) {
      if (!M.contains(e.vertex1) && !M.contains(e.vertex2)) {
        M.add(e.vertex1);
        M.add(e.vertex2);
      }
    }

    return new ArrayList<>(M);
  }
}
