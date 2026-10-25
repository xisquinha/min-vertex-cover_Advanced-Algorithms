package test;

import algorithms.*;

import java.util.*;

/**
 * TestRunner
 * 
 * The class TestRunner generates a few different types of graphs and test both
 * the graphs and algorithms that are implemented in the "algorithms" directory.
 * 
 * These tests are made with different graph sizes and are repeated several times,
 * since some algorithms work with randomness.
 */
public class TestRunner {

    private final int repetitions;

    int[] instanceSizes = { 5, 10, 100, 123, 1000, 5000 };

    int[][] instacesDoubleStar_A_B = { { 0, 0 }, { 0, 5 }, { 10, 10 }, { 1, 50 }, { 500, 500 } };

    int[] instancesLayers = { 5, 100, 1000 };

    int[][] instancesLattice = { { 3, 3 }, { 4, 3 }, { 10, 10 }, { 15, 15 }, { 100, 100 }, { 40, 30 },
            { 400, 300 } };

    public TestRunner(int repetitions) {
        this.repetitions = repetitions;
    }

    /**
     * Given a graph and an instance of a vertex cover algorithm, this function
     * executes the solution "repetitions" times and calculates the average
     * of the number of vertices in the solution and the average of the time that
     * it took to execute it.
     * This "repetitions" number is a variable that's initialized in the constructor
     * of this class.
     *
     * Since different algorithms work with different characteristics of the graph,
     * for better cost a class specified for each algorithm is better than a geral
     * graph.
     *
     * The structures that are being used for the graph implement the Graph
     * interface
     * (GraphGreedy and GraphMatching); and the classes for the algorithms implement
     * the VertexCoverAlgorithm interface (GreedyMaxDegree and MaximalMatching).
     * So if we want to use the maximal matching algorithm we will use the
     * correspondent graph, the same logic applies to the greedy maximum-degree
     * algorithm.
     *
     * @param graph     - an instance of the graph implementing Graph (GraphGreedy
     *                  or GraphMatching)
     * @param algorithm - an instance of the algorithm implementing
     *                  VertexCoverAlgorithm
     *                  (GreedyMaxDegree or MaximalMatching)
     * @return average of cover size and average of execution time
     */
    private TestResult runAlgorithm(Graph graph, VertexCoverAlgorithm algorithm) {
        long totalTime = 0;
        long totalCover = 0;
        List<Integer> coverSizes = new ArrayList<>();

        System.out.println("==  " + algorithm.getAlgorithmName() + "   ==");

        for (int i = 0; i < repetitions; i++) {

            long start = System.nanoTime(); // start the timer

            ArrayList<Integer> cover = algorithm.findVertexCover(graph); // find a solution with the algorithm

            long elapsed = System.nanoTime() - start; // calculate the execution time

            System.out.printf("Run %2d -> Cover: %-5d | Tempo: %.3f ms%n", i + 1, cover.size(), elapsed / 1000000.0);

            totalTime += elapsed;
            totalCover += cover.size();
            coverSizes.add(cover.size());
        }

        return new TestResult(
                (double) totalCover / repetitions,
                (double) totalTime / repetitions / 1000000.0,
                coverSizes);
    }

    /**
     * Executes all the tests with different types of graphs and print the results
     * in the console.
     * 
     * Types of graphs: Random Graphs; Star Graph; Path Graph; Layered Bipartite
     * Graph; Complete Graph; Double Star Graph and Lattice Graph.
     */
    public void runAllTests() {
        MaximalMatching maximalAlgo = new MaximalMatching();
        GreedyMaxDegree greedyAlgo = new GreedyMaxDegree();

        System.out.println("================================================================================");
        System.out.println("                        INÍCIO DOS TESTES EXPERIMENTAIS                          ");
        System.out.println("================================================================================\n");

        runStar(instanceSizes, maximalAlgo, greedyAlgo);
        runDoubStar(instacesDoubleStar_A_B, maximalAlgo, greedyAlgo);
        runPath(instanceSizes, maximalAlgo, greedyAlgo);
        runLattice(instancesLattice, maximalAlgo, greedyAlgo);
        runBipartite(instancesLayers, maximalAlgo, greedyAlgo);
        runComplete(instanceSizes, maximalAlgo, greedyAlgo);
        runRandom(instanceSizes, maximalAlgo, greedyAlgo);

        System.out.println("================================================================================");
        System.out.println("                            FIM DOS TESTES EXPERIMENTAIS                        ");
        System.out.println("================================================================================");
    }

