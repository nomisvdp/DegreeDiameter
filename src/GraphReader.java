import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


public class GraphReader {
    public GraphReader(){

    }
    public Graph initializeGraph(String string){
        try {
            return initializeGraphPrivate(string);
        } catch (IOException e) {
            throw new IllegalArgumentException("wrong name");
        }
    }
    private Graph initializeGraphPrivate(String graphName) throws IOException {
        int size = 1;
        Graph graph = new Graph(1);
        BufferedReader reader;
        if (!graphName.contains("src")) {
            reader = new BufferedReader(new FileReader("src/graphs/" + graphName));
        } else {
            reader = new BufferedReader(new FileReader(graphName));
        }

        String line = reader.readLine();
        while(line != null && !line.isEmpty()){
            String[] parts = line.split("[:\\s]+");
            int mainVertex = Integer.parseInt(parts[0])-1;
            if(size-1<mainVertex){
                while (size<=mainVertex){
                    graph.addVertex();
                    size++;
                }
            }
            for (int i = 1; i < parts.length; i++) {

                int v = Integer.parseInt(parts[i])-1;
                if (v < mainVertex) {
                    continue;
                }
                if (size-1 < v) {
                    while (size <= v) {
                        graph.addVertex();
                        size++;
                    }
                }
                graph.addEdge(mainVertex, v);
            }

            line = reader.readLine();
        }
        return graph;
    }

    public File storeGraph(Graph graph) {
        return storeGraph(graph,"tmp");
    }
    public File storeGraph(Graph graph, String string) {
        List<Set<Integer>> adjacencies = graph.getAdjacencies();
        File file = new File(string+".lst");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < graph.size(); i++) {
                StringBuilder builder = new StringBuilder();
                builder.append(i + 1).append(": ");
                ArrayList<Integer> neighbours = new ArrayList<>(adjacencies.get(i));
                Collections.sort(neighbours);
                for (int x : neighbours) {
                    builder.append(x + 1).append(" "); // space between numbers
                }

                writer.write(builder.toString().trim());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        return file;
    }
}
