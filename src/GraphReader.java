import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.BitSet;


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

    public File storeGraph(Graph graph){
        return storeGraph(graph,String.format("%d , %d , tmp",graph.getDiameter(),graph.size()));
    }

    /**
     * Stores a graph in the compact graph6 format.
     *
     * @param graph The graph to store (must be undirected and 0-indexed)
     * @param string The base name for the file
     * @return The File object for the created .g6 file
     */
    public File storeGraph(Graph graph, String string) {
        List<Set<Integer>> adjacencies = graph.getAdjacencies();
        int n = graph.size();

        // 1. Encode the number of vertices, n
        String nEncoded = encodeN(n);

        // 2. Create the bit vector (x) for the upper-right triangle
        //    of the adjacency matrix (e.g., A[0,1], A[0,2], A[1,2], ...)
        int totalBits = (n * (n - 1)) / 2;
        BitSet bitVector = new BitSet(totalBits);
        int bitIndex = 0;

        // IMPORTANT: graph6 is 0-indexed. This assumes your getAdjacencies()
        // list is also 0-indexed.
        for (int j = 1; j < n; j++) {
            for (int i = 0; i < j; i++) {
                if (adjacencies.get(i).contains(j)) {
                    bitVector.set(bitIndex);
                }
                bitIndex++;
            }
        }

        // 3. Pack the bit vector into 6-bit chunks and convert to ASCII
        StringBuilder data = new StringBuilder();
        for (int k = 0; k < totalBits; k += 6) {
            int chunk = 0;
            for (int bit = 0; bit < 6; bit++) {
                // Check if the bit is within the vector's bounds and is set
                if (k + bit < totalBits && bitVector.get(k + bit)) {
                    // Prepend the bit (6-bit chunks are big-endian)
                    chunk |= (1 << (5 - bit)); // (1 << 5) is 32, (1 << 4) is 16, etc.
                }
            }
            // Add 63 to get the printable ASCII character
            data.append((char) (chunk + 63));
        }

        // 4. Write to the .g6 file
        File file = new File(string + ".g6"); // Use .g6 extension
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(nEncoded);
            writer.write(data.toString());
            // Do NOT write a newLine()
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to write .g6 file", e);
        }

        return file;
    }

    /**
     * Encodes the vertex count 'n' into the N(n) graph6 format.
     */
    private String encodeN(int n) {
        StringBuilder sb = new StringBuilder();
        if (n >= 0 && n <= 62) {
            // 1 byte: R(n)
            sb.append((char) (n + 63));
        } else if (n >= 63 && n <= 258047) {
            // 4 bytes: 126 R(n2) R(n1) R(n0)
            sb.append((char) 126);
            sb.append((char) ((n >> 12) & 0x3F) + 63); // Top 6 bits
            sb.append((char) ((n >> 6) & 0x3F) + 63);  // Middle 6 bits
            sb.append((char) (n & 0x3F) + 63);         // Bottom 6 bits
        } else { // n >= 258048 (up to Integer.MAX_VALUE)
            // 8 bytes: 126 126 R(n5) R(n4) R(n3) R(n2) R(n1) R(n0)
            long nLong = (long) n;
            sb.append((char) 126);
            sb.append((char) 126);
            sb.append((char) ((nLong >> 30) & 0x3F) + 63);
            sb.append((char) ((nLong >> 24) & 0x3F) + 63);
            sb.append((char) ((nLong >> 18) & 0x3F) + 63);
            sb.append((char) ((nLong >> 12) & 0x3F) + 63);
            sb.append((char) ((nLong >> 6) & 0x3F) + 63);
            sb.append((char) (nLong & 0x3F) + 63);
        }
        return sb.toString();
    }
}



