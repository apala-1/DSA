/*
            Question 1 b:
            You have two sorted arrays of investment returns, returns1 and returns2, and a target number k. You 
            want to find the kth lowest combined return that can be achieved by selecting one investment from each 
            array. 
            Rules: 
             The arrays are sorted in ascending order. 
             You can access any element in the arrays. 
            Goal: 
            Determine the kth lowest combined return that can be achieved. 
            Input: 
             returns1: The first sorted array of investment returns. 
             returns2: The second sorted array of investment returns. 
             k: The target index of the lowest combined return. 
            Output: 
             The kth lowest combined return that can be achieved. 
            Example 1: 
            Input: returns1= [2,5], returns2= [3,4], k = 2 
            Output: 8 
            Explanation: The 2 smallest investments are  are: - returns1 [0] * returns2 [0] = 2 * 3 = 6 - returns1 [0] * returns2 [1] = 2 * 4 = 8 
            The 2nd smallest investment  is 8. 
            Example 2: 
            Input: returns1= [-4,-2,0,3], returns2= [2,4], k = 6 
            Output: 0 
            Explanation: The 6 smallest products are: - returns1 [0] * returns2 [1] = (-4) * 4 = -16 - returns1 [0] * returns2 [0] = (-4) * 2 = -8 - returns1 [1] * returns2 [1] = (-2) * 4 = -8 - returns1 [1] * returns2 [0] = (-2) * 2 = -4 - returns1 [2] * returns2 [0] = 0 * 2 = 0 - returns1 [2] * returns2 [1] = 0 * 4 = 0 
            The 6th smallest investment is 0. 
 */

 /*
            * Explanation:
            * If we take returns1 = {-4, -2, 0, 3} ,returns2 = {2, 4} and k=6 as the input, 
            * that means we need to find the 6th smallest product.

            * But if we multiply each number of both arrays, it will take a lot of time. So, we use binary search in the code below.

            * First, we find the smallest and largest product.

            * In this case, we have the smallest (-4 * 2 = -8) and largest (3 * 4 = 12).

            * We get mid as 2. Then we check how many products are less than or equal to 2.

            * If it is less than 6, then we increase the mid. Else, we decrease the mid.

            * We keep on doing this till we find the 6th smallest product which is 0 in this case.

  */

 public class Question_One_B {

    // This function finds the k-th smallest product of elements from two sorted arrays
    public static int kthSmallestProduct(int[] returns1, int[] returns2, int k) {
        // The smallest product is in left
        int left = returns1[0] * returns2[0]; 
        
        // The largest product saved in right
        int right = returns1[returns1.length - 1] * returns2[returns2.length - 1];

        // So while left is less than right, we run this loop
        while (left < right) {
            int mid = left + (right - left) / 2; // We use mid to find the middle value
            
            // We then count how many values are less than mid
            if (countPairs(returns1, returns2, mid) < k) {
                left = mid + 1; // If k products are less then mid then we go right
            } else {
                right = mid; // else, we add the value of mid to right
            }
        }

        // This returns the k-th smallest product
        return left;
    }

    // This function counts which function is less than or equal to the target
    private static int countPairs(int[] arr1, int[] arr2, int target) {
        int count = 0;
        int j = arr2.length - 1; // We start from last element of second array

        // First, we loop through every element of first array
        for (int num : arr1) {
            // Until num * arr2[j] is <= target, we move j to the left
            while (j >= 0 && num * arr2[j] > target) {
                j--; 
            }
            // All elements up to j forms valid product, so we add j+1
            count += (j + 1);
        }

        return count; // this helps to return the count value
    }

    public static void main(String[] args) {
        int[] returns1 = {-4, -2, 0, 3}; // First sorted array
        int[] returns2 = {2, 4}; // Second sorted array
        int k = 6; // We have to find the 6th smallest product

        // This helps to print the result
        System.out.println(kthSmallestProduct(returns1, returns2, k)); // Output: 0
    }
}

