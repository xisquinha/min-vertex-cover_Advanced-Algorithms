import java.util.ArrayList;
import java.util.List;

public class GraphMatching {
  private List<Edge> edges;

  public GraphMatching() {
    this.edges = new ArrayList<Edge>();
  }

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