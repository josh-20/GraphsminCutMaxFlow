import java.io.File;
import java.util.*;
import java.util.LinkedList;
import java.util.spi.AbstractResourceBundleProvider;
// path method
// max flow
//

public class Graph {
    int vertexCt;  // Number of vertices in the graph.
    GraphNode [] G;  // Adjacency list for graph.
    String graphName;  //The file from which the graph was created.

    public Graph() {
        this.vertexCt = 0;
        this.graphName = "";
    }
    public boolean findPath(GraphNode start, GraphNode end){
        Queue<GraphNode> q = new LinkedList<>();
        q.add(start);
        while (!q.isEmpty()) {
            start = q.remove();
            for (GraphNode.EdgeInfo i : start.succ) {
                G[i.to].parent = start.nodeID;
                if (i.capacity != 0 && !G[i.to].visited) {
                    q.add(G[i.to]);
                    G[i.to].visited = true;
                }
            }
        }
        if (start == end) {
            return maxFlow(start) != 0;
        }
        return true;
    }

    public int maxFlow(GraphNode end){
        ArrayList<Integer> list = new ArrayList<>();
        int check = 10;
            while(end != G[0]){
                GraphNode temp = G[end.parent];
                for (GraphNode.EdgeInfo i : temp.succ){
                    if(G[i.to] == end){

                        if(i.capacity <= check ){
                            check = i.capacity;
                        }
                        list.add(end.nodeID);
                        end = temp;
                    }
                }
            }
        list.add(G[0].nodeID);
        updateCap(list,check);
        Collections.reverse(list);
        System.out.println("Max Flow: " + check + " " + list);
            return check;
        }

        public void updateCap(ArrayList<Integer> list, int mFlow){
           for (int i = 0; i <= list.size() ;i++){
               for (GraphNode.EdgeInfo j : G[i].succ){
                   if(list.contains(G[j.from].nodeID)) {
                       j.capacity -= mFlow;
                   }
               }
           }
           Reset(G[0], G[vertexCt -1]);
        }

        public void Reset(GraphNode start, GraphNode end){
            Queue<GraphNode> q = new LinkedList<>();
            start.visited = false;
            q.add(start);
            while(!q.isEmpty()){
                start = q.remove();
                for (GraphNode.EdgeInfo i : start.succ){
                    q.add(G[i.to]);
                    if (start == end){
                        return;
                    }
                    G[i.to].visited = false;
                }
        }
    }
    public static void main(String[] args) {
        Graph graph1 = new Graph();
        System.out.println();
        graph1.makeGraph("demands1.txt");
        System.out.println(graph1.toString());
        while (graph1.findPath(graph1.G[0], graph1.G[graph1.vertexCt - 1])) {
            System.out.println(graph1.findPath(graph1.G[0], graph1.G[graph1.vertexCt - 1]));

        }
    }

    public int getVertexCt() {
        return vertexCt;
    }

    public boolean addEdge(int source, int destination, int cap) {
        //System.out.println("addEdge " + source + "->" + destination + "(" + cap + ")");
        if (source < 0 || source >= vertexCt) return false;
        if (destination < 0 || destination >= vertexCt) return false;
        //add edge
        G[source].addEdge(source, destination, cap);
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("The Graph " + graphName + " \n");

        for (int i = 0; i < vertexCt; i++) {
            sb.append(G[i].toString());
        }
        return sb.toString();
    }

    public void makeGraph(String filename) {
        try {
            graphName = filename;
            Scanner reader = new Scanner(new File(filename));
            vertexCt = reader.nextInt();
            G = new GraphNode[vertexCt];
            for (int i = 0; i < vertexCt; i++) {
                G[i] = new GraphNode(i);
            }
            while (reader.hasNextInt()) {
                int v1 = reader.nextInt();
                int v2 = reader.nextInt();
                int cap = reader.nextInt();
                if (!addEdge(v1, v2, cap))
                    throw new Exception();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
