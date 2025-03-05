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

 /*
            * Explanation: 
            * We use the inputs x_coords = {1, 2, 3, 2, 4} and y_coords = {2, 3, 1, 2, 3}.
            * First, we set the value of minDistance to Integer.MAX_VALUE. In here, we can store the smallest distance.
            * We can also create result array to store the indices of the closest points.

            * After looping through all pairs of points, we calculate the Manhattan Distance for all pairs of points.

            * Manhattan Distance = |xi-xj| + |yi-yj|

            * in the input above, for (1,2) and (2,3), the distance is |1-2|+|2-3| = 1+1 = 2

            * After this, the code checks if this distance is smaller than the current minDistance. If it is, then it updates the minDistance
            * and stores the indices of that pair in result

            * If the pairs have the same distance, then the code chooses the pair who has the smaller index

            * After checking all the pairs, the result array will have the indices of the closest pair of points.

            * In our case, we get [1,3] as the closest points
  */

public class Question_Two_B {
    // This function finds the closest pair of points
    public static int[] closestPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length; // this finds the number of points
        int[] result = new int[2]; // this array stores the indices of the closest pair
        int minDistance = Integer.MAX_VALUE; // We initialize the minimum distance really large

        // We can use loop in all the pairs of points to calculate Manhattan distance
        for (int i = 0; i < n; i++) { // this loop is for the first point
            for (int j = i + 1; j < n; j++) { // this loop is for the second point
                // This calculates the distance between the two points
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);

                // If the pair has smaller distance, then we update the closest pair
                if (distance < minDistance) {
                    minDistance = distance; // This helps to update the minimum distance
                    result[0] = i; // This stores the index of the first point
                    result[1] = j; // This stores the index of the second point
                } else if (distance == minDistance) {
                    // If the distance is the same as the current minimum,then we choose the lexicographically smaller pair
                    if (i < result[0] || (i == result[0] && j < result[1])) {
                        result[0] = i; // This helps to update the index of the first point
                        result[1] = j; // This updates the index of the second point
                    }
                }
            }
        }
        
        // This returns the index of the closest pair
        return result;
    }

    public static void main(String[] args) {
        // The inputs
        int[] x_coords = {1, 2, 3, 2, 4};
        int[] y_coords = {2, 3, 1, 2, 3};

        // This function finds the closest pair
        int[] result = closestPair(x_coords, y_coords);

        // This prints the closest pair
        System.out.println("The closest pair of points is: [" + result[0] + ", " + result[1] + "]");
    }
}
