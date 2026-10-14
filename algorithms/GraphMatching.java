package algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an undirected and unweighted graph.
 * This graph is stored as an edge list and used as input for the maximal
 * matching vertex cover algorithm.
 * 
 * The algorithm scans the edges in insertion order, so the order in which
 * "addEdge" is called can change the cover that ir returned.~
 * 
 * GraphMatching
 */
public class GraphMatching implements Graph {
  private final List<Edge> edges; // all edges

  /**
   * Creates a graph
   */
  public GraphMatching() {
    this.edges = new ArrayList<Edge>();
  }

  /**
   * Adds an undirected edge between two vertices
   * 
   * @param v1
   * @param v2
   */
  public void addEdge(int v1, int v2) {
    edges.add(new Edge(v1, v2));
  }

  public List<Edge> getEdges() {
    return edges;
  }

  @Override
  public String toString() {
    String str = "";
    for (Edge e : edges) {
      str += e.toString() + "\n";
    }
    return str;
  }
}
