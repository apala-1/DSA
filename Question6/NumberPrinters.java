/* */
public class NumberPrinters {
    // Assumed NumberPrinter class (cannot be modified)
    static class NumberPrinter {
        public void printZero() {
            System.out.print("0");
        }

        public void printEven(int n) {
            System.out.print(n);
        }

        public void printOdd(int n) {
            System.out.print(n);
        }
    }

    // ThreadController class to coordinate the threads
    static class ThreadController {
        private final NumberPrinter printer; // Instance of NumberPrinter
        private final int n; // Upper limit of the sequence
        private volatile int currentNum = 1; // Current number to print (starts at 1 for odd)
        private volatile int state = 0; // State: 0 = zero, 1 = odd, 2 = even

        // Constructor
        public ThreadController(NumberPrinter printer, int n) {
            this.printer = printer;
            this.n = n;
        }

        // Method for ZeroThread
        public void printZero() throws InterruptedException {
            synchronized (this) {
                while (currentNum <= n) { // Continue until we exceed n
                    while (state != 0) { // Wait if it’s not zero’s turn
                        wait();
                    }
                    if (currentNum <= n) { // Double-check to avoid extra zero after n
                        printer.printZero(); // Print "0"
                        if (currentNum % 2 == 1) { // If currentNum is odd, next is odd
                            state = 1; // Transition to odd
                        } else { // If currentNum is even, next is even
                            state = 2; // Transition to even
                        }
                    }
                    notifyAll(); // Notify other threads
                }
            }
        }

        // Method for OddThread
        public void printOdd() throws InterruptedException {
            synchronized (this) {
                while (currentNum <= n) { // Continue until we exceed n
                    while (state != 1) { // Wait if it’s not odd’s turn
                        wait();
                    }
                    if (currentNum <= n && currentNum % 2 == 1) { // If currentNum is odd and within n
                        printer.printOdd(currentNum); // Print the odd number
                        currentNum++; // Move to next number (even)
                        state = 0; // Transition back to zero
                    }
                    notifyAll(); // Notify other threads
                }
            }
        }

        // Method for EvenThread
        public void printEven() throws InterruptedException {
            synchronized (this) {
                while (currentNum <= n) { // Continue until we exceed n
                    while (state != 2) { // Wait if it’s not even’s turn
                        wait();
                    }
                    if (currentNum <= n && currentNum % 2 == 0) { // If currentNum is even and within n
                        printer.printEven(currentNum); // Print the even number
                        currentNum++; // Move to next number (odd)
                        state = 0; // Transition back to zero
                    }
                    notifyAll(); // Notify other threads
                }
            }
        }
    }

    // Main method to test the solution
    public static void main(String[] args) {
        int n = 5; // Example: print sequence up to 5
        NumberPrinter printer = new NumberPrinter(); // Create NumberPrinter instance
        ThreadController controller = new ThreadController(printer, n); // Create ThreadController instance

        // Create threads
        Thread zeroThread = new Thread(() -> {
            try {
                controller.printZero();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "ZeroThread");

        Thread oddThread = new Thread(() -> {
            try {
                controller.printOdd();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "OddThread");

        Thread evenThread = new Thread(() -> {
            try {
                controller.printEven();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "EvenThread");

        // Start threads
        zeroThread.start();
        oddThread.start();
        evenThread.start();

        // Wait for threads to finish
        try {
            zeroThread.join();
            oddThread.join();
            evenThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(); // Newline after output for readability
    }
}