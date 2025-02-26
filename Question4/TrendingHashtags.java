/*
            * Question 4 a
            * Write a solution to find the top 3 trending hashtags in February 2024. Every tweet may contain several hashtags. 
            * Return the result table ordered by count of hashtag, hashtag in descending order.
 */

/*
            The `TrendingHashtags` program extracts and counts the occurrences of hashtags from tweets posted in February 2024. 
            It uses a regular expression to identify hashtags, stores the counts in a `HashMap`, and then sorts the hashtags first by 
            frequency (in descending order) and second by name (also in descending order). The program filters the tweets based
            on their date, ensuring only those from February 2024 are processed. It then prints the top 3 hashtags and their respective
            counts.

            The main steps in the program include: 1) Extracting hashtags and counting their occurrences using a regex pattern,
            2) Sorting the hashtags by count and name, and 3) Outputting the top 3 hashtags along with their frequencies.
            The code effectively handles the task of identifying trending hashtags from a dataset of tweets.
 */

 import java.util.*;  // Importing the required classes for collections
 import java.util.regex.*;  // Importing the classes for regular expressions
 
 public class TrendingHashtags {
     public static void main(String[] args) {
         // Input: Tweets data
         String[][] tweets = {
             {"135", "13", "2024-02-01", "Enjoying a great start to the day. #HappyDay #MorningVibes"},
             {"136", "14", "2024-02-03", "Another #HappyDay with good vibes! #FeelGood"},
             {"137", "15", "2024-02-04", "Productivity peaks! #WorkLife #ProductiveDay"},
             {"138", "16", "2024-02-04", "Exploring new tech frontiers. #TechLife #Innovation"},
             {"139", "17", "2024-02-05", "Gratitude for today's moments. #HappyDay #Thankful"},
             {"140", "18", "2024-02-07", "Innovation drives us. #TechLife #FutureTech"},
             {"141", "19", "2024-02-09", "Connecting with nature's serenity. #Nature #Peaceful"}
         };
 
         // Step 1: Extract hashtags and count their occurrences
         Map<String, Integer> hashtagCount = new HashMap<>();  // Creating a map to store the counts of each hashtag
         Pattern hashtagPattern = Pattern.compile("#\\w+");  // Defining a regex pattern to identify hashtags
 
         for (String[] tweet : tweets) {
             String tweetDate = tweet[2];  // Extracting the tweet date
             String tweetText = tweet[3];  // Extracting the tweet text
 
             // Consider only tweets from February 2024
             if (tweetDate.startsWith("2024-02")) {
                 Matcher matcher = hashtagPattern.matcher(tweetText);  // Finding hashtags using the regex pattern
                 while (matcher.find()) {
                     String hashtag = matcher.group();  // Extracting each found hashtag
                     // Incrementing the count of the hashtag in the map
                     hashtagCount.put(hashtag, hashtagCount.getOrDefault(hashtag, 0) + 1);
                 }
             }
         }
 
         // Step 2: Sort hashtags by count (descending), then by name (descending)
         List<Map.Entry<String, Integer>> sortedHashtags = new ArrayList<>(hashtagCount.entrySet());  // Converting the map to a list
         // Sorting the list first by count (descending), then by name (descending) in case of ties
         sortedHashtags.sort((a, b) -> b.getValue().equals(a.getValue()) ? b.getKey().compareTo(a.getKey()) : b.getValue() - a.getValue());
 
         // Step 3: Output the top 3 hashtags
         System.out.println("hashtag-");  // Printing the label for hashtags
         for (int i = 0; i < Math.min(3, sortedHashtags.size()); i++) {
             System.out.println(sortedHashtags.get(i).getKey());  // Printing the hashtag
         }
 
         System.out.println("\ncount-");  // Printing the label for counts
         for (int i = 0; i < Math.min(3, sortedHashtags.size()); i++) {
             System.out.println(sortedHashtags.get(i).getValue());  // Printing the count of the hashtag
         }
     }
 }
 