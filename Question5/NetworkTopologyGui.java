package Question5;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class NetworkTopologyGui extends JPanel {
    private JFrame frame;
    private JButton addNodeBtn, addEdgeBtn, optimizeBtn, shortestPathBtn;
    private Map<String, Map<String, Integer>> graph;
    private Map<String, Point> nodePositions;
    private Random random = new Random();

    public NetworkTopologyGui() {
        // Initialize frame and buttons
        frame = new JFrame("Network Topology Optimizer");
        graph = new HashMap<>();
        nodePositions = new HashMap<>();

        JPanel controlPanel = new JPanel();
        addNodeBtn = new JButton("Add Node");
        addEdgeBtn = new JButton("Add Edge");
        optimizeBtn = new JButton("Optimize Network");
        shortestPathBtn = new JButton("Find Shortest Path");

        controlPanel.add(addNodeBtn);
        controlPanel.add(addEdgeBtn);
        controlPanel.add(optimizeBtn);
        controlPanel.add(shortestPathBtn);

        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(this, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Button Actions
        addNodeBtn.addActionListener(e -> addNode());
        addEdgeBtn.addActionListener(e -> addEdge());
        optimizeBtn.addActionListener(e -> optimizeNetwork());
        shortestPathBtn.addActionListener(e -> findShortestPath());
    }

    // Method to add a new node
    private void addNode() {
        String nodeName = JOptionPane.showInputDialog("Enter node name:");
        if (nodeName != null && !nodeName.trim().isEmpty() && !graph.containsKey(nodeName)) {
            graph.put(nodeName, new HashMap<>());
            nodePositions.put(nodeName, new Point(random.nextInt(500), random.nextInt(400)));
            repaint(); // Refresh graph
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid or duplicate node name!");
        }
    }

    // Method to add a new edge
    private void addEdge() {
        String node1 = JOptionPane.showInputDialog("Enter first node:");
        String node2 = JOptionPane.showInputDialog("Enter second node:");
        if (graph.containsKey(node1) && graph.containsKey(node2) && !node1.equals(node2)) {
            try {
                int cost = Integer.parseInt(JOptionPane.showInputDialog("Enter cost:"));
                graph.get(node1).put(node2, cost);
                graph.get(node2).put(node1, cost); // For undirected graph
                repaint(); // Refresh graph
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid cost input!");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid nodes!");
        }
    }

    // Optimize network using Minimum Spanning Tree (MST)
    private void optimizeNetwork() {
        if (graph.size() > 1) {
            // Using Prim's algorithm or Kruskal's could be implemented here (simplified for now)
            JOptionPane.showMessageDialog(frame, "Network optimization logic (MST) would go here!");
        } else {
            JOptionPane.showMessageDialog(frame, "Graph is not connected!");
        }
    }

    // Find the shortest path using Dijkstra's Algorithm
    private void findShortestPath() {
        String start = JOptionPane.showInputDialog("Enter start node:");
        String end = JOptionPane.showInputDialog("Enter end node:");
        if (graph.containsKey(start) && graph.containsKey(end)) {
            // Implementing Dijkstra's algorithm
            Map<String, Integer> distances = new HashMap<>();
            for (String node : graph.keySet()) {
                distances.put(node, Integer.MAX_VALUE);
            }
            distances.put(start, 0);
            PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));
            pq.add(start);

            while (!pq.isEmpty()) {
                String currentNode = pq.poll();
                if (currentNode.equals(end)) {
                    break;
                }
                for (String neighbor : graph.get(currentNode).keySet()) {
                    int newDist = distances.get(currentNode) + graph.get(currentNode).get(neighbor);
                    if (newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        pq.add(neighbor);
                    }
                }
            }

            JOptionPane.showMessageDialog(frame, "Shortest Path Distance: " + distances.get(end));
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid nodes!");
        }
    }

    // Paint method to visualize the graph
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);

        // Draw edges
        for (String source : graph.keySet()) {
            Point p1 = nodePositions.get(source);
            for (String target : graph.get(source).keySet()) {
                Point p2 = nodePositions.get(target);
                if (p1 != null && p2 != null) {
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                    int midX = (p1.x + p2.x) / 2;
                    int midY = (p1.y + p2.y) / 2;
                    g.drawString(String.valueOf(graph.get(source).get(target)), midX, midY);
                }
            }
        }

        // Draw nodes
        for (Map.Entry<String, Point> entry : nodePositions.entrySet()) {
            g.setColor(Color.BLUE);
            Point p = entry.getValue();
            g.fillOval(p.x - 10, p.y - 10, 20, 20);
            g.setColor(Color.WHITE);
            g.drawString(entry.getKey(), p.x - 5, p.y + 5);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NetworkTopologyGui::new);
    }
}