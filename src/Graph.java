import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * Class made to store and allow basic operations on a graph
 */
public class Graph {


    protected List<Set<Integer>> adjacencies;

    private int maxVertices;
    private int maxDegree;




    /*
    Create a non-connected Graph with given size of vertices
     */
    public Graph(int size){
        adjacencies = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            adjacencies.add(new HashSet<>());
        }
        maxVertices = 0;
        maxDegree = 0;
    }
    public Graph(int size,int maxVertices,int maxDegree){
        adjacencies = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            adjacencies.add(new HashSet<>());
        }
        this.maxVertices = maxVertices;
        this.maxDegree = maxDegree;
    }

    public List<Set<Integer>> getAdjacencies(){
        return adjacencies;
    }

    public int size(){
        return adjacencies.size();
    }

    public boolean canAddEdge(int u, int v){
        return (maxDegree == 0 |(getDegree(u)<maxDegree&&(getDegree(v)<maxDegree))) && !adjacencies.get(u).contains(v);
    }
    /*
    add an Edge between u and v
     */
    public void addEdge(int u, int v) {
        if(canAddEdge(u,v)) {
            adjacencies.get(u).add(v);
            adjacencies.get(v).add(u);
        }
    }

    public int getDegree(int vertex){
        return adjacencies.get(vertex).size();
    }


    /*
    add an Edge between u and v
     */
    public void removeEdge(int u, int v) {
        adjacencies.get(u).remove(v);
    }

    public int addVertex(){
        adjacencies.add(new HashSet<>());
        return adjacencies.size();
    }

    public int getDiameter(){
        int dia = 0;
        for (int i = 0; i < adjacencies.size(); i++) {
            dia = Math.max(dia,BFS(i));
        }
        return dia;
    }
    /*
    execute BFS from given vertex
     */
    public int BFS(int vertex){
        Set<Integer> vert = new HashSet<>();
        vert.add(vertex);
        Set<Integer> visited = new HashSet<>();
        visited.add(vertex);
        return BFS(vert, visited,0);
    }

    public boolean connected(){
        Set<Integer> vert = new HashSet<>();
        vert.add(0);
        Set<Integer> visited = new HashSet<>();
        visited.add(0);
        return connected(vert, visited,0);

    }

    private boolean connected(Set<Integer> frontier, Set<Integer> visited, int distance){
        Set<Integer> newFrontier = new HashSet<>();
        visited.addAll(frontier);
        for (int v : frontier) {
            newFrontier.addAll(adjacencies.get(v));
        }
        newFrontier.removeAll(visited);
        if(!newFrontier.isEmpty()){

            return connected(newFrontier,visited,distance+1);
        }
        else{
            return visited.size()==adjacencies.size();
        }

    }

    private int BFS( Set<Integer> frontier, Set<Integer> visited, int distance){
        Set<Integer> newFrontier = new HashSet<>();
        visited.addAll(frontier);
        for (int v : frontier) {
            newFrontier.addAll(adjacencies.get(v));
        }
        newFrontier.removeAll(visited);
        if(!newFrontier.isEmpty()){

            return BFS(newFrontier,visited,distance+1);
        }
        else{
            return distance;
        }
    }


}
