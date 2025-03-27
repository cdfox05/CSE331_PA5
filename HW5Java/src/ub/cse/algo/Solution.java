package ub.cse.algo;

import java.util.*;

public class Solution {
    
    private int _startNode;
    private int _endNode;
    private HashMap<Integer, ArrayList<Integer>> graph;
    public Solution(int startNode, int endNode, HashMap<Integer, ArrayList<Integer>> g){
        _startNode = startNode;
        _endNode = endNode;
        graph = g;
    }

    class PathEntry{ //makes keeping track of the path easier
        int from;
        int to;
        int weight;

        public PathEntry(int e, int to, int w)
        {
            this.from = e;
            this.to = to;
            this.weight = w;
        }
    }

    class Comparor implements Comparator<Integer> //compares the weights of each node for Priority Queue (Dijkstra's)
    {
        @Override
        public int compare(Integer a, Integer b)
        {
            if (graph.get(a).get(0) > graph.get(b).get(0))
                return 1;

            return -1;
        }
    }
    
    public ArrayList<Integer> outputPath(){ //Dijkstra's via textbook documentations skeleton
        /*
         * Find the smallest weighted path between _startNode and _endNode
         * The first number of graph's adjacency list is the weight of it's node
         */

        PriorityQueue<Integer> todo = new PriorityQueue<>(new Comparor());

        //HashMap<Integer, Integer> nodeList = new HashMap<>();
        HashSet<Integer> edgesVisited = new HashSet<>(); //keep track of "edges" pretty much keeps track of nodes
        HashMap<Integer, PathEntry> paths = new HashMap<>(); //path entry log

        graph.get(_startNode).set(0, 0); //sets start node weight to 0

        todo.add(_startNode);

        while (!todo.isEmpty()) //full Dijkstra's implementation
        {
            int curr = todo.poll(); //grabs current node

            if (curr == _endNode) { //breaks when target node is found
                break;
            }
            if (!graph.containsKey(curr)) //skips nodes that aren't in the graph (null pointer)
            {
                continue;
            }

            int x = 0; //used to skip the weight in the current nodes edge list

            for (int node : graph.get(curr)) //cycles through the current nodes edge list
            {
                if (x == 0)
                {
                    x++;
                    continue;
                }
                if (!edgesVisited.contains(node)) {
                    int nWeight = graph.get(node).get(0);
                    int cWeight = graph.get(curr).get(0);

                    if (!paths.containsKey(node)) { //if the paths doesn't already have the current edge/neighbor add it
                        paths.put(node, new PathEntry(curr, node, cWeight + nWeight));
                    }
                    else
                    {
                        if (paths.get(node).weight > (cWeight + nWeight)) //if new weight is smaller than old weight update the path
                            paths.put(node, new PathEntry(curr, node,cWeight + nWeight));
                    }

                    graph.get(node).set(0,cWeight + nWeight); //updates weights for the comparator
                    edgesVisited.add(node);
                    todo.add(node);
                }
            }

        }

        //System.out.println(paths);
        ArrayList<Integer> finalPath = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        //System.out.println("START NODE: " + _startNode);

        int curr = _endNode;
        //System.out.println("CURR BEFORE STACK " + curr);
        while (curr != _startNode) //Loops through until path followed back from end node gives us the starting node
        {
            //System.out.println(curr);
            if (!paths.containsKey(curr))
                return new ArrayList<Integer>();
            PathEntry pathEntry = paths.get(curr);
            if (!stack.contains(pathEntry.from));
            {
                stack.push(pathEntry.from);
            }
            curr = pathEntry.from;
            //System.out.println("NEXT: "+ curr);
        }

        while (!stack.isEmpty()) //empties the stack into our path
            finalPath.add(stack.pop());

        finalPath.add(_endNode); //adds end node since it is skipped in the while loop adding to stack


        return finalPath;
    }
}

