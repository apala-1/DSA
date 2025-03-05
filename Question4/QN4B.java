import java.util.*; // Import Java utility package for collections like List, Queue, Set, etc.

public class QN4B { // Define the public class QN4B
    // Method to calculate minimum roads needed to collect all packages and return to start
    public static int minRoadsToCollectPackages(int[] packages, List<List<Integer>> roads) {
        int n = packages.length; // Number of nodes (locations) in the graph
        
        // Build adjacency list for the graph (undirected)
        List<List<Integer>> adj = new ArrayList<>(); // Create an adjacency list to represent the graph
        for (int i = 0; i < n; i++) { // Loop through all nodes
            adj.add(new ArrayList<>()); // Add an empty list for each node to store its neighbors
        }
        for (List<Integer> road : roads) { // Iterate over each road (edge) in the input list
            int a = road.get(0); // Get the first node of the edge
            int b = road.get(1); // Get the second node of the edge
            adj.get(a).add(b); // Add b as a neighbor of a (undirected graph)
            adj.get(b).add(a); // Add a as a neighbor of b (undirected graph)
        }
        
        // Find all locations with packages
        List<Integer> packageLocations = new ArrayList<>(); // List to store indices of nodes with packages
        for (int i = 0; i < n; i++) { // Loop through all nodes
            if (packages[i] == 1) { // Check if the node has a package
                packageLocations.add(i); // Add the node index to the list of package locations
            }
        }
        
        if (packageLocations.isEmpty()) { // If there are no packages to collect
            return 0; // Return 0 as no roads are needed
        }
        
        // Compute distances between all pairs using BFS
        int[][] distances = new int[n][n]; // 2D array to store shortest distances between all node pairs
        for (int i = 0; i < n; i++) { // Loop through all nodes as starting points
            Arrays.fill(distances[i], Integer.MAX_VALUE); // Initialize distances from node i to all others as infinity
            distances[i] = bfsDistances(i, adj, n); // Compute actual distances from node i using BFS
        }
        
        int minRoads = Integer.MAX_VALUE; // Variable to track the minimum number of roads needed
        // Try each location as a starting point
        for (int start = 0; start < n; start++) { // Loop through each node as a potential starting point
            boolean[] visited = new boolean[n]; // Array to track visited nodes in this BFS exploration
            boolean[] collected = new boolean[n]; // Array to track which packages have been collected
            Queue<State> queue = new LinkedList<>(); // Queue for BFS exploration of states
            queue.offer(new State(start, 0, new HashSet<>())); // Add initial state: start at 'start', 0 roads, no packages collected
            
            while (!queue.isEmpty()) { // Continue while there are states to explore
                State state = queue.poll(); // Get the next state from the queue
                int curr = state.location; // Current location in this state
                int roadCount = state.roadsTraveled; // Number of roads traveled so far (renamed to avoid conflict)
                Set<Integer> collectedSet = state.collectedPackages; // Set of collected package locations
                
                if (visited[curr]) { // If this location was already visited in this exploration
                    continue; // Skip to the next state
                }
                visited[curr] = true; // Mark this location as visited
                
                // Collect packages within distance 2
                for (int loc = 0; loc < n; loc++) { // Loop through all nodes
                    if (packages[loc] == 1 && distances[curr][loc] <= 2 && !collected[loc]) { // If node has a package, is within 2 roads, and not collected
                        collected[loc] = true; // Mark the package as collected
                        collectedSet.add(loc); // Add the location to the collected set
                    }
                }
                
                // Check if all packages are collected
                boolean allCollected = true; // Flag to indicate if all packages are collected
                for (int p : packageLocations) { // Loop through all package locations
                    if (!collected[p]) { // If any package hasn’t been collected
                        allCollected = false; // Set flag to false
                        break; // Exit the loop early
                    }
                }
                
                if (allCollected) { // If all packages have been collected
                    if (distances[curr][start] != Integer.MAX_VALUE) { // If there’s a valid path back to the start
                        minRoads = Math.min(minRoads, roadCount + distances[curr][start]); // Update minRoads with total roads (traveled + return)
                    }
                    continue; // Move to the next state (no need to explore further from here)
                }
                
                // Explore adjacent locations
                for (int next : adj.get(curr)) { // Loop through neighbors of the current location
                    if (!visited[next]) { // If the neighbor hasn’t been visited
                        Set<Integer> newCollected = new HashSet<>(collectedSet); // Create a new set of collected packages for the next state
                        queue.offer(new State(next, roadCount + 1, newCollected)); // Add new state: move to next, increment road count, keep collected packages
                    }
                }
            }
        }
        
        return minRoads == Integer.MAX_VALUE ? -1 : minRoads; // Return -1 if no solution found, otherwise return minimum roads
    }
    
