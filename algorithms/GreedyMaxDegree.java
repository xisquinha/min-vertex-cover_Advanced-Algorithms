package algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Greedy max-degree algorithm for the unweighted
 * Minimum Vertex Cover problem.
 * Always choose the vertex with the highest number of still-uncovered
 * incident edges, adds it to the cover, and updates the degrees of its
 * adjacent vertices, until no uncovered edge remains.
 * 
 * GreedyMaxDegree
 */
public class GreedyMaxDegree {

  /**
   * Computes a vertex cover with the greedy max-degree algorithm.
   * 
   * @param graph
   * @return the cover
   */
  public ArrayList<Integer> findGreedyMaxDegree(GraphGreedy graph) {
    ArrayList<Integer> cover = new ArrayList<>();

    int numVertices = graph.getNumVertices();
    List<Edge> edges = graph.getEdges();

    int[] currentDegrees = graph.getDegreesCopy();
    boolean[] inCover = new boolean[numVertices];

    while (true) {
      int[] max = maxDegree(numVertices, inCover, currentDegrees);
      int maxVertex = max[0];
      int maxDegree = max[1];

      // stop when no more vertices left or every remaining vertex has degree 0
      // (theres no more edges)
      if (maxVertex == -1 || maxDegree == 0) {
        break;
      }

      cover.add(maxVertex);
      inCover[maxVertex] = true;
      currentDegrees[maxVertex] = 0; // all edges are covered

      updateNeighborsDegree(edges, maxVertex, inCover, currentDegrees);
    }

    return cover;
  }

  /**
   * Finds the vertex outside the cover with the highest current degree.
   * Here we choose the vertex with higher id ( because of >= )
   * 
   * @param numVertices
   * @param inCover
   * @param currentDegrees
   * @return array with [vertex, degree]
   */
  private int[] maxDegree(int numVertices, boolean[] inCover, int[] currentDegrees) {

    int maxVertex = -1;
    int maxDegree = 0;

    for (int i = 0; i < numVertices; i++) {
      if (!inCover[i] && currentDegrees[i] >= maxDegree) {
        maxDegree = currentDegrees[i];
        maxVertex = i;
      }
    }
    return new int[] { maxVertex, maxDegree };
  }

  /**
   * Decrements the degree of every adjacent vertex that is still outside the
   * cover, since the edge between them just became covered.
   * Vertices that already are in the cover are skipped.
   * 
   * @param edges
   * @param maxVertex
   * @param inCover
   * @param currentDegrees
   */
  private void updateNeighborsDegree(List<Edge> edges, int maxVertex, boolean[] inCover, int[] currentDegrees) {
    for (Edge e : edges) {
      if (e.vertex1 == maxVertex && !inCover[e.vertex2]) {
        currentDegrees[e.vertex2]--;
      } else if (e.vertex2 == maxVertex && !inCover[e.vertex1]) {
        currentDegrees[e.vertex1]--;
      }
    }
  }
}
