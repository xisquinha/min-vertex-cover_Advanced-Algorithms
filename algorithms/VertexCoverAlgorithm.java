package algorithms;

import java.util.ArrayList;

public interface VertexCoverAlgorithm {
    ArrayList<Integer> findVertexCover(Graph graph);
    String getAlgorithmName();
}