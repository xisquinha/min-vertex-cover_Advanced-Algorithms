package algorithms;

import java.util.*;

/**
 * This class implements the algorithm of Maximal Matchin using a Vertex Cover
 * Problem.
 * algorithms.MaximalMatching
 */
public class MaximalMatching implements VertexCoverAlgorithm {

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
  @Override
  public ArrayList<Integer> findVertexCover(Graph graph) {
    GraphMatching graphMatching = (GraphMatching) graph;

    Set<Integer> vertexCover = new HashSet<>();

    List<Edge> edges = new ArrayList<>(graphMatching.getEdges());

    Collections.shuffle(edges);

    for (Edge e : edges) {
      if (!vertexCover.contains(e.vertex1) && !vertexCover.contains(e.vertex2)) {
        vertexCover.add(e.vertex1);
        vertexCover.add(e.vertex2);
      }
    }

    return new ArrayList<>(vertexCover);
  }

  @Override
  public String getAlgorithmName() {
    return "Maximal Matching";
  }
}
