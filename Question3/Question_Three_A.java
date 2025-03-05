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
    // This function calculates the minimum cost that is needed to connect all the devices in the network
    public int minCost(int n, int[] modules, int[][] connections) {
        // This priority queue helps in picking the device that has the minimum cost each time
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        
        // First, we initialize the priority queue with the device installation costs
        for (int i = 0; i < n; i++) {
            pq.offer(new int[]{modules[i], i}); // This adds every device's cost and index
        }
        
        // This keeps track of which devices have been visited (added to the network)
        boolean[] visited = new boolean[n];

        // This stores the total cost of connecting all the devices
        int totalCost = 0;
        
        // While there are devices in the priority queue
        while (!pq.isEmpty()) {
            // we poll the device with the minimum installation cost
            int[] current = pq.poll();
            int cost = current[0]; // Cost needed to install the device
            int device = current[1]; // Index of the device
            
            // Skip this device if it is has been visited already
            if (visited[device]) continue;
            
            // Add the cost of this device in the total cost, if it hasn't been visited
            totalCost += cost;
            // then mark it as visited
            visited[device] = true;
            
            // Now we look for other connections and add them to the priorty queue
            for (int[] connection : connections) {
                // here, we get the devices and the connection  cost from the connection array
                int device1 = connection[0] - 1;
                int device2 = connection[1] - 1;
                // This is the connection cost
                int connectionCost = connection[2];
                
                // if device 1 is not visited, then we add it to the priority queue along with the connection cost
                if (!visited[device1]) {
                    pq.offer(new int[]{connectionCost, device1});
                }
                // if device 1 is not visited, then we add it to the priority queue along with the connection cost
                if (!visited[device2]) {
                    pq.offer(new int[]{connectionCost, device2});
                }
            }
        }
        // This returns the total cost required to connect all the devices
        return totalCost;
    }
    
    public static void main(String[] args) {
        Question_Three_A sol = new Question_Three_A();
        
        int n = 3;
        int[] modules = {1, 2, 2}; // These are the installation costs for the devices
        int[][] connections = { // These are the connection details which represent (device1, device2, connection cost)
            {1, 2, 1},
            {2, 3, 1}
        };
        
        // We can call the function and find out the minimum cost needed to connect all the devices
        System.out.println(sol.minCost(n, modules, connections));  // Output: 3
    }
}
