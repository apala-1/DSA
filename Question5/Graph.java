package Question5;

import java.util.*;

public class Graph {
    private Map<String, Node> nodes;  // All nodes in the graph

    public Graph() {
        nodes = new HashMap<>();
    }

    public void addNode(String name) {
        nodes.put(name, new Node(name));
    }

    public void addEdge(String from, String to, int cost) {
        Node fromNode = nodes.get(from);
        Node toNode = nodes.get(to);
        
        if (fromNode != null && toNode != null) {
            fromNode.addEdge(toNode, cost);
            toNode.addEdge(fromNode, cost); // Add bidirectional edge
        } else {
            System.out.println("Invalid nodes: " + from + " or " + to);
        }
    }

    public Node getNode(String name) {
        return nodes.get(name);
    }

    // Dijkstra's Algorithm for shortest path
    public List<Node> dijkstra(Node source, Node destination) {
        Map<Node, Integer> dist = new HashMap<>();
        Map<Node, Node> prev = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));

        for (Node node : nodes.values()) {
            dist.put(node, Integer.MAX_VALUE);
            prev.put(node, null);
        }

        dist.put(source, 0);
        pq.add(source);

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (current.equals(destination)) break;

            for (Map.Entry<Node, Integer> entry : current.getEdges().entrySet()) {
                Node neighbor = entry.getKey();
                int cost = entry.getValue();

                int newDist = dist.get(current) + cost;
                if (newDist < dist.get(neighbor)) {
                    dist.put(neighbor, newDist);
                    prev.put(neighbor, current);
                    pq.add(neighbor);
                }
            }
        }

        // Reconstruct the shortest path
        List<Node> path = new ArrayList<>();
        for (Node at = destination; at != null; at = prev.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path.size() == 1 ? null : path;
    }

    public void printGraph() {
        for (Node node : nodes.values()) {
            System.out.print(node.getName() + " --> ");
            for (Map.Entry<Node, Integer> entry : node.getEdges().entrySet()) {
                System.out.print(entry.getKey().getName() + " (Cost: " + entry.getValue() + "), ");
            }
            System.out.println();
        }
    }

    // ✅ **Main method for testing**
    public static void main(String[] args) {
        Graph graph = new Graph();

        // Add Nodes
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");

        // Add Edges
        graph.addEdge("A", "B", 4);
        graph.addEdge("A", "C", 2);
        graph.addEdge("B", "C", 5);
        graph.addEdge("B", "D", 10);
        graph.addEdge("C", "D", 3);

        // Print Graph
        System.out.println("Graph Structure:");
        graph.printGraph();

        // Find Shortest Path
        Node source = graph.getNode("A");
        Node destination = graph.getNode("D");
        List<Node> path = graph.dijkstra(source, destination);

        if (path != null) {
            System.out.print("Shortest Path from A to D: ");
            for (Node node : path) {
                System.out.print(node.getName() + " ");
            }
            System.out.println();
        } else {
            System.out.println("No path found!");
        }
    }
}

class Node {
    private String name;
    private Map<Node, Integer> edges;

    public Node(String name) {
        this.name = name;
        this.edges = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Map<Node, Integer> getEdges() {
        return edges;
    }

    public void addEdge(Node neighbor, int cost) {
        edges.put(neighbor, cost);
    }
}
