/*
 * You have a network of n devices. Each device can have its own communication module installed at a 
cost of modules [i - 1]. Alternatively, devices can communicate with each other using direct connections. 
The cost of connecting two devices is given by the array connections where each connections[j] = 
[device1j, device2j, costj] represents the cost to connect devices device1j and device2j. Connections are 
bidirectional, and there could be multiple valid connections between the same two devices with different 
costs. 
Goal: 
Determine the minimum total cost to connect all devices in the network. 
Input: 
n: The number of devices. 
modules: An array of costs to install communication modules on each device. 
connections: An array of connections, where each connection is represented as a triplet [device1j, 
device2j, costj]. 
Output: 
The minimum total cost to connect all devices. 
Example: 
Input: n = 3, modules = [1, 2, 2], connections = [[1, 2, 1], [2, 3, 1]] Output: 3 
Explanation: 
The best strategy is to install a communication module on the first device with cost 1 and connect the 
other devices to it with cost 2, resulting in a total cost of 3. 
 */
package Question3;
import java.util.*;

public class Question_Three_A {
    public int minCost(int n, int[] modules, int[][] connections) {
        // Priority queue for MST (cost, device)
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        
        // Initialize the priority queue with device installation costs
        for (int i = 0; i < n; i++) {
            pq.offer(new int[]{modules[i], i});
        }
        
        // To track which devices have been visited (added to the network)
        boolean[] visited = new boolean[n];
        int totalCost = 0;
        
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int cost = current[0];
            int device = current[1];
            
            // Skip this device if it is already visited
            if (visited[device]) continue;
            
            // Add the cost of this device's module if not visited
            totalCost += cost;
            visited[device] = true;
            
            // Look for connections and add them to the queue
            for (int[] connection : connections) {
                int device1 = connection[0] - 1;
                int device2 = connection[1] - 1;
                int connectionCost = connection[2];
                
                if (!visited[device1]) {
                    pq.offer(new int[]{connectionCost, device1});
                }
                if (!visited[device2]) {
                    pq.offer(new int[]{connectionCost, device2});
                }
            }
        }
        
        return totalCost;
    }
    
    public static void main(String[] args) {
        Question_Three_A sol = new Question_Three_A();
        
        int n = 3;
        int[] modules = {1, 2, 2};
        int[][] connections = {
            {1, 2, 1},
            {2, 3, 1}
        };
        
        System.out.println(sol.minCost(n, modules, connections));  // Output: 3
    }
}
