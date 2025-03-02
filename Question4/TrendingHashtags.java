import java.util.*;
import java.util.regex.*;

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
        Map<String, Integer> hashtagCount = new HashMap<>();
        Pattern hashtagPattern = Pattern.compile("#\\w+"); // Regex to find hashtags
        
        for (String[] tweet : tweets) {
            String tweetDate = tweet[2];
            String tweetText = tweet[3];
            
            // Consider only tweets from February 2024
            if (tweetDate.startsWith("2024-02")) {
                Matcher matcher = hashtagPattern.matcher(tweetText);
                while (matcher.find()) {
                    String hashtag = matcher.group();
                    hashtagCount.put(hashtag, hashtagCount.getOrDefault(hashtag, 0) + 1);
                }
            }
        }

        // Step 2: Sort hashtags by count (descending), then by name (descending)
        List<Map.Entry<String, Integer>> sortedHashtags = new ArrayList<>(hashtagCount.entrySet());
        sortedHashtags.sort((a, b) -> b.getValue().equals(a.getValue()) ? b.getKey().compareTo(a.getKey()) : b.getValue() - a.getValue());

        // Step 3: Output the top 3 hashtags
        System.out.println("hashtag-");
        for (int i = 0; i < Math.min(3, sortedHashtags.size()); i++) {
            System.out.println(sortedHashtags.get(i).getKey());
        }

        System.out.println("\ncount-");
        for (int i = 0; i < Math.min(3, sortedHashtags.size()); i++) {
            System.out.println(sortedHashtags.get(i).getValue());
        }
    }
}

