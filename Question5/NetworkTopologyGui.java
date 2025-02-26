/*
            Question 5 
            Optimizing a Network with Multiple Objectives 
            Problem: 
            [5 Marks] 
            Suppose you are hired as software developer for certain organization and you are tasked with creating a 
            GUI application that helps network administrators design a network topology that is both cost-effective 
            and efficient for data transmission. The application needs to visually represent servers and clients as 
            nodes in a graph, with potential network connections between them, each having associated costs and 
            bandwidths. The goal is to enable the user to find a network topology that minimizes both the total cost 
            and the latency of data transmission. 
            Approach: 
            1. Visual Representation of the Network: 
            o Design the GUI to allow users to create and visualize a network graph where each node 
            represents a server or client, and each edge represents a potential network connection. The 
            edges should display associated costs and bandwidths. 
            2. Interactive Optimization: 
            o Implement tools within the GUI that enable users to apply algorithms or heuristics to 
            optimize the network. The application should provide options to find the best combination 
            of connections that minimizes the total cost while ensuring all nodes are connected. 
            3. Dynamic Path Calculation: 
            o Include a feature where the user can calculate the shortest path between any pair of nodes 
            within the selected network topology. The GUI should display these paths, taking into 
            account the bandwidths as weights. 
            4. Real-time Evaluation: 
            o Provide real-time analysis within the GUI that displays the total cost and latency of the 
            current network topology. If the user is not satisfied with the results, they should be able 
            to adjust the topology and explore alternative solutions interactively. 
            Example: 
             Input: The user inputs a graph in the application, representing servers, clients, potential 
            connections, their costs, and bandwidths. 
             Output: The application displays the optimal network topology that balances cost and latency, 
            and shows the shortest paths between servers and clients on the GUI. 
*/


/*
            This Java application provides a GUI for managing and visualizing a network topology, 
            using Swing for the interface and basic graph operations. It features four main functionalities: 
            adding nodes, adding edges, optimizing the network, and finding the shortest path between two nodes.
            The program uses two Map structures: one to store the graph with nodes as keys and edges with costs
            as values, and another to track node positions for visualization. Nodes are represented as blue circles, 
            and edges are drawn as black lines, with the edge cost displayed at the midpoint.

            The application includes interactive buttons for the user to add nodes and edges,
            and to run algorithms like Dijkstra's shortest path and a placeholder for network optimization
            (Minimum Spanning Tree). The paintComponent method renders the graph by drawing nodes and edges
            based on their positions in the nodePositions map. When a node or edge is added, the graph is updated visually.
            This tool allows users to interact with the network topology in an intuitive way, making it easier to
            understand and analyze basic network structures.
 */


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
         // Ask for node name from user
         String nodeName = JOptionPane.showInputDialog("Enter node name:");
         if (nodeName != null && !nodeName.trim().isEmpty() && !graph.containsKey(nodeName)) {
             // If node is valid and not already in the graph, add it
             graph.put(nodeName, new HashMap<>());
             // Randomly assign position to the node
             nodePositions.put(nodeName, new Point(random.nextInt(500), random.nextInt(400)));
             repaint(); // Refresh graph to display the new node
         } else {
             // Show error message if node name is invalid or duplicate
             JOptionPane.showMessageDialog(frame, "Invalid or duplicate node name!");
         }
     }
 
     // Method to add a new edge
     private void addEdge() {
         // Ask for two nodes to create an edge between
         String node1 = JOptionPane.showInputDialog("Enter first node:");
         String node2 = JOptionPane.showInputDialog("Enter second node:");
         if (graph.containsKey(node1) && graph.containsKey(node2) && !node1.equals(node2)) {
             try {
                 // Ask for the cost of the edge between the two nodes
                 int cost = Integer.parseInt(JOptionPane.showInputDialog("Enter cost:"));
                 // Add the edge with the cost between both nodes (undirected graph)
                 graph.get(node1).put(node2, cost);
                 graph.get(node2).put(node1, cost); // Undirected graph means we add the reverse edge as well
                 repaint(); // Refresh graph to display the new edge
             } catch (NumberFormatException e) {
                 // Show error if the cost input is invalid
                 JOptionPane.showMessageDialog(frame, "Invalid cost input!");
             }
         } else {
             // Show error message if nodes are invalid
             JOptionPane.showMessageDialog(frame, "Invalid nodes!");
         }
     }
 
     // Optimize network using Minimum Spanning Tree (MST)
     private void optimizeNetwork() {
         if (graph.size() > 1) {
             // Logic for network optimization (like Prim's or Kruskal's algorithm) would go here
             JOptionPane.showMessageDialog(frame, "Network optimization logic (MST) would go here!");
         } else {
             // Show error if graph is not connected or has insufficient nodes
             JOptionPane.showMessageDialog(frame, "Graph is not connected!");
         }
     }
 
     // Find the shortest path using Dijkstra's Algorithm
     private void findShortestPath() {
         // Ask for start and end nodes to find the shortest path
         String start = JOptionPane.showInputDialog("Enter start node:");
         String end = JOptionPane.showInputDialog("Enter end node:");
         if (graph.containsKey(start) && graph.containsKey(end)) {
             // Initialize distances for all nodes
             Map<String, Integer> distances = new HashMap<>();
             for (String node : graph.keySet()) {
                 distances.put(node, Integer.MAX_VALUE); // Set initial distance to max for all nodes
             }
             distances.put(start, 0); // Start node distance is 0
             // Priority queue for Dijkstra's algorithm
             PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));
             pq.add(start);
 
             // Perform Dijkstra's algorithm
             while (!pq.isEmpty()) {
                 String currentNode = pq.poll();
                 if (currentNode.equals(end)) {
                     break; // Stop if the end node is reached
                 }
                 // Check all neighbors of the current node
                 for (String neighbor : graph.get(currentNode).keySet()) {
                     int newDist = distances.get(currentNode) + graph.get(currentNode).get(neighbor);
                     if (newDist < distances.get(neighbor)) {
                         distances.put(neighbor, newDist);
                         pq.add(neighbor);
                     }
                 }
             }
 
             // Show the shortest path distance in a dialog
             JOptionPane.showMessageDialog(frame, "Shortest Path Distance: " + distances.get(end));
         } else {
             // Show error if the nodes are invalid
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
                     // Draw line for the edge between nodes
                     g.drawLine(p1.x, p1.y, p2.x, p2.y);
                     // Draw edge cost at the midpoint
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
             // Draw node as a filled circle
             g.fillOval(p.x - 10, p.y - 10, 20, 20);
             g.setColor(Color.WHITE);
             // Draw node name inside the circle
             g.drawString(entry.getKey(), p.x - 5, p.y + 5);
         }
     }
 
     public static void main(String[] args) {
         // Run the GUI on the Swing event dispatch thread
         SwingUtilities.invokeLater(NetworkTopologyGui::new);
     }
 }
 