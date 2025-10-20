public class MetaHeuristics {

    public MetaHeuristics(){

    }

    public Graph BipartiteAnnealing(int degLeft, int degRight, int dia,int maxIterations, double temp,double dtemp, double minTemp){
        BipartiteGraph graph = new BipartiteGraph(1,1,degLeft,degRight);
        int diameter = graph.getDiameter();
        int size = graph.size();
        //calculate heuristic
        int cost = diameter*size;
        //execute move

        //check new heuristic

            //check against temperature if heuristic is worse

        //update temperature

        //repeat
        return graph;
    }

    public int calculateReducalHeuristic(Graph graph){return graph.getDiameter()*graph.size();}

}
