package Question5;

import java.util.HashMap;
import java.util.Map;

class Node {
    private String name;
    private Map<Node, Edge> edges;  // Now storing Edge objects

    public Node(String name) {
        this.name = name;
        this.edges = new HashMap<>();
    }

    // Getter for 'name'
    public String getName() {
        return name;
    }

    // Getter for 'edges'
    public Map<Node, Edge> getEdges() {
        return edges;
    }

    // Add an edge with cost and bandwidth
    public void addEdge(Node destination, int cost, int bandwidth) {
        edges.put(destination, new Edge(this, destination, cost, bandwidth));
    }

    // Remove an edge
    public void removeEdge(Node destination) {
        edges.remove(destination);
    }
}
