package Question5;

public class Edge {
    private Node from;
    private Node to;
    private int cost;
    private int bandwidth;

    // Constructor
    public Edge(Node from, Node to, int cost, int bandwidth) {
        this.from = from;
        this.to = to;
        this.cost = cost;
        this.bandwidth = bandwidth;
    }

    // Getters
    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public int getCost() {
        return cost;
    }

    public int getBandwidth() {
        return bandwidth;
    }
}
