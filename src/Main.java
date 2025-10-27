//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        BipartiteGraph graph = BipartiteGraph.construct(20,100,10,2);
        MetaHeuristics heuristics = new MetaHeuristics();
        heuristics.BipartiteAnnealingMinimizeDiameter( graph,50000,50000000,.999,10);
        GraphReader reader = new GraphReader();
        reader.storeGraph(graph);
    }
}