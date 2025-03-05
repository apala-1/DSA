/*
            You have two points in a 2D plane, represented by the arrays x_coords and y_coords. The goal is to find 
            the lexicographically pair i.e. (i, j) of points (one from each array) that are closest to each other. 
            Goal: 
            Determine the lexicographically pair of points with the smallest distance and smallest distance calculated 
            using  
            | x_coords [i] - x_coords [j]| + | y_coords [i] - y_coords [j]| 
            Note that 
            |x| denotes the absolute value of x. 
            A pair of indices (i1, j1) is lexicographically smaller than (i2, j2) if i1 < i2 or i1 == i2 and j1 < j2. 
            Input: 
            x_coords: The array of x-coordinates of the points. 
            y_coords: The array of y-coordinates of the points. 
            Output: 
            The indices of the closest pair of points. 
            Input: x_coords = [1, 2, 3, 2, 4], y_coords = [2, 3, 1, 2, 3] 
            Output: [0, 3] 
            Explanation: Consider index 0 and index 3. The value of | x_coords [i]- x_coords [j]| + | y_coords [i]- 
            y_coords [j]| is 1, which is the smallest value we can achieve. 
 */

public class Question_Two_B {
    public static int[] closestPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length;
        int[] result = new int[2];
        int minDistance = Integer.MAX_VALUE;

        // Loop through all pairs of points to calculate Manhattan distance
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Calculate the Manhattan distance
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);

                // Check if this pair has a smaller distance or if it's lexicographically smaller
                if (distance < minDistance) {
                    minDistance = distance;
                    result[0] = i;
                    result[1] = j;
                } else if (distance == minDistance) {
                    // If the distance is the same, choose the lexicographically smaller pair
                    if (i < result[0] || (i == result[0] && j < result[1])) {
                        result[0] = i;
                        result[1] = j;
                    }
                }
            }
        }
        
        return result;
    }

    public static void main(String[] args) {
        int[] x_coords = {1, 2, 3, 2, 4};
        int[] y_coords = {2, 3, 1, 2, 3};

        int[] result = closestPair(x_coords, y_coords);
        System.out.println("The closest pair of points is: [" + result[0] + ", " + result[1] + "]");
    }
}
