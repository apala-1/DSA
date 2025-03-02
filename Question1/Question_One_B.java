/*
 * You have two sorted arrays of investment returns, returns1 and returns2, and a target number k. You 
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

public class Question_One_B {

    public static int kthSmallestProduct(int[] returns1, int[] returns2, int k) {
        int left = returns1[0] * returns2[0]; // Smallest possible product
        int right = returns1[returns1.length - 1] * returns2[returns2.length - 1]; // Largest possible product

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (countPairs(returns1, returns2, mid) < k) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return left;
    }

    private static int countPairs(int[] arr1, int[] arr2, int target) {
        int count = 0;
        int j = arr2.length - 1; 

        for (int num : arr1) {
            while (j >= 0 && num * arr2[j] > target) {
                j--; // Move left to find valid pairs
            }
            count += (j + 1);
        }

        return count;
    }

    public static void main(String[] args) {
        int[] returns1 = {-4, -2, 0, 3};
        int[] returns2 = {2, 4};
        int k = 6;
        System.out.println(kthSmallestProduct(returns1, returns2, k)); // Output: 0
    }
}
