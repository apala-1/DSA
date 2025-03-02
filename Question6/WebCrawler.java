package Question6;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class WebCrawler {
    private static final int MAX_THREADS = 5; // Increase if needed
    private static final int TIMEOUT = 5000;
    private static final Pattern URL_PATTERN = Pattern.compile("href=\"(http[^\"]+)\"");
    private static final Pattern TITLE_PATTERN = Pattern.compile("<title>(.*?)</title>", Pattern.DOTALL);
    private static final Pattern META_DESCRIPTION_PATTERN = Pattern.compile("<meta name=\"description\" content=\"(.*?)\".*?>", Pattern.DOTALL);
    private static final Pattern IMAGE_PATTERN = Pattern.compile("<img [^>]*src=\"([^\"]+)\"");

    private static final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();
    private static final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
    private static final List<Future<?>> futures = new ArrayList<>();

    public static void main(String[] args) {
        String startUrl = "https://www.cbeebies.com/"; // Change to any site you'd like
        submitTask(startUrl);

        // Wait for all tasks to complete before shutting down
        waitForCompletion();

        executor.shutdown();
        System.out.println("Crawling finished.");
    }

    private static void submitTask(String url) {
        if (!visitedUrls.add(url)) return; // Skip duplicates

        Future<?> future = executor.submit(() -> {
            System.out.println("Crawling: " + url);
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                connection.setConnectTimeout(TIMEOUT);
                connection.setReadTimeout(TIMEOUT);

                if (connection.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line);
                    }
                    reader.close();

                    // Extract and save title, meta description, image URLs
                    String title = extractTitle(content.toString());
                    String metaDescription = extractMetaDescription(content.toString());
                    List<String> imageUrls = extractImageUrls(content.toString());

                    // Save to CSV file
                    saveToCsv(title, metaDescription, imageUrls);

                    // Extract new URLs to crawl
                    extractUrls(content.toString());
                }
            } catch (Exception e) {
                System.err.println("Failed to crawl " + url + ": " + e.getMessage());
            }
        });

        futures.add(future); // Track task completion
    }

    private static void extractUrls(String content) {
        Matcher matcher = URL_PATTERN.matcher(content);
        while (matcher.find()) {
            String newUrl = matcher.group(1);
            if (newUrl.startsWith("http")) {
                submitTask(newUrl);
            }
        }
    }

    private static String extractTitle(String content) {
        Matcher titleMatcher = TITLE_PATTERN.matcher(content);
        if (titleMatcher.find()) {
            return titleMatcher.group(1);
        }
        return "No Title Found";
    }

    private static String extractMetaDescription(String content) {
        Matcher metaMatcher = META_DESCRIPTION_PATTERN.matcher(content);
        if (metaMatcher.find()) {
            return metaMatcher.group(1);
        }
        return "No Meta Description Found";
    }

    private static List<String> extractImageUrls(String content) {
        List<String> imageUrls = new ArrayList<>();
        Matcher imageMatcher = IMAGE_PATTERN.matcher(content);
        while (imageMatcher.find()) {
            imageUrls.add(imageMatcher.group(1));
        }
        return imageUrls;
    }

    private static void saveToCsv(String title, String metaDescription, List<String> imageUrls) {
        try (FileWriter writer = new FileWriter("crawled_data.csv", true)) {
            // Writing data in CSV format (Title, Meta Description, Image URLs)
            writer.append(title)
                  .append(", ")
                  .append(metaDescription)
                  .append(", ")
                  .append(String.join("; ", imageUrls))  // Join multiple image URLs with a semicolon
                  .append("\n");
        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }

    private static void waitForCompletion() {
        for (Future<?> future : futures) {
            try {
                future.get(); // Wait for each task to finish
            } catch (Exception e) {
                System.err.println("Task error: " + e.getMessage());
            }
        }
    }
}
