/* Question1 
a) 
You have a material with n temperature levels. You know that there exists a critical temperature f where 
0 <= f <= n such that the material will react or change its properties at temperatures higher than f but 
remain unchanged at or below f. 
Rules: 
 You can measure the material's properties at any temperature level once. 
 If the material reacts or changes its properties, you can no longer use it for further measurements. 
 If the material remains unchanged, you can reuse it for further measurements. 
Goal: 
Determine the minimum number of measurements required to find the critical temperature. 
Input: 
 k: The number of identical samples of the material. 
 n: The number of temperature levels. 
Output: 
 The minimum number of measurements required to find the critical temperature. 
Example 1: 
Input: k = 1, n = 2 
Output: 2 
Explanation:  
Check the material at temperature 1. If its property changes, we know that f = 0. 
Otherwise, raise temperature to 2 and check if property changes. If its property changes, we know that f = 
1.If its property changes at temperature, then we know f = 2. 
Hence, we need at minimum 2 moves to determine with certainty what the value of f is. 
Example 2: 
Input: k = 2, n = 6 
Output: 3 
Example 3: 
Input: k = 3, n = 14 
Output: 4  */


public class Question_One_A {
    // creating a function for finding the minimum number of tests required
    public static int findMinTests(int k, int n) {
        // create a 2d array that stores the minimum number of tests required in each condition
        int[][] dp = new int[k + 1][n + 1];

        // When we only have 1 sample, we check each level sequentially
        /*  First we iterate from i=1 to k (number of samples), and then from j=1 to n(no of temperatures)  
         *   here k is also equal to 1.. but after this, 
         *   it doesn't go to the next loop as in the next loop i=2 and 2 is not <= k (1)
         */
        for (int i = 1; i <= k; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = j;
            }
        }

        // this part is for when we have multiple number of samples
        // first we have an outer loop that iterates from i=2 to k (number of samples)
        for (int i = 2; i <= k; i++) {
            // then we have an inner loop that iterates from j=1 to n (number of temperatures)
            for (int j = 1; j <= n; j++) {
                // 1 is the lowest, high is given the value of j and
                // the minAttempts is j because we care initially considering the worst case
                int low = 1, high = j, minAttempts = j;

                // Then we run a loop for when low is less than or equal to high
                while (low <= high) {
                    // we find out the mid value among the temperatures by adding the lowest and highest temperature and 
                    // divide it by 2
                    int mid = (low + high) / 2;

                    // if the sample breaks, we go to the lower part by 
                    int breakCase = dp[i - 1][mid - 1];  // Sample breaks
                    // if it doesn't break, we go to the upper part
                    int noBreakCase = dp[i][j - mid];    // Sample does not break

                    // we calculate the worstCase which returns the worst possible outcome and we add 1 because 
                    // we already did a test at mid
                    int worstCase = 1 + Math.max(breakCase, noBreakCase);

                    // we use the minAttempts to check the smallest worstCase
                    minAttempts = Math.min(minAttempts, worstCase);

                    // if breakCase is more then noBreakCase, we move down
                    if (breakCase > noBreakCase) {
                        high = mid - 1;
                    }
                    // else, we move up
                    else {
                        low = mid + 1;
                    }
                }

                // this stores the best solution for the i and j value
                dp[i][j] = minAttempts;
            }
        }

        // this returns that value to the user
        return dp[k][n];
    }
    public static void main(String[] args) {
        // adding variables
            int k1 = 1, n1 = 2;
            int k2 = 2, n2 = 6;
            int k3 = 3, n3 = 14;

            // Printing the final output
            System.out.println(findMinTests(k1, n1)); // Output: 2
            System.out.println(findMinTests(k2, n2)); // Output: 3
            System.out.println(findMinTests(k3, n3)); // Output: 4
    }
}