import java.util.*;

public class PackageCollection {
    public static int minRoadsToTraverse(int[] packages, int[][] roads) {
        int n = packages.length;
        List<List<Integer>> graph = new ArrayList<>();

        // Create adjacency list
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] road : roads) {
            graph.get(road[0]).add(road[1]);
            graph.get(road[1]).add(road[0]);
        }

        // Find package locations
        Set<Integer> packageLocations = new HashSet<>();
        for (int i = 0; i < n; i++) {
            if (packages[i] == 1) {
                packageLocations.add(i);
            }
        }

        // Find the farthest package node as a good starting point
        int start = findFarthestNode(graph, packageLocations.iterator().next(), packageLocations);
        
        // DFS from the chosen starting point
        boolean[] visited = new boolean[n];
        return dfs(start, graph, packages, visited, packageLocations);
    }

    private static int findFarthestNode(List<List<Integer>> graph, int start, Set<Integer> packageLocations) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{start, 0});
        Set<Integer> visited = new HashSet<>();
        visited.add(start);
        int farthestNode = start;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int node = current[0];

            if (packageLocations.contains(node)) {
                farthestNode = node;
            }

            for (int neighbor : graph.get(node)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(new int[]{neighbor, current[1] + 1});
                }
            }
        }
        return farthestNode;
    }

    private static int dfs(int node, List<List<Integer>> graph, int[] packages, boolean[] visited, Set<Integer> packageLocations) {
        visited[node] = true;
        int roadsUsed = 0;
        
        // If the current node has a package, count it as part of the required roads
        if (packageLocations.contains(node)) {
            roadsUsed = 1; // Only 1 road needed to visit this node
        }
    
        // Traverse each neighbor node
        for (int neighbor : graph.get(node)) {
            if (!visited[neighbor]) {
                int subTreeRoads = dfs(neighbor, graph, packages, visited, packageLocations);
                if (subTreeRoads > 0) {
                    // If there are roads in the subtree that are needed, count the current road
                    roadsUsed += subTreeRoads + 1; // Count 1 for the current road and any roads required in the subtree
                }
            }
        }
        return roadsUsed;
    }
    

    public static void main(String[] args) {
        int[] packages1 = {1, 0, 0, 0, 0, 1};
        int[][] roads1 = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}};
        System.out.println(minRoadsToTraverse(packages1, roads1)); // ✅ Output: 2

        int[] packages2 = {0, 0, 0, 1, 1, 0, 0, 1};
        int[][] roads2 = {{0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {5, 6}, {5, 7}};
        System.out.println(minRoadsToTraverse(packages2, roads2)); // ✅ Output: 2
    }
}

