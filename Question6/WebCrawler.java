/*
            A Multithreaded Web Crawler 
            Problem: 
            [5 Marks] 
            You need to crawl a large number of web pages to gather data or index content. Crawling each page 
            sequentially can be time-consuming and inefficient. 
            Goal: 
            Create a web crawler application that can crawl multiple web pages concurrently using multithreading to 
            improve performance. 
            Tasks: 
            Design the application: 
            Create a data structure to store the URLs to be crawled. 
            Implement a mechanism to fetch web pages asynchronously. 
            Design a data storage mechanism to save the crawled data. 
            Create a thread pool: 
            Use the ExecutorService class to create a thread pool for managing multiple threads. 
            Submit tasks: 
            For each URL to be crawled, create a task (e.g., a Runnable or Callable object) that fetches the web page 
            and processes the content. 
            Submit these tasks to the thread pool for execution. 
            Handle responses: 
            Process the fetched web pages, extracting relevant data or indexing the content. 
            Handle errors or exceptions that may occur during the crawling process. 
            Manage the crawling queue: 
            Implement a mechanism to manage the queue of URLs to be crawled, such as a priority queue or a 
            breadth-first search algorithm. 
            By completing these tasks, you will create a multithreaded web crawler that can efficiently crawl large 
            numbers of web page 
*/

