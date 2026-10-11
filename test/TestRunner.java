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
        System.out.println("[FAMÍLIA 2] GRAFO EM ESTRELA / DUPLA ESTRELA");
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

        for (int V : instanceSizes) {
            GraphMatching gMatchingBi = new GraphMatching();
            GraphGreedy gGreedyBi = new GraphGreedy(V);
            generateLayeredBipartiteGraph(V, gMatchingBi, gGreedyBi);

            executeAndPrint("Grafo Bipartido em Camadas (V=" + V + ", E=" + (V - 1) + ")",
                    gMatchingBi, gGreedyBi, maximalAlgo, greedyAlgo);

        }

        System.out.println("-> Testes pendentes de implementação (TODO)\n");

        System.out.println("================================================================================");
        System.out.println("                   FIM DA SUITE DE TESTES EXPERIMENTAIS                        ");
        System.out.println("================================================================================");
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

    // TODO: falta fazer a estrela dupla

    private void generatePathGraph(int V, GraphMatching g1, GraphGreedy g2) {
        // Conectar i com i+1 de 0 até V-1
        for (int i = 0; i < V - 1; i++) {
            g1.addEdge(i, i + 1);
            g2.addEdge(i, i + 1);
        }
    }

    /**
     * The Layered Bipartite Graph is one of the few graphs that is adversial to
     * the greedy max degree algorithm.
     * The vertices in the graph are divided in two groups (U, V), where U has k
     * vertices.
     * V is split into layers 1, 2, ..., k. Layer i has floor(k/i) vertices, each
     * one with degree i. The vertices in group V are connected only to vertices in
     * group U and vice-versa.
     * 
     * @param V  - vertices in total
     * @param g1 - graph for the maximal matching algorithm
     * @param g2 - graph for the greedy degree
     */
    private void generateLayeredBipartiteGraph(int V, GraphMatching g1, GraphGreedy g2) {

        // largest k such that the construction fits in V vertices
        int k = 0;
        // find the k value given the V vertices
        while (layeredTotal(k + 1) <= V) {
            k++;
        }

        // U = {0, ..., k-1}, optimal cover = k
        // V = {k, ..., V-1}, vertices of layer i have degree i and floor(k/i) vertices
        int nextV = k; // next V vertex to be created

        // looping the layers
        for (int i = 1; i <= k; i++) {
            // number of vertices that will be in i layer
            int verticesInLayer_i = k / i;

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

    /**
     * This function calculates how many vertices there will be in total in a
     * layered bipartite graph giving the number of vertices in the U group
     * 
     * @param k - number of vertices in the U group
     * @return total number of vertices
     */
    private int layeredTotal(int k) {
        int total = k; // vertices in U group

        // the layer i of the V group has floor(k / i) vertices, so we add them in until
        // we go through all layers
        for (int i = 1; i <= k; i++)
            total += k / i;
        return total;
    }

    private void generateCompleteGraph(int n, GraphMatching g1, GraphGreedy g2) {
        // TODO: grafo com n vertices todos ligados uns aos outros
    }
}
