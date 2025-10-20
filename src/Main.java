//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        GraphReader reader = new GraphReader();
        Graph graph = reader.initializeGraph("graph_31057.lst");
        int dia = graph.getDiameter();
        System.out.println(dia);
        reader.storeGraph(graph);
    }
}