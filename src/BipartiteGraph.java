import java.util.*;
import java.util.random.RandomGenerator;

public class BipartiteGraph extends Graph{
    int split;
    int leftDegree;
    int rightDegree;
    /*
    Create a bipartite graph with given sizes for each half
     */
    public BipartiteGraph(int leftSize,int rightSize){
        super(leftSize+rightSize);
        split = leftSize;
        leftDegree = 0;
        rightDegree = 0;
    }
    /*
    Create a bipartite graph with given sizes for each half and a max given degree
     */
    public BipartiteGraph(int leftSize,int rightSize, int degree){
        super(leftSize+rightSize);
        split = leftSize;
        leftDegree = degree;
        rightDegree = degree;
    }
    @Override
    public BipartiteGraph clone(){
        return new BipartiteGraph(adjacencies,split,leftDegree,rightDegree);
    }

    protected BipartiteGraph(List<Set<Integer>> adjacencies,int split,int leftDegree,int rightDegree){
        super(adjacencies);
        this.split = split;
        this.leftDegree = leftDegree;
        this.rightDegree = rightDegree;
    }

    public static BipartiteGraph construct(int leftSize,int rightSize, int leftDegree,int rightDegree){
        if((leftSize * leftDegree != rightSize * rightDegree)|rightDegree<2|leftDegree<2){
            throw new IllegalArgumentException("Wrong size and/or degrees");
        }
        int size = leftSize+rightSize;
        BipartiteGraph graph = new BipartiteGraph(leftSize,rightSize,leftDegree,rightDegree);
        Dictionary<Integer,Integer> degrees = new Hashtable<>(size);
        int split = graph.getSplit();
        for (int i = 0; i < size; i++) {
            degrees.put(i,0);
        }
        for (int i = 0; i < Math.min(split,size-split)-1; i++) {
            graph.addEdge(i,split+i);// guarantee connectedness
            graph.addEdge(i+1,split+i);
            degrees.put(i,degrees.get(i)+1);
            degrees.put(i+1,degrees.get(i+1)+1);
            degrees.put(split+i,degrees.get(split+i)+2);
        }
        boolean complete = false;
        int currentRightPartner = split;
        while(!complete)
        {
            complete = true;
            for (int i = 0; i < split; i++) {

                if(degrees.get(i)==leftDegree){
                    continue;
                }
                if(degrees.get(i)<leftDegree){
                    complete = false;
                }
                if (currentRightPartner >= size) {
                    currentRightPartner = split;
                }
                if (graph.edge(i, currentRightPartner) | degrees.get(currentRightPartner) == rightDegree) {
                    currentRightPartner++;
                } else {
                    graph.addEdge(i, currentRightPartner);
                    degrees.put(i, degrees.get(i) + 1);
                    degrees.put(currentRightPartner, degrees.get(currentRightPartner) + 1);
                }

            }
        }
        return graph;
    }


    /*
    Create a bipartite biregular graph with given sizes for each half and a max given degree for each half
     */
    public BipartiteGraph(int leftSize,int rightSize, int degreeLeft,int degreeRight){
        super(leftSize+rightSize);
        split = leftSize;
        leftDegree = degreeLeft;
        rightDegree = degreeRight;
    }

    public int getSplit(){
        return split;
    }

    /*
    Add a vertex to the left half of the graph
     */
    public int addVertexLeft(){
        adjacencies.add(split,new HashSet<>());
        split++;
        return split-1;
    }
    /*
    Check if the bipartite and possibly the biregularity would stay if an edge between two given vertices is added
     */
    @Override
    public boolean canAddEdge(int u, int v){
        if(u>v){
            return canAddEdge(v,u);
        }
        boolean bipartite = (u<split && v>=split)&& !adjacencies.get(u).contains(v);
        boolean biregular =  (getDegree(u)<leftDegree|leftDegree==0)&&(getDegree(v)<rightDegree|rightDegree==0);
        return bipartite && biregular;
    }

    public boolean finished(){
        for (int i = 0; i < split; i++) {
            if(!(getDegree(i)==leftDegree|leftDegree==0)){
                return false;
            }
        }
        for (int i = split; i < adjacencies.size(); i++) {
            if (!(getDegree(i) == rightDegree | rightDegree == 0)) {
                return false;
            }
        }
        return true;
    }

    public boolean edge(int u, int v){
        return adjacencies.get(u).contains(v);
    }

    public void swapEdges(int u, int v, int w, int x){
        removeEdge(u,v);
        removeEdge(w,x);
        addEdge(u,x);
        addEdge(w,v);
    }

    public boolean canSwapEdge(int u, int v, int w ,int x){
        return edge(u,v)&&edge(w,x)&&((u<split&&w<split)|(v>=split&&x>=split));
    }

    public Set<Integer> getNeighbours(int u){
        return adjacencies.get(u);
    }
}
