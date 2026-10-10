import java.util.ArrayList;
import java.util.List;

public class GraphGreedyOpt {
    private int numVertices;
    private List<Edge> edges;
    private int[] degrees;

    public GraphGreedyOpt(int numVertices) {
        this.numVertices = numVertices;
        this.edges = new ArrayList<>();
        this.degrees = new int[numVertices]; // Inicializa todos os graus a 0
    }

    public void addEdge(int v1, int v2) {
        edges.add(new Edge(v1, v2));
        degrees[v1]++;
        degrees[v2]++;
    }

    public int getNumVertices() {
        return numVertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public int[] getDegreesCopy() {
        // Retorna uma cópia para proteger os dados originais do grafo
        return degrees.clone();
    }
}