package Question6;

public class ThreadController {
    public static void main(String[] args) throws InterruptedException {
        int n = 6;  // Set the max number to be printed
        NumberPrinter printer = new NumberPrinter(n);

        // Create threads
        ZeroThread zeroThread = new ZeroThread(printer);
        EvenThread evenThread = new EvenThread(printer);
        OddThread oddThread = new OddThread(printer);

        // Start threads
        zeroThread.start();
        evenThread.start();
        oddThread.start();
        
        // Wait for threads to complete
        zeroThread.join();
        evenThread.join();
        oddThread.join();
    }
}