/*
            This program is a simple web crawler that extracts specific data (title, meta description, and URL image) 
            using multiple flow to crawl through the website and stores it in a CSV file. Start with the cropping of the 
            initial URL and extract additional URL -address for crawling when processing all pages. The program uses a 
            regular expression template to search for URL addresses, page titles, meters and image sources on HTML 
            content on the page. To ensure effective processing, the cripping work is performed simultaneously using a 
            fixed thread pool. 

            Each stream performs crawls for a specific URL, maintains the extracted data, then crawls 
            the new URL -ADDRESS on the page. Data extraction is performed using regular expressions. The program is looking 
            for specific HTML elements such as header tags, meta description tags and image tags. Then the extracted data is 
            stored in the CSV file, including the page title, meta description and URL image list. If the page contains a new 
            URL -ADDRESS, it is added to the URL list to ensure that the crawler will continue to explore the bound page. 
            The program also uses a set for tracking the URL -adjustment you visited to avoid the same page again. Through 
            this, all the flows are waiting for the completion of each flow before turning off the contractor's service and 
            ensuring that all the flow will complete the task.
 */

 import java.io.*; // Importing classes for input/output operations
 import java.net.*; // Importing classes for networking operations
 import java.util.*; // Importing utility classes (for data structures)
 import java.util.concurrent.*; // Importing classes for concurrent execution (ExecutorService)
 import java.util.regex.*; // Importing classes for regular expressions
 
 public class WebCrawler {
     private static final int MAX_THREADS = 5; // Maximum number of concurrent threads for crawling
     private static final int TIMEOUT = 5000; // Timeout duration for HTTP connections (5 seconds)
     private static final Pattern URL_PATTERN = Pattern.compile("href=\"(http[^\"]+)\""); // Pattern to extract URLs from anchor tags
     private static final Pattern TITLE_PATTERN = Pattern.compile("<title>(.*?)</title>", Pattern.DOTALL); // Pattern to extract page title
     private static final Pattern META_DESCRIPTION_PATTERN = Pattern.compile("<meta name=\"description\" content=\"(.*?)\".*?>", Pattern.DOTALL); // Pattern to extract meta description
     private static final Pattern IMAGE_PATTERN = Pattern.compile("<img [^>]*src=\"([^\"]+)\""); // Pattern to extract image URLs from img tags
 
     private static final Set<String> visitedUrls = ConcurrentHashMap.newKeySet(); // Set to store visited URLs and avoid revisiting
     private static final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS); // Executor service for managing threads
     private static final List<Future<?>> futures = new ArrayList<>(); // List to store Future objects representing thread tasks
 
     public static void main(String[] args) {
         String startUrl = "https://www.cbeebies.com/"; // Starting URL for crawling
         submitTask(startUrl); // Submit the task to crawl the start URL
 
         // Wait for all tasks to complete before shutting down
         waitForCompletion();
 
         executor.shutdown(); // Shutdown the executor service
         System.out.println("Crawling finished."); // Print completion message
     }
 
     private static void submitTask(String url) {
         if (!visitedUrls.add(url)) return; // If URL is already visited, return (avoid duplicates)
 
         Future<?> future = executor.submit(() -> { // Submit a new task to the executor
             System.out.println("Crawling: " + url); // Print the URL currently being crawled
             try {
                 HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection(); // Open a connection to the URL
                 connection.setRequestProperty("User-Agent", "Mozilla/5.0"); // Set User-Agent header for the request
                 connection.setConnectTimeout(TIMEOUT); // Set connection timeout
                 connection.setReadTimeout(TIMEOUT); // Set read timeout
 
                 if (connection.getResponseCode() == 200) { // If the response code is 200 (OK)
                     BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); // Read the response stream
                     StringBuilder content = new StringBuilder(); // StringBuilder to hold the content of the page
                     String line;
                     while ((line = reader.readLine()) != null) { // Read the content line by line
                         content.append(line); // Append each line to content
                     }
                     reader.close(); // Close the reader after reading the content
 
                     // Extract and save title, meta description, and image URLs from the content
                     String title = extractTitle(content.toString()); // Extract title from the content
                     String metaDescription = extractMetaDescription(content.toString()); // Extract meta description from the content
                     List<String> imageUrls = extractImageUrls(content.toString()); // Extract image URLs from the content
 
                     // Save extracted data to a CSV file
                     saveToCsv(title, metaDescription, imageUrls);
 
                     // Extract and submit new URLs to crawl
                     extractUrls(content.toString());
                 }
             } catch (Exception e) {
                 System.err.println("Failed to crawl " + url + ": " + e.getMessage()); // Handle exceptions and print error message
             }
         });
 
         futures.add(future); // Add the Future object representing the crawling task to the futures list
     }
 
     private static void extractUrls(String content) {
         Matcher matcher = URL_PATTERN.matcher(content); // Create a matcher for finding URLs in the content
         while (matcher.find()) { // Loop through all matches found by the URL pattern
             String newUrl = matcher.group(1); // Get the URL from the match
             if (newUrl.startsWith("http")) { // If the URL starts with "http" (valid URL)
                 submitTask(newUrl); // Submit a new crawling task for the found URL
             }
         }
     }
 
     private static String extractTitle(String content) {
         Matcher titleMatcher = TITLE_PATTERN.matcher(content); // Create a matcher for extracting the title
         if (titleMatcher.find()) { // If a title is found in the content
             return titleMatcher.group(1); // Return the extracted title
         }
         return "No Title Found"; // Return a default value if title is not found
     }
 
     private static String extractMetaDescription(String content) {
         Matcher metaMatcher = META_DESCRIPTION_PATTERN.matcher(content); // Create a matcher for extracting the meta description
         if (metaMatcher.find()) { // If a meta description is found in the content
             return metaMatcher.group(1); // Return the extracted meta description
         }
         return "No Meta Description Found"; // Return a default value if meta description is not found
     }
 
     private static List<String> extractImageUrls(String content) {
         List<String> imageUrls = new ArrayList<>(); // List to store extracted image URLs
         Matcher imageMatcher = IMAGE_PATTERN.matcher(content); // Create a matcher for extracting image URLs
         while (imageMatcher.find()) { // Loop through all matches found by the image pattern
             imageUrls.add(imageMatcher.group(1)); // Add the image URL to the list
         }
         return imageUrls; // Return the list of extracted image URLs
     }
 
     private static void saveToCsv(String title, String metaDescription, List<String> imageUrls) {
         try (FileWriter writer = new FileWriter("crawled_data.csv", true)) { // Open the CSV file for appending data
             // Writing extracted data to the CSV file (title, meta description, image URLs)
             writer.append(title)
                   .append(", ")
                   .append(metaDescription)
                   .append(", ")
                   .append(String.join("; ", imageUrls))  // Join multiple image URLs with a semicolon
                   .append("\n");
         } catch (IOException e) {
             System.err.println("Error writing to CSV: " + e.getMessage()); // Handle I/O exceptions when writing to CSV
         }
     }
 
     private static void waitForCompletion() {
         for (Future<?> future : futures) { // Loop through all Future objects in the futures list
             try {
                 future.get(); // Wait for each task to finish
             } catch (Exception e) {
                 System.err.println("Task error: " + e.getMessage()); // Handle errors when waiting for tasks to complete
             }
         }
     }
 }
 