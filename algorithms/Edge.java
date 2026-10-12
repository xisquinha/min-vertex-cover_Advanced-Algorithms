package algorithms;

import java.util.Objects;

/**
 * This class represents an undirected and unweighted edge between two vertices.
 * (1, 2) == (2, 1)
 * Edge
 */
public class Edge {
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

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Edge other = (Edge) obj;

        // two vertices are equal if theu connect the same pair of vertices in either
        // direction
        return (vertex1 == other.vertex1 && vertex2 == other.vertex2)
                || (vertex1 == other.vertex2 && vertex2 == other.vertex1);
    }

    @Override
    public int hashCode() {

        int min = Math.min(vertex1, vertex2);
        int max = Math.max(vertex1, vertex2);

        return Objects.hash(min, max);
    }
}
