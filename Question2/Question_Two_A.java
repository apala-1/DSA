/*
            Question 2 a:
            You have a team of n employees, and each employee is assigned a performance rating given in the 
            integer array ratings. You want to assign rewards to these employees based on the following rules: 
            Every employee must receive at least one reward. 
            Employees with a higher rating must receive more rewards than their adjacent colleagues. 
            Goal: 
            Determine the minimum number of rewards you need to distribute to the employees. 
            Input: 
            ratings: The array of employee performance ratings. 
            Output: 
            The minimum number of rewards needed to distribute.  
            Example 1: 
            Input: ratings = [1, 0, 2] 
            Output: 5 
            Explanation: You can allocate to the first, second and third employee with 2, 1, 2 rewards respectively. 
            Example 2: 
            Input: ratings = [1, 2, 2] 
            Output: 4 
            Explanation: You can allocate to the first, second and third employee with 1, 2, 1 rewards respectively. 
            The third employee gets 1 rewards because it satisfies the above two conditions. 
 */

 /*
            * Explanation:
            * We can use the example of the input {1, 0, 2}.

            * At first, we give each employee one reward.

            * We then check from the left to right, we also increase the reward, if the rating is higher than the previous one.

            * In this case, it will make our array = {1, 1, 2}

            * Then we check from the right to left. Again, we make sure the higher rating gets more rewards and this turns our array to {2, 1, 2}

            * Then we sum all the rewards and return it. Here, we get 5.
            
            * This code helps to find the least number of rewards that needs to be given, in an efficient manner.
  */

import java.util.Arrays;;

public class Question_Two_A {

    public static int minRewards(int[] ratings) {
        int n = ratings.length;
        int[] rewards = new int[n];
        Arrays.fill(rewards, 1); // This makes sure that each employee gets at least one reward

        // We check from left to right
        // Here, if the employee has a higher rating than previous employee, then they get more rewards
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }

        // We check from right to left
        // If the employee has higher rating than the next one, then they get more rewards
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                // We find the maximum so that we don't accidentallt overwrite the higher reward from the left to right check
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }

        // Then we can calculate the total rewards
        int totalRewards = 0;
        for (int reward : rewards) {
            totalRewards += reward;
        }

        // This helps to return the total rewards
        return totalRewards;
    }

    public static void main(String[] args) {
        int[] ratings1 = {1, 0, 2}; // This is the first array
        int[] ratings2 = {1, 2, 2}; // This is the second array

        // We can print the result
        System.out.println(minRewards(ratings1)); // Output: 5
        System.out.println(minRewards(ratings2)); // Output: 4
    }
    
}
