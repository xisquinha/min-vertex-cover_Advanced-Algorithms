import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
  private List<Edge> edges;
  private Map<Integer, List<Integer>> adjacencyList;

  public Graph(int numVertices) {
    this.edges = new ArrayList<Edge>();
    this.adjacencyList = new HashMap<>();

    for (int i = 0; i < numVertices; i++) {
      adjacencyList.put(i, new ArrayList<>());
    }
  }

  public void addEdge(int v1, int v2) {
    edges.add(new Edge(v1, v2));
    adjacencyList.get(v1).add(v2);
    adjacencyList.get(v2).add(v1);
  }

  public int degree(int v) {
    return adjacencyList.get(v).size();
  }

  public List<Edge> getEdges() {
    return edges;
  }

  public Map<Integer, List<Integer>> getAdjacencyList() {
    return adjacencyList;
  }
}

class Edge {
  public int vertex1;
  public int vertex2;

  public Edge(int edge1, int edge2) {
    this.vertex1 = edge1;
    this.vertex2 = edge2;
  }

  @Override
  public String toString() {
    return "(" + vertex1 + ", " + vertex2 + ")";
  }
}