    // Inner class to represent a state in the BFS exploration
    static class State {
        int location; // Current location
        int roadsTraveled; // Number of roads traveled to reach this location
        Set<Integer> collectedPackages; // Set of package locations collected so far
        
        // Constructor for State
        State(int location, int roadsTraveled, Set<Integer> collectedPackages) {
            this.location = location; // Initialize location
            this.roadsTraveled = roadsTraveled; // Initialize roads traveled
            this.collectedPackages = collectedPackages; // Initialize collected packages
        }
    }
    
    // Method to compute distances from a single start node to all others using BFS
    private static int[] bfsDistances(int start, List<List<Integer>> adj, int n) {
        int[] distances = new int[n]; // Array to store distances from start to each node
        Arrays.fill(distances, Integer.MAX_VALUE); // Initialize all distances as infinity
        distances[start] = 0; // Distance to start node is 0
        Queue<Integer> queue = new LinkedList<>(); // Queue for BFS
        queue.offer(start); // Add the start node to the queue
        
        while (!queue.isEmpty()) { // Continue while there are nodes to explore
            int curr = queue.poll(); // Get the next node from the queue
            for (int next : adj.get(curr)) { // Loop through neighbors of the current node
                if (distances[next] == Integer.MAX_VALUE) { // If neighbor hasn’t been visited
                    distances[next] = distances[curr] + 1; // Set distance as one more than current
                    queue.offer(next); // Add neighbor to the queue for exploration
                }
            }
        }
        return distances; // Return the array of distances
    }
    
    // Main method to test the solution
    public static void main(String[] args) {
        // Test Case 1
        int[] packages1 = {1, 0, 0, 0, 0, 1}; // Array indicating packages at nodes 0 and 5
        List<List<Integer>> roads1 = new ArrayList<>(); // List of edges for Test Case 1
        roads1.add(Arrays.asList(0, 1)); // Edge between 0 and 1
        roads1.add(Arrays.asList(1, 2)); // Edge between 1 and 2
        roads1.add(Arrays.asList(2, 3)); // Edge between 2 and 3
        roads1.add(Arrays.asList(3, 4)); // Edge between 3 and 4
        roads1.add(Arrays.asList(4, 5)); // Edge between 4 and 5
        System.out.println("Test Case 1: " + minRoadsToCollectPackages(packages1, roads1)); // Print result for Test Case 1 (Outputs 5)
        
        // Test Case 2
        int[] packages2 = {0, 0, 0, 1, 1, 0, 0, 1}; // Array indicating packages at nodes 3, 4, and 7
        List<List<Integer>> roads2 = new ArrayList<>(); // List of edges for Test Case 2
        roads2.add(Arrays.asList(0, 1)); // Edge between 0 and 1
        roads2.add(Arrays.asList(0, 2)); // Edge between 0 and 2
        roads2.add(Arrays.asList(1, 3)); // Edge between 1 and 3
        roads2.add(Arrays.asList(1, 4)); // Edge between 1 and 4
        roads2.add(Arrays.asList(2, 5)); // Edge between 2 and 5
        roads2.add(Arrays.asList(5, 6)); // Edge between 5 and 6
        roads2.add(Arrays.asList(5, 7)); // Edge between 5 and 7
        System.out.println("Test Case 2: " + minRoadsToCollectPackages(packages2, roads2)); // Print result for Test Case 2 (Outputs 4)
    }
}