    // ---------------------------------------------------------------------
    // GRAFO EM ESTRELA (Pior caso do Maximal, Melhor do Greedy)
    // ---------------------------------------------------------------------
    private void runStar(int[] instanceSizes, MaximalMatching maximalAlgo, GreedyMaxDegree greedyAlgo) {

        printTestSeparation("GRAFO EM ESTRELA");

        for (int V : instanceSizes) {
            GraphMatching gMatchingStar = new GraphMatching();
            GraphGreedy gGreedyStar = new GraphGreedy(V);
            generateStarGraph(V, gMatchingStar, gGreedyStar);

            executeAndPrint("Grafo em Estrela (V=" + V + ", E=" + (V - 1) + "), |C*|= 1",
                    gMatchingStar, gGreedyStar, maximalAlgo, greedyAlgo);
        }
    }

    // ---------------------------------------------------------------------
    // GRAFO EM LINHA / CAMINHO
    // ---------------------------------------------------------------------
    private void runPath(int[] instanceSizes, MaximalMatching maximalAlgo, GreedyMaxDegree greedyAlgo) {
        printTestSeparation("GRAFO EM LINHA (PATH GRAPH)");

        for (int V : instanceSizes) {
            GraphMatching gMatchingPath = new GraphMatching();
            GraphGreedy gGreedyPath = new GraphGreedy(V);
            generatePathGraph(V, gMatchingPath, gGreedyPath);

            executeAndPrint("Grafo em Linha/Caminho (V=" + V + ", E=" + (V - 1) + "), |C*|=" + ((int) (V / 2)),
                    gMatchingPath, gGreedyPath, maximalAlgo, greedyAlgo);
        }
    }

    // ---------------------------------------------------------------------
    // GRAFO BIPARTIDO EM CAMADAS (Pior caso do Greedy)
    // ---------------------------------------------------------------------
    private void runBipartite(int[] instancesLayers, MaximalMatching maximalAlgo, GreedyMaxDegree greedyAlgo) {
        printTestSeparation("GRAFO BIPARTIDO EM CAMADAS");

        for (int h : instancesLayers) {
            int totalV = layeredBipartiteVertexCount(h);
            int totalE = layeredBipartiteEdgeCount(h);
            generateAndRunBipartite(maximalAlgo, greedyAlgo, h, true, totalV, totalE);
            generateAndRunBipartite(maximalAlgo, greedyAlgo, h, false, totalV, totalE);
        }
    }

    private void generateAndRunBipartite(MaximalMatching maximalAlgo, GreedyMaxDegree greedyAlgo, int h, boolean uHasSmallerIds, int totalV, int totalE) {
        String testName = String.format("Grafo Bipartido em Camadas [%s] (V=%d, E=%d, nLayers=%d, |U|=%d, |W|=%d), |C*|=%d",
                (uHasSmallerIds ? "U com Ids menores" : "U com Ids maiores"), totalV, totalE, h, h, (totalV - h), h);
        GraphMatching gMatchingBi = new GraphMatching();
        GraphGreedy gGreedyBi = new GraphGreedy(totalV);
        generateLayeredBipartiteGraph(h, gMatchingBi, gGreedyBi, uHasSmallerIds, totalV);

        executeAndPrint(testName, gMatchingBi, gGreedyBi, maximalAlgo, greedyAlgo);
    }

    // ---------------------------------------------------------------------
    // GRAFO COMPLETO
    // ---------------------------------------------------------------------
    private void runComplete(int[] instanceSizes, MaximalMatching maximalAlgo, GreedyMaxDegree greedyAlgo) {
        printTestSeparation("GRAFO COMPLETO");

        for (int V : instanceSizes) {
            GraphMatching gMatchingComplete = new GraphMatching();
            GraphGreedy gGreedyComplete = new GraphGreedy(V);
            generateCompleteGraph(V, gMatchingComplete, gGreedyComplete);

            executeAndPrint("Grafo Completo (V=" + V + ", E=" + (V * (V - 1) / 2) + "), |C*|=" + (V - 1),
                    gMatchingComplete, gGreedyComplete, maximalAlgo, greedyAlgo);

        }
    }

