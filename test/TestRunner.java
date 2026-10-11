package test;

import algorithms.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TestRunner {

    public void runAllTests() {
        MaximalMatching maximalAlgo = new MaximalMatching();
        GreedyMaxDegree greedyAlgo = new GreedyMaxDegree();

        // Variamos o tamanho dos testes: Pequeno, Médio, Grande
        int[] instanceSizes = {100, 1000, 5000};

        System.out.println("================================================================================");
        System.out.println("                   INÍCIO DA SUITE DE TESTES EXPERIMENTAIS                      ");
        System.out.println("================================================================================\n");

        // ---------------------------------------------------------------------
        // FAMÍLIA 1: GRAFOS ALEATÓRIOS (ESPARSOS E DENSOS)
        // ---------------------------------------------------------------------
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("[FAMÍLIA 1] GRAFOS ALEATÓRIOS (Instâncias de Controlo)");
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
        // TODO: Implementar testes para a família bipartida do pior caso
        System.out.println("-> Testes pendentes de implementação (TODO)\n");

        System.out.println("================================================================================");
        System.out.println("                   FIM DA SUITE DE TESTES EXPERIMENTAIS                        ");
        System.out.println("================================================================================");
    }

    // Auxiliar: Executa os algoritmos e formata a saída de forma legível
    private void executeAndPrint(String testName, GraphMatching g1, GraphGreedy g2,
                                 MaximalMatching maximal, GreedyMaxDegree greedy) {

        // Cronometragem do Maximal Matching
        long start = System.nanoTime();
        ArrayList<Integer> coverMaximal = maximal.findMaxMatching(g1);
        long tempoMaximal = (System.nanoTime() - start) / 1000000;

        // Cronometragem do Greedy Max Degree
        start = System.nanoTime();
        ArrayList<Integer> coverGreedy = greedy.greedyMaxDegree(g2);
        long tempoGreedy = (System.nanoTime() - start) / 1000000;

        // Print Formatado e Visual para Humanos
        System.out.println(">> Instância: " + testName);
        System.out.printf("   %-20s -> Cobertura: %-5d vértices | Tempo: %d ms\n", "Maximal Matching", coverMaximal.size(), tempoMaximal);
        System.out.printf("   %-20s -> Cobertura: %-5d vértices | Tempo: %d ms\n", "Greedy Max-Degree", coverGreedy.size(), tempoGreedy);
        System.out.println("   -----------------------------------------------------------------------------");
    }

    // Grafos Aleatórios com seed fixa
    private void generateRandomGraph(int V, int E, GraphMatching g1, GraphGreedy g2) {
        Random rand = new Random(42);
        Set<Edge> edges = new HashSet<>();

        while(edges.size() < E) {
            int v1 = rand.nextInt(V);
            int v2 = rand.nextInt(V);

            if(v1 == v2)
                continue;

            edges.add(new Edge(v1, v2));
        }

        for(Edge e : edges) {
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

    private void generatePathGraph(int V, GraphMatching g1, GraphGreedy g2) {
        // Conectar i com i+1 de 0 até V-1
        for(int i = 0; i < V-1; i++) {
            g1.addEdge(i, i+1);
            g2.addEdge(i, i+1);
        }
    }

    private void generateBipartiteGraph(int h, GraphMatching g1, GraphGreedy g2) {
        // TODO: Implementar a estrutura de pior caso do Greedy ainda n sei bem como
        // mas aqui atencao que o nosso greedy para desempatar faz por ordem númerica,
        // tipo se o 1 e 2 têm o grau maximo ele usa o do 1
        // portanto se construirmos o grafo de uma forma vai dar o otimo e de outra nao vai dar
        // entao é melhor construir das duas formas
    }

    // TODO: acho que é importante parra o maximal correr varias vezes os testes porque como ele
    //  seleciona arestas random o resultado pode variar

    // TODO: Fazer para as versoes otimizadas tambem para comparar ??
}
