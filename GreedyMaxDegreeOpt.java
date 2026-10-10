import java.util.ArrayList;
import java.util.List;

public class GreedyMaxDegreeOpt {

    public ArrayList<Integer> greedyMaxDegree(GraphGreedyOpt graph) {
        ArrayList<Integer> cover = new ArrayList<>();

        int numVertices = graph.getNumVertices();
        List<Edge> edges = graph.getEdges();

        int[] currentDegrees = graph.getDegreesCopy();
        boolean[] inCover = new boolean[numVertices];

        while (true) {
            int[] max = maxDegree(numVertices, inCover, currentDegrees);
            int maxVertex = max[0];
            int maxDegree = max[1];

            if (maxVertex == -1 || maxDegree == 0) {
                break;
            }

            cover.add(maxVertex);
            inCover[maxVertex] = true;
            currentDegrees[maxVertex] = 0;

            updateNeighborsDegree(edges, maxVertex, inCover, currentDegrees);
        }

        return cover;
    }

    private int[] maxDegree(int numVertices, boolean[] inCover, int[] currentDegrees) {

        int maxVertex = -1;
        int maxDegree = 0;

        for (int i = 0; i < numVertices; i++) {
            if (!inCover[i] && currentDegrees[i] > maxDegree) {
                maxDegree = currentDegrees[i];
                maxVertex = i;
            }
        }
        return new int[] { maxVertex, maxDegree };
    }

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