    // ---------------------------------------------------------------------
    // GRAFO DUPLA ESTRELA
    // ---------------------------------------------------------------------
    private void runDoubStar(int[][] instacesDoubleStar_A_B, MaximalMatching maximalAlgo, GreedyMaxDegree greedyAlgo) {
        printTestSeparation("GRAFO DUPLA ESTRELA");

        for (int[] a_b : instacesDoubleStar_A_B) {
            int a = a_b[0];
            int b = a_b[1];
            int V = a + b + 2;

            int optimal_C = 2;
            if (a == 0 || b == 0) {
                optimal_C = 1;
            }

            GraphMatching gMatchingDubStar = new GraphMatching();
            GraphGreedy gGreedyDubStar = new GraphGreedy(V);
            generateDoubleStarGraph(a, b, gMatchingDubStar, gGreedyDubStar);

            executeAndPrint("Grafo Dupla Estrela (V=" + V + ", E=" + (V - 1) + ", A=" + a + ", B=" + b + "), |C*|= " + optimal_C,
                    gMatchingDubStar, gGreedyDubStar, maximalAlgo, greedyAlgo);

        }
    }

    // ---------------------------------------------------------------------
    // GRAFO LATTICE
    // ---------------------------------------------------------------------
    private void runLattice(int[][] instancesLattice, MaximalMatching maximalAlgo, GreedyMaxDegree greedyAlgo) {
        printTestSeparation("GRAFO LATTICE (GRELHA)");

        for (int[] w_h : instancesLattice) {
            int w = w_h[0];
            int h = w_h[1];
            int V = w * h;

            GraphMatching gMatchingLattice = new GraphMatching();
            GraphGreedy gGreedyLattice = new GraphGreedy(V);
            generateLatticeGraph(w, h, gMatchingLattice, gGreedyLattice);

            executeAndPrint("Grafo Lattice (V=" + V + ", W=" + w + ", H=" + h + "), |C*|=" + ((int) (V / 2)),
                    gMatchingLattice, gGreedyLattice, maximalAlgo, greedyAlgo);

        }
    }

    // ---------------------------------------------------------------------
    // GRAFOS ALEATÓRIOS (ESPARSOS E DENSOS)
    // ---------------------------------------------------------------------
    private void runRandom(int[] instanceSizes, MaximalMatching maximalAlgo, GreedyMaxDegree greedyAlgo) {

        printTestSeparation("GRAFOS ALEATÓRIOS");

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
    }

