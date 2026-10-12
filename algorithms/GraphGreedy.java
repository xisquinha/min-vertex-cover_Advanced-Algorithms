package algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an undirected, unweighted graph used as input for the
 * greedy max-degree vertex cover algorithm.
 * 
 * The vertices are identified by ints.
 * The graph is stored as an edge list and and array with the degree of each
 * vertex, so the algorithm can read the initial degree without checking the
 * edges.
 * 
 * GraphGreedy
 */
public class GraphGreedy {

  private int numVertices; // total numver of vertices
  private List<Edge> edges; // list of edges
  private int[] degrees; // list of vertices' degrees

  /**
   * Creates a graph with no edges
   * 
   * @param numVertices
   */
  public GraphGreedy(int numVertices) {
    this.numVertices = numVertices;
    this.edges = new ArrayList<>();
    this.degrees = new int[numVertices]; // initialize every degree to 0
  }

  /**
   * Adds an undirected edge between two vertices and updates their degrees
   * 
   * @param v1
   * @param v2
   */
  public void addEdge(int v1, int v2) {
    edges.add(new Edge(v1, v2));
    degrees[v1]++;
    degrees[v2]++;
  }

  /**
   * @return the total number of vertices
   */
  public int getNumVertices() {
    return numVertices;
  }

  /**
   * @return the edge list
   */
  public List<Edge> getEdges() {
    return edges;
  }

  /**
   * @return a copy of the degree array, so the algorithm can use and change the
   *         data of the graph without actually change the graph. This is to other
   *         tests be able to use the same graph instance
   */
  public int[] getDegreesCopy() {
    return degrees.clone();
  }
}