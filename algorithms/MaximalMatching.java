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
   * First, the list of edges is sorted in a random order. Then the list is
   * scanned and if both vertices of an edge are not in the cover, they will
   * then be added to it.
   * 
   * This verification is made until we go through all the edge list.
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
