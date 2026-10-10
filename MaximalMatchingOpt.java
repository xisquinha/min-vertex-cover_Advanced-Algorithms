import java.util.*;

public class MaximalMatchingOpt {

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