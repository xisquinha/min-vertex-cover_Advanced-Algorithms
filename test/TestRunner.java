package test;

import algorithms.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TestRunner {

    private final int repetitions;

    public TestRunner(int repetitions) {
        this.repetitions = repetitions;
    }

    private TestResult runMaximalMatching(GraphMatching graph, MaximalMatching algorithm) {
        long totalTime = 0;
        long totalCover = 0;

        System.out.println("== Maximal Matching ==");

        for (int i = 0; i < repetitions; i++) {

            long start = System.nanoTime();
            ArrayList<Integer> cover = algorithm.findMaxMatching(graph);
            long elapsed = System.nanoTime() - start;

            System.out.printf("Run %2d -> Cover: %-5d | Tempo: %.3f ms%n", i + 1, cover.size(), elapsed / 1000000.0);

            totalTime += elapsed;
            totalCover += cover.size();
        }

        return new TestResult(
                (double) totalCover / repetitions,
                (double) totalTime / repetitions / 1000000.0);
    }

    private TestResult runGreedyMaxDegree(GraphGreedy graph, GreedyMaxDegree algorithm) {
        long totalTime = 0;
        long totalCover = 0;

        System.out.println("== Greedy Max-Degree ==");

        for (int i = 0; i < repetitions; i++) {

            long start = System.nanoTime();
            ArrayList<Integer> cover = algorithm.greedyMaxDegree(graph);
            long elapsed = System.nanoTime() - start;

            System.out.printf("Run %2d -> Cover: %-5d | Tempo: %.3f ms%n", i + 1, cover.size(), elapsed / 1000000.0);

            totalTime += elapsed;
            totalCover += cover.size();
        }

        return new TestResult(
                (double) totalCover / repetitions,
                (double) totalTime / repetitions / 1000000.0);
    }

    public void runAllTests() {
        MaximalMatching maximalAlgo = new MaximalMatching();
        GreedyMaxDegree greedyAlgo = new GreedyMaxDegree();

        // Variamos o tamanho dos testes: Pequeno, Médio, Grande
        int[] instanceSizes = { 100, 1000, 5000 };

        int[][] instacesDoubleStar_A_B = { { 0, 0 }, { 0, 5 }, { 10, 10 }, { 1, 50 }, { 500, 500 } };

        int[] instancesLayers = { 5, 100, 1000 };

        System.out.println("================================================================================");
        System.out.println("                        INÍCIO DOS TESTES EXPERIMENTAIS                          ");
        System.out.println("================================================================================\n");

        // ---------------------------------------------------------------------
        // FAMÍLIA 1: GRAFOS ALEATÓRIOS (ESPARSOS E DENSOS)
        // ---------------------------------------------------------------------
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("[FAMÍLIA 1] GRAFOS ALEATÓRIOS");
        System.out.println("--------------------------------------------------------------------------------");

        for (int V : instanceSizes) {
            // Cenário Esparso
            int E_esparso = V * 2;
            GraphMatching gMatchingEsp = new GraphMatching();
            GraphGreedy gGreedyEsp = new GraphGreedy(V);
            generateRandomGraph(V, E_esparso, gMatchingEsp, gGreedyEsp);

            executeAndPrint("Aleatório Esparso (V=" + V + ", E=" + E_esparso + ")",
                    gMatchingEsp, gGreedyEsp, maximalAlgo, greedyAlgo);

            // Cenário Denso (Apenas para tamanhos que não sobrecarreguem o PC)
            if (V <= 1000) {
                int E_denso = (V * V) / 10;
                GraphMatching gMatchingDens = new GraphMatching();
                GraphGreedy gGreedyDens = new GraphGreedy(V);
                generateRandomGraph(V, E_denso, gMatchingDens, gGreedyDens);

                executeAndPrint("Aleatório Denso (V=" + V + ", E=" + E_denso + ")",
                        gMatchingDens, gGreedyDens, maximalAlgo, greedyAlgo);
            }
        }

        // ---------------------------------------------------------------------
        // FAMÍLIA 2: GRAFO EM ESTRELA (Pior caso do Maximal, Melhor do Greedy)
        // ---------------------------------------------------------------------
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("[FAMÍLIA 2] GRAFO EM ESTRELA");
        System.out.println("--------------------------------------------------------------------------------");

        for (int V : instanceSizes) {
            GraphMatching gMatchingStar = new GraphMatching();
            GraphGreedy gGreedyStar = new GraphGreedy(V);
            generateStarGraph(V, gMatchingStar, gGreedyStar);

            executeAndPrint("Grafo em Estrela (V=" + V + ", E=" + (V - 1) + ")",
                    gMatchingStar, gGreedyStar, maximalAlgo, greedyAlgo);
        }

        // ---------------------------------------------------------------------
        // FAMÍLIA 3: GRAFO EM LINHA / CAMINHO
        // ---------------------------------------------------------------------
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("[FAMÍLIA 3] GRAFO EM LINHA (PATH GRAPH)");
        System.out.println("--------------------------------------------------------------------------------");

        for (int V : instanceSizes) {
            GraphMatching gMatchingPath = new GraphMatching();
            GraphGreedy gGreedyPath = new GraphGreedy(V);
            generatePathGraph(V, gMatchingPath, gGreedyPath);

            executeAndPrint("Grafo em Linha/Caminho (V=" + V + ", E=" + (V - 1) + ")",
                    gMatchingPath, gGreedyPath, maximalAlgo, greedyAlgo);
        }

        // ---------------------------------------------------------------------
        // FAMÍLIA 4: GRAFO BIPARTIDO EM CAMADAS (Pior caso do Greedy)
        // ---------------------------------------------------------------------
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("[FAMÍLIA 4] GRAFO BIPARTIDO EM CAMADAS");
        System.out.println("--------------------------------------------------------------------------------");

        for (int h : instancesLayers) {
            int totalV = layeredBipartiteVertexCount(h);
            int totalE = layeredBipartiteEdgeCount(h);
            GraphMatching gMatchingBi = new GraphMatching();
            GraphGreedy gGreedyBi = new GraphGreedy(totalV);
            generateLayeredBipartiteGraph(h, gMatchingBi, gGreedyBi);

            executeAndPrint("Grafo Bipartido em Camadas (total Vert.=" + totalV + ", E=" + totalE +
                    ", nLayers =" + h + ", U=" + h + ", V=" + (totalV - h) + ")",
                    gMatchingBi, gGreedyBi, maximalAlgo, greedyAlgo);

        }

        // ---------------------------------------------------------------------
        // FAMÍLIA 5: GRAFO COMPLETO
        // ---------------------------------------------------------------------

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("[FAMÍLIA 5] GRAFO COMPLETO");
        System.out.println("--------------------------------------------------------------------------------");

        for (int V : instanceSizes) {
            GraphMatching gMatchingComplete = new GraphMatching();
            GraphGreedy gGreedyComplete = new GraphGreedy(V);
            generateCompleteGraph(V, gMatchingComplete, gGreedyComplete);

            executeAndPrint("Grafo Completo (V=" + V + ", E=" + (V * (V - 1) / 2) + ")",
                    gMatchingComplete, gGreedyComplete, maximalAlgo, greedyAlgo);

        }

        // ---------------------------------------------------------------------
        // FAMÍLIA 6: GRAFO DUPLA ESTRELA
        // ---------------------------------------------------------------------

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("[FAMÍLIA 6] GRAFO DUPLA ESTRELA");
        System.out.println("--------------------------------------------------------------------------------");

        for (int[] a_b : instacesDoubleStar_A_B) {
            int a = a_b[0];
            int b = a_b[1];
            int V = a + b + 2;

            GraphMatching gMatchingDubStar = new GraphMatching();
            GraphGreedy gGreedyDubStar = new GraphGreedy(V);
            generateDoubleStarGraph(a, b, gMatchingDubStar, gGreedyDubStar);

            executeAndPrint("Grafo Dupla Estrela (V=" + V + ", E=" + (V - 1) + "A=" + a + ", B=" + b + ")",
                    gMatchingDubStar, gGreedyDubStar, maximalAlgo, greedyAlgo);

        }

        System.out.println("================================================================================");
        System.out.println("                   FIM DA SUITE DE TESTES EXPERIMENTAIS                        ");
        System.out.println("================================================================================");
    }

    /**
     * Auxilir function to calculate the total number of
     * 
     * @param h
     * @return
     */
    private int layeredBipartiteEdgeCount(int h) {
        int e = 0;
        for (int i = 1; i <= h; i++) {
            e += i * (h / i);
        }
        return e;
    }

    // Auxiliar: Executa os algoritmos e formata a saída de forma legível
    private void executeAndPrint(String testName, GraphMatching g1, GraphGreedy g2, MaximalMatching maximal,
            GreedyMaxDegree greedy) {

        System.out.println(">> Instância: " + testName);

        TestResult maximalResult = runMaximalMatching(g1, maximal);
        TestResult greedyResult = runGreedyMaxDegree(g2, greedy);

        System.out.printf(
                "   %-20s -> Cobertura média: %.2f | Tempo médio: %.3f ms%n", "Maximal Matching",
                maximalResult.getAverageCoverSize(), maximalResult.getAverageExecutionTimeMs());

        System.out.printf(
                "   %-20s -> Cobertura média: %.2f | Tempo médio: %.3f ms%n", "Greedy Max-Degree",
                greedyResult.getAverageCoverSize(), greedyResult.getAverageExecutionTimeMs());
        System.out.println();
    }

    // Grafos Aleatórios com seed fixa
    private void generateRandomGraph(int V, int E, GraphMatching g1, GraphGreedy g2) {
        Random rand = new Random(42);
        Set<Edge> edges = new HashSet<>();

        while (edges.size() < E) {
            int v1 = rand.nextInt(V);
            int v2 = rand.nextInt(V);

            if (v1 == v2)
                continue;

            edges.add(new Edge(v1, v2));
        }

        for (Edge e : edges) {
            g1.addEdge(e.vertex1, e.vertex2);
            g2.addEdge(e.vertex1, e.vertex2);
        }
    }

    private void generateStarGraph(int V, GraphMatching g1, GraphGreedy g2) {
        int center = 0;
        for (int leaf = 1; leaf < V; leaf++) {
            g1.addEdge(center, leaf);
            g2.addEdge(center, leaf);
        }
    }

    /**
     * A Double Star Graph is a graph that is made of two adjacent "center"
     * vertices, where each center vertex has its own set of vertices leaves.
     * The vertices in the center will have degree nº of leaves (in the respective
     * side) + 1 and each leave only has degree 1.
     * 
     * The optimal solution will be always 2.
     * 
     * @param a  - number of leaves in the U side
     * @param b  - number of leaver in the V side
     * @param g1 - graph for the maximal matching algorithm
     * @param g2 - graph for the greedy degree
     */
    private void generateDoubleStarGraph(int a, int b, GraphMatching g1, GraphGreedy g2) {

        g1.addEdge(0, 1);
        g2.addEdge(0, 1);

        for (int i = 0; i < a; i++) {
            g1.addEdge(0, 2 + i);
            g2.addEdge(0, 2 + i);
        }

        for (int i = 0; i < b; i++) {
            g1.addEdge(1, 2 + a + i);
            g2.addEdge(1, 2 + a + i);
        }
    }

    private void generatePathGraph(int V, GraphMatching g1, GraphGreedy g2) {
        // Conectar i com i+1 de 0 até V-1
        for (int i = 0; i < V - 1; i++) {
            g1.addEdge(i, i + 1);
            g2.addEdge(i, i + 1);
        }
    }

    /**
     * A Layered Bipartite Graph is one of the few graphs that is adversial to
     * the greedy max degree algorithm.
     * The vertices in the graph are divided in two groups (U, V), where U has k
     * vertices.
     * V is split into layers 1, 2, ..., k. Layer i has floor(k/i) vertices, each
     * one with degree i. The vertices in group V are connected only to vertices in
     * group U and vice-versa.
     * 
     * @param h  - number of layers
     * @param g1 - graph for the maximal matching algorithm
     * @param g2 - graph for the greedy degree
     */
    private void generateLayeredBipartiteGraph(int h, GraphMatching g1, GraphGreedy g2) {

        // U = {0, ..., k-1}, optimal cover = k
        // V = {k, ..., V-1}, vertices of layer i have degree i and floor(k/i) vertices
        int nextV = h; // next V vertex to be created

        // looping the layers
        for (int i = 1; i <= h; i++) {
            // number of vertices that will be in i layer
            int verticesInLayer_i = h / i;

            // loop to create vertices for the i layer
            for (int j = 0; j < verticesInLayer_i; j++) {
                int v = nextV++;

                // connects the j vertex of layer i to i vertices of U group
                for (int d = 0; d < i; d++) {
                    int u = j * i + d;
                    g1.addEdge(u, v);
                    g2.addEdge(u, v);
                }
            }
        }
    }

    private int layeredBipartiteVertexCount(int h) {
        int total = h;
        for (int i = 1; i <= h; i++) {
            total += h / i;
        }
        return total;
    }

    /**
     * A Complete Graph is a graph where every vertex is connected to all the
     * other vertices, by exactly one edge.
     * 
     * @param V  - total number of vertices
     * @param g1 - graph for the maximal matching algorithm
     * @param g2 - graph for the greedy degree
     */
    private void generateCompleteGraph(int n, GraphMatching g1, GraphGreedy g2) {

        for (int i = 0; i < n; i++) {
            for (int t = i + 1; t < n; t++) {
                g1.addEdge(i, t);
                g2.addEdge(i, t);
            }
        }
    }
}
