import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GreedyMaxDegree {

  public ArrayList<Integer> greedyMaxDegree(GraphGreedy graph) {
    ArrayList<Integer> cover = new ArrayList<>();

    Map<Integer, List<Integer>> adjacencyList = graph.getAdjacencyList();

    while (!adjacencyList.isEmpty()) {

      int[] max = maxDegree(adjacencyList);
      int maxDegreeVer = max[0];

      if (max[1] == 0) {
        break;
      }
      cover.add(maxDegreeVer);

      removeEdges(maxDegreeVer, adjacencyList);
      adjacencyList.remove((Object) maxDegreeVer);
    }
    return cover;
  }

  public int[] maxDegree(Map<Integer, List<Integer>> adjacencyList) {

    int max = 0;
    int maxVertex = 0;

    for (Integer v : adjacencyList.keySet()) {

      int degree = adjacencyList.get(v).size();
      if (degree > max) {
        max = degree;
        maxVertex = v;
      }
    }
    return new int[] { maxVertex, max };
  }

  public void removeEdges(int vertex, Map<Integer, List<Integer>> adjacencyList) {
    List<Integer> adjacencyVertices = adjacencyList.get(vertex);

    for (int v : adjacencyVertices) {
      adjacencyList.get(v).remove(((Object) vertex));
    }
  }
}
