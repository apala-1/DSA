package Question5;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class NetworkOptimizerGUI {

    private JFrame frame;
    private JTextField nodeNameField, sourceField, destinationField, costField, bandwidthField;
    private JTextArea networkDisplay;
    private Map<String, Node> nodes = new HashMap<>();

    public NetworkOptimizerGUI() {
        frame = new JFrame("Network Optimizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(8, 2));  // Adjusted to 8 rows to add Find Path button

        nodeNameField = new JTextField();
        sourceField = new JTextField();
        destinationField = new JTextField();
        costField = new JTextField();
        bandwidthField = new JTextField();  // Added field for bandwidth
        networkDisplay = new JTextArea();
        networkDisplay.setEditable(false);

        JButton addNodeButton = new JButton("Add Node");
        JButton removeNodeButton = new JButton("Remove Node");
        JButton addEdgeButton = new JButton("Add Edge");
        JButton removeEdgeButton = new JButton("Remove Edge");
        JButton findPathButton = new JButton("Find Shortest Path");  // Added Find Path button

        inputPanel.add(new JLabel("Node Name:"));
        inputPanel.add(nodeNameField);
        inputPanel.add(addNodeButton);
        inputPanel.add(removeNodeButton);
        inputPanel.add(new JLabel("Source Node:"));
        inputPanel.add(sourceField);
        inputPanel.add(new JLabel("Destination Node:"));
        inputPanel.add(destinationField);
        inputPanel.add(new JLabel("Cost:"));
        inputPanel.add(costField);
        inputPanel.add(new JLabel("Bandwidth:"));
        inputPanel.add(bandwidthField);
        inputPanel.add(addEdgeButton);
        inputPanel.add(removeEdgeButton);
        inputPanel.add(findPathButton);  // Add the Find Path button

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(networkDisplay), BorderLayout.CENTER);

        addNodeButton.addActionListener(this::addNode);
        removeNodeButton.addActionListener(this::removeNode);
        addEdgeButton.addActionListener(this::addEdge);
        removeEdgeButton.addActionListener(this::removeEdge);
        findPathButton.addActionListener(this::findShortestPathAction);  // Action for Find Path button

        frame.setVisible(true);
    }

    private void findShortestPathAction(ActionEvent e) {
        String sourceName = sourceField.getText().trim();
        String destinationName = destinationField.getText().trim();

        if (nodes.containsKey(sourceName) && nodes.containsKey(destinationName)) {
            Node source = nodes.get(sourceName);
            Node destination = nodes.get(destinationName);

            // Call findShortestPath method
            String result = findShortestPath(source, destination);

            // Update the network display to show the result of the shortest path
            networkDisplay.setText("Shortest Path from " + sourceName + " to " + destinationName + ": " + result + "\n\n");

            // Update the network display with all nodes and edges again after showing the result
            updateNetworkDisplay();
        } else {
            networkDisplay.setText("Source or Destination node not found.");
        }
    }

    private String findShortestPath(Node source, Node destination) {
        // PriorityQueue stores nodes with calculated "distance"
        Map<Node, Integer> dist = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> dist.getOrDefault(n, Integer.MAX_VALUE)));
        Map<Node, Node> previous = new HashMap<>();
        
        // Initialize all nodes with a distance of infinity except the source node
        for (Node node : nodes.values()) {
            dist.put(node, Integer.MAX_VALUE);
        }
        dist.put(source, 0);

        // Add the source node to the priority queue
        pq.add(source);

        // Debug: Print initial state
        System.out.println("Starting Dijkstra from: " + source.getName());

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            // Debug: Print the current node being processed
            System.out.println("Processing node: " + current.getName() + " with distance: " + dist.get(current));

            // Iterate through the edges of the current node
            for (Map.Entry<Node, Edge> entry : current.getEdges().entrySet()) {
                Node neighbor = entry.getKey();
                Edge edge = entry.getValue();
                
                // Calculate combined weight (cost + latency)
                int combinedWeight = edge.getCost() + (1 / edge.getBandwidth());  // Adjust the weight factor if needed
                
                // Debug: Print the current edge being processed
                System.out.println("Checking edge from " + current.getName() + " to " + neighbor.getName() + " with combined weight: " + combinedWeight);

                // If a shorter path to the neighbor is found, update the distance and previous node
                if (dist.get(current) + combinedWeight < dist.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    dist.put(neighbor, dist.get(current) + combinedWeight);
                    previous.put(neighbor, current);
                    pq.add(neighbor);

                    // Debug: Print the updated distance for the neighbor
                    System.out.println("Updated distance for " + neighbor.getName() + ": " + dist.get(neighbor));
                }
            }
        }

        // Reconstruct the shortest path
        if (!dist.containsKey(destination) || dist.get(destination) == Integer.MAX_VALUE) {
            return "No path";
        }

        // Reconstruct the path from the destination to source using the previous map
        StringBuilder path = new StringBuilder();
        Node current = destination;
        while (current != null) {
            path.insert(0, current.getName() + " ");
            current = previous.get(current);
        }

        return path.toString().trim();
    }

    private void updateNetworkDisplay() {
        StringBuilder display = new StringBuilder("Nodes:\n");
        for (String nodeName : nodes.keySet()) {
            display.append(nodeName).append("\n");
        }

        display.append("\nEdges:\n");
        for (Node node : nodes.values()) {
            for (Map.Entry<Node, Edge> entry : node.getEdges().entrySet()) {
                display.append(node.getName()).append(" -> ").append(entry.getKey().getName())
                        .append(" (Cost: ").append(entry.getValue().getCost())
                        .append(", Bandwidth: ").append(entry.getValue().getBandwidth()).append(")\n");
            }
        }
        networkDisplay.setText(display.toString());
    }

    private void addNode(ActionEvent e) {
        String nodeName = nodeNameField.getText().trim();

        if (!nodeName.isEmpty() && !nodes.containsKey(nodeName)) {
            nodes.put(nodeName, new Node(nodeName));
            updateNetworkDisplay();
            nodeNameField.setText("");
        } else {
            JOptionPane.showMessageDialog(frame, "Node already exists or invalid input!");
        }
    }

    private void removeNode(ActionEvent e) {
        String nodeName = nodeNameField.getText().trim();

        if (!nodeName.isEmpty() && nodes.containsKey(nodeName)) {
            nodes.remove(nodeName);
            updateNetworkDisplay();
            nodeNameField.setText("");
        } else {
            JOptionPane.showMessageDialog(frame, "Node not found!");
        }
    }

    private void addEdge(ActionEvent e) {
        String sourceName = nodeNameField.getText().trim();
        String destinationName = sourceField.getText().trim();
        int cost = Integer.parseInt(costField.getText().trim());
        int bandwidth = Integer.parseInt(bandwidthField.getText().trim());

        if (nodes.containsKey(sourceName) && nodes.containsKey(destinationName)) {
            Node source = nodes.get(sourceName);
            Node destination = nodes.get(destinationName);
            source.addEdge(destination, new Edge(cost, bandwidth));
            updateNetworkDisplay();
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid nodes for edge!");
        }
    }

    private void removeEdge(ActionEvent e) {
        String sourceName = nodeNameField.getText().trim();
        String destinationName = sourceField.getText().trim();

        if (nodes.containsKey(sourceName) && nodes.containsKey(destinationName)) {
            Node source = nodes.get(sourceName);
            Node destination = nodes.get(destinationName);
            source.removeEdge(destination);
            updateNetworkDisplay();
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid nodes for edge removal!");
        }
    }

    public static void main(String[] args) {
        new NetworkOptimizerGUI();
    }

    // Node and Edge classes are defined below.

    public static class Node {
        private String name;
        private Map<Node, Edge> edges;

        public Node(String name) {
            this.name = name;
            this.edges = new HashMap<>();
        }

        public String getName() {
            return name;
        }

        public Map<Node, Edge> getEdges() {
            return edges;
        }

        public void addEdge(Node destination, Edge edge) {
            edges.put(destination, edge);
        }

        public void removeEdge(Node destination) {
            edges.remove(destination);
        }
    }

    public static class Edge {
        private int cost;
        private int bandwidth;

        public Edge(int cost, int bandwidth) {
            this.cost = cost;
            this.bandwidth = bandwidth;
        }

        public int getCost() {
            return cost;
        }

        public int getBandwidth() {
            return bandwidth;
        }
    }
}
