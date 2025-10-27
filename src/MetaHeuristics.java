import java.util.*;
import java.util.random.RandomGenerator;

public class MetaHeuristics {

    public MetaHeuristics(){

    }



    public BipartiteGraph BipartiteAnnealingMinimizeDiameter(BipartiteGraph graph,int maxIterations,double temp,double dtemp,double minTemp) {

        Random gen = new Random();

        BipartiteGraph currentGraph = graph.clone();
        BipartiteGraph bestGraph = graph.clone();

        int currentDiameter = currentGraph.getDiameter();
        int bestDiameter = currentDiameter;
        int split = currentGraph.getSplit();

        for (int i = 0; i < maxIterations; i++) {

            // Check termination condition
            if (temp <= minTemp) {
                break;
            }

            int u = gen.nextInt(split);
            int w = gen.nextInt(split);
            if (w == u) {
                w = (w + 1 + gen.nextInt(split - 1)) % split;
            }
            Set<Integer> neighboursOfU = new HashSet<>(currentGraph.getNeighbours(u));
            Set<Integer> neighboursOfW = new HashSet<>(currentGraph.getNeighbours(w));

            List<Integer> neighbourlistU = new ArrayList<>(neighboursOfU);
            List<Integer> neighbourlistW = new ArrayList<>(neighboursOfW);
            neighbourlistW.removeAll(neighboursOfU);
            neighbourlistU.removeAll(neighboursOfW);
            if(neighbourlistW.isEmpty()|neighbourlistU.isEmpty()){
                continue;
            }
            int v = neighbourlistU.get(gen.nextInt(neighbourlistU.size()));
            int x = neighbourlistW.get(gen.nextInt(neighbourlistW.size()));

            // Perform the swap on the 'currentGraph'
            currentGraph.swapEdges(u, v, w, x);


            // Calculate the new heuristic
            int newDia = currentGraph.getDiameter();
            // Calculate the change in "energy" (cost)
            int deltaDiameter = newDia - currentDiameter;

            // --- 5. Acceptance Criteria (Metropolis-Hastings) ---

            if (deltaDiameter <= 0) {
                //Improvement:Always accept the new state.
                currentDiameter = newDia;

                // Check if it's a new all-best
                if (newDia < bestDiameter) {
                    bestDiameter = newDia;
                    bestGraph = (BipartiteGraph) currentGraph.clone();
                }
            } else if (gen.nextDouble() < Math.exp(-deltaDiameter / temp)) {
                // worse, still accept
                currentDiameter = newDia;
            } else {
                // worse, reject
                currentGraph.swapEdges(u, v, x, w);
                // currentDiameter remains unchanged.
            }
            temp *= dtemp; // Geometric cooling schedule
            if (i % 100 == 0) {
                // Using printf for cleaner formatting of the double
                System.out.printf("Iter: %d | Temp: %.6f | Current Dia: %d | Best Dia: %d%n", i, temp, currentDiameter, bestDiameter);
            }

        }

        return bestGraph;
    }

    public int calculateReducalHeuristic(Graph graph){return graph.getDiameter()*graph.size();}

}
