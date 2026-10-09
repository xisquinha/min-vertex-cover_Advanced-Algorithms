import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphGreedy {
  private Map<Integer, List<Integer>> adjacencyList;

  public GraphGreedy(int numVertices) {
    this.adjacencyList = new HashMap<>();

    for (int i = 0; i < numVertices; i++) {
      adjacencyList.put(i, new ArrayList<>());
    }
  }

  public void addEdge(int v1, int v2) {
    adjacencyList.get(v1).add(v2);
    adjacencyList.get(v2).add(v1);
  }

  public int degree(int v) {
    return adjacencyList.get(v).size();
  }

  public Map<Integer, List<Integer>> getAdjacencyList() {
    return adjacencyList;
  }

  @Override
  public String toString() {
    String str = "";
    for (Integer e : adjacencyList.get(0)) {
      str += e.toString() + "\n";
    }
    return str;
  }
}