import java.util.HashSet;
import java.util.Set;

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
}
