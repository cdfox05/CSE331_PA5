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

    class Node {
        int node;
        int weight;
        ArrayList<Integer> edges;

        public Node(int node, int weight)
        {
            this.node = node;
            this.weight = weight;
            edges = new ArrayList<>();
        }
    }

    class Edge{
        int to;
        int from;

        public Edge(int from, int to)
        {
            this.to = to;
            this.from = from;
        }
    }

    class PathEntry{
        Edge edge;
        int weight;

        public PathEntry(Edge e, int w)
        {
            this.edge = e;
            this.weight = w;
        }
    }

    class Comparor implements Comparator<Node>
    {
        @Override
        public int compare(Node a, Node b)
        {
            if (a.weight > b.weight)
                return 1;

            return -1;
        }
    }
    
    public ArrayList<Integer> outputPath(){
        /*
         * Find the smallest weighted path between _startNode and _endNode
         * The first number of graph's adjacency list is the weight of it's node
         */

        PriorityQueue<Node> todo = new PriorityQueue<>(new Comparor());

        HashMap<Integer, Node> nodeList = new HashMap<>();
        HashMap<Integer, List<Edge>> g = new HashMap<>();
        HashSet<Edge> edgesVisited = new HashSet<>();
        HashSet<Node> nodesVisited = new HashSet<>();
        HashMap<Integer, PathEntry> paths = new HashMap<>();
        Node startNode = null;
        Node endNode = null;

        for (int key : graph.keySet())
        {
            ArrayList<Integer> adj = graph.get(key);
            Node node = new Node(key, adj.remove(0));

            if (key == _startNode)
            {
                node.weight = 0;
                startNode = node;
            }
            else if (key == _endNode)
                endNode = node;

            node.edges = adj;
            nodeList.put(key, node);

            g.put(node.node, new ArrayList<>());
            for (int to : adj)
            {
                g.get(node.node).add(new Edge(node.node, to));
            }
        }

        todo.add(startNode);

        while (!todo.isEmpty())
        {
            Node curr = todo.poll();
            nodesVisited.add(curr);

            if (curr == endNode)
                break;
            if (!g.containsKey(curr.node))
            {
                System.out.println(curr.node);
                continue;
            }

            for (Edge e : g.get(curr.node))
            {
                if (!edgesVisited.contains(e)) {
                    Node node = nodeList.get(e.to);

                    if (!paths.containsKey(node.node))
                        paths.put(node.node, new PathEntry(e, curr.weight + node.weight));
                    else
                    {
                        if (paths.get(node.node).weight > (curr.weight + node.weight))
                            paths.put(node.node, new PathEntry(e, curr.weight + node.weight));
                    }

                    edgesVisited.add(e);
                    node.weight+=curr.weight;
                    todo.add(node);
                }
            }

        }

        ArrayList<Integer> finalPath = new ArrayList<>();
        Stack<Edge> stack = new Stack<>();

        int curr = _endNode;
        while (curr != _startNode)
        {
            if (!paths.containsKey(curr))
                return new ArrayList<Integer>();
            PathEntry pathEntry = paths.get(curr);
            if (!stack.contains(pathEntry.edge));
            {
                stack.push(pathEntry.edge);
            }
            curr = pathEntry.edge.from;
        }

        while (!stack.isEmpty())
            finalPath.add(stack.pop().from);

        finalPath.add(_endNode);


        return finalPath;
    }
}

