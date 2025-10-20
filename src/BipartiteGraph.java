import java.util.HashSet;

public class BipartiteGraph extends Graph{
    int split;
    int leftDegree;
    int rightDegree;

    public BipartiteGraph(int leftSize,int rightSize){
        super(leftSize+rightSize);
        split = leftSize;
        leftDegree = 0;
        rightDegree = 0;
    }
    public BipartiteGraph(int leftSize,int rightSize, int degree){
        super(leftSize+rightSize);
        split = leftSize;
        leftDegree = degree;
        rightDegree = degree;
    }
    public BipartiteGraph(int leftSize,int rightSize, int degreeLeft,int degreeRight){
        super(leftSize+rightSize);
        split = leftSize;
        leftDegree = degreeLeft;
        rightDegree = degreeRight;
    }

    public int addVertexLeft(){
        adjacencies.add(split,new HashSet<>());
        split++;
        return split-1;
    }

    @Override
    public boolean canAddEdge(int u, int v){
        if(u>v){
            return canAddEdge(v,u);
        }
        boolean bipartite = (u<split && v>=split);
        boolean biregular =  (getDegree(u)<leftDegree|leftDegree==0)&&(getDegree(v)<rightDegree|rightDegree==0);
        return bipartite && biregular;
    }
}
