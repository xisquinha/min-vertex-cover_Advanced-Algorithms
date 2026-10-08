import java.util.ArrayList;

public class Main {
  public static void main(String[] args) {

    MaximalMatching maxMat = new MaximalMatching();
    GreedyMaxDegree greedyMaxD = new GreedyMaxDegree();

    /*
     * System.out.println("Graph 1");
     * Graph graph1 = new Graph();
     * 
     * graph1.addEdge(0, 1);
     * graph1.addEdge(0, 3);
     * graph1.addEdge(0, 2);
     * graph1.addEdge(2, 3);
     * graph1.addEdge(2, 1);
     */

    Graph graph1 = new Graph(7);
    graph1.addEdge(0, 1);
    graph1.addEdge(0, 4);
    graph1.addEdge(1, 2);
    graph1.addEdge(1, 3);
    graph1.addEdge(3, 5);
    graph1.addEdge(5, 6);

    // ArrayList<Integer> M = maxMat.findMaxMatching(graph1);
    ArrayList<Integer> M = greedyMaxD.greedyMaxDegree(graph1);

    for (int i = 0; i < M.size(); i++) {
      System.out.print(M.get(i));
      if (i + 1 < M.size()) {
        System.out.print(", ");
      }
    }
    // MinimumWeightedVertexCover.findMinimumWeightedVertexCoverApprox(graph1,
    // weights1, vertexNames1);
  }
}