    /**
     * Auxilir function to calculate the total number of edges in a layered
     * bipartire graph
     * 
     * @param h - num of layers
     * @return the number of edges
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

        TestResult maximalResult = runAlgorithm(g1, maximal);
        TestResult greedyResult = runAlgorithm(g2, greedy);

        System.out.println();
        System.out.println("Resultados Consolidados:");
        printAlgorithmMetrics(maximal.getAlgorithmName(), maximalResult);
        printAlgorithmMetrics(greedy.getAlgorithmName(), greedyResult);
        System.out.println();
    }

    private void printAlgorithmMetrics(String algorithmName, TestResult result) {
        System.out.printf(
                "%-20s -> Cobertura Média: %6.2f [Mín: %4d | Máx: %4d] | Tempo Médio: %7.3f ms%n",
                algorithmName,
                result.getAverageCoverSize(),
                result.getMinCoverSize(),
                result.getMaxCoverSize(),
                result.getAverageExecutionTimeMs());
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
            addEdgeToGraphs(g1, g2, e.vertex1, e.vertex2);
        }
    }

    /**
     * Generates a Star Graph with V total vertices.
     * A star graph is a graph with one central vertex connected to n leaves (V - 1
     * in this case) and no other edges. Every leaf is only adjacent to the center.
     * So every leaf has degree 1 and the center has degree n (V-1).
     * 
     * @param V  - total number of vertices
     * @param g1 - graph for the maximal matching algorithm
     * @param g2 - graph for the greedy degree
     */
    private void generateStarGraph(int V, GraphMatching g1, GraphGreedy g2) {
        int center = 0;
        for (int leaf = 1; leaf < V; leaf++) {
            addEdgeToGraphs(g1, g2, center, leaf);
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

        addEdgeToGraphs(g1, g2, 0, 1);

        for (int i = 0; i < a; i++)
            addEdgeToGraphs(g1, g2, 0, 2 + i);

        for (int i = 0; i < b; i++)
            addEdgeToGraphs(g1, g2, 1, 2 + a + i);
    }

    private void generatePathGraph(int V, GraphMatching g1, GraphGreedy g2) {
        // Conectar i com i+1 de 0 até V-1
        for (int i = 0; i < V - 1; i++) {
            addEdgeToGraphs(g1, g2, i, i + 1);
        }
    }

    /**
     * A Layered Bipartite Graph is one of the few graphs that is adversarial to
     * the greedy max degree algorithm.
     * The vertices in the graph are divided in two groups (U, V), where U has k
     * vertices (where k = h).
     * V is split into layers 1, 2, ..., k. Layer i has floor(k/i) vertices, each
     * one with degree i. The vertices in group V are connected only to vertices in
     * group U and vice-versa.
     *
     * @param h - number of layers (and size of group U)
     * @param g1 - graph for the maximal matching algorithm
     * @param g2 - graph for the greedy degree
     * @param uHasSmallerIds if true, group U gets the smaller IDs {0, ..., k-1};
     *                       if false, V layers get the smaller IDs and U gets the larger IDs
     * @param totalV         total number of vertices in the graph
     */
    public void generateLayeredBipartiteGraph(int h, GraphMatching g1, GraphGreedy g2, boolean uHasSmallerIds, int totalV) {

        // ID assignment ranges depend on uHasSmallerIds (used to test greedy tie-breaking behavior)
        // Vertices of layer i have degree i and floor(k/i) vertices
        int nextV = uHasSmallerIds ? h : 0;

        // Defines where U vertex ids start
        int startU = uHasSmallerIds ? 0 : (totalV - h);

        // looping the layers
        for (int i = 1; i <= h; i++) {
            // number of vertices that will be in i layer
            int verticesInLayer_i = h / i;

            // loop to create vertices for the i layer
            for (int j = 0; j < verticesInLayer_i; j++) {
                int v = nextV++;

                // connects the j vertex of layer i to i vertices of U group
                for (int d = 0; d < i; d++) {
                    int u = startU + (j * i + d);
                    addEdgeToGraphs(g1, g2, u, v);
                }
            }
        }
    }

    /**
     * Auxiliary function to calculate the total number of vertices in a layered
     * bipartite graph, given the number of the layers.
     * 
     * @param h - number of layers
     * @return total number of vertices
     */
    private int layeredBipartiteVertexCount(int h) {
        int total = h;
        for (int i = 1; i <= h; i++) {
            // the number of vertices in layer i is floor(nº of layers / i)
            total += h / i;
        }
        return total;
    }

    /**
     * A Complete Graph is a graph where every vertex is connected to all the
     * other vertices, by exactly one edge.
     * 
     * @param n  - total number of vertices
     * @param g1 - graph for the maximal matching algorithm
     * @param g2 - graph for the greedy degree
     */
    private void generateCompleteGraph(int n, GraphMatching g1, GraphGreedy g2) {

        for (int i = 0; i < n; i++) {
            for (int t = i + 1; t < n; t++) {
                addEdgeToGraphs(g1, g2, i, t);
            }
        }
    }

    /**
     * A Lattice Graph is a graph whose vertices are arranged like points on a
     * regular grid, with edeges connecting neighbours points.
     * 
     * @param w  - width of the graph (number of vertices)
     * @param h  - hwight of the graph (number of vertices)
     * @param g1 - graph for the maximal matching algorithm
     * @param g2 - graph for the greedy degree
     */
    public void generateLatticeGraph(int w, int h, GraphMatching g1, GraphGreedy g2) {
        for (int i = 0; i < h - 1; i++) {
            for (int t = 0; t < w - 1; t++) {
                addEdgeToGraphs(g1, g2, w * i + t, w * i + t + 1);
                addEdgeToGraphs(g1, g2, w * i + t, w * i + t + w);
            }
            addEdgeToGraphs(g1, g2, (i + 1) * w - 1, (i + 1) * w - 1 + w);
        }
        for (int y = 0; y < w - 1; y++) {
            addEdgeToGraphs(g1, g2, w * (h - 1) + y, w * (h - 1) + y + 1);
        }
    }

    /**
     * Auxiliary function to add an edge to both instances of GraphMatching and
     * GraphGreedy
     * 
     * @param g1 - graph for the maximal matching algorithm
     * @param g2 - graph for the greedy degree
     * @param v1 - one vertex of the edge
     * @param v2 - other vertex of the edge
     */
    private void addEdgeToGraphs(GraphMatching g1, GraphGreedy g2, int v1, int v2) {
        g1.addEdge(v1, v2);
        g2.addEdge(v1, v2);
    }

    private void printTestSeparation(String testName) {
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println(testName.toUpperCase());
        System.out.println("--------------------------------------------------------------------------------");

    }
}
