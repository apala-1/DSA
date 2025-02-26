/*
            Question 6 a
            You are given a class NumberPrinter with three methods: printZero, printEven, and printOdd. 
            These methods are designed to print the numbers 0, even numbers, and odd numbers, respectively. 
            Task: 
            Create a ThreadController class that coordinates three threads: 
            5. ZeroThread: Calls printZero to print 0s. 
            6. EvenThread: Calls printEven to print even numbers. 
            7. OddThread: Calls printOdd to print odd numbers. 
            These threads should work together to print the sequence "0102030405..." up to a specified number n. 
            The output should be interleaved, ensuring that the numbers are printed in the correct order. 
            Example: 
            If n = 5, the output should be "0102030405". 
            Constraints: 
             The threads should be synchronized to prevent race conditions and ensure correct output. 
             The NumberPrinter class is already provided and cannot be modified. 
*/


/*
            This program is intended to print a series of numbers after certain templates. Printed between
            each odd number and even numbers. It is assumed that the NumberPrinter class is actually processed.
            Print number. The sequence starts from the beginning, followed by odd, 0, and then Uniform numbers, etc.
            until they reach the upper limit of N. ThreadController is being managed Adjusting between the flow, 
            each stream is ensured to print in a timely manner according to the current. situation. There are three 
            types of flow: Zerothread, Oddthread and Eventhreads are responsible. Printed numeric numbers. 
           
            The adjustment between the flow is controlled using the variable state. At first, the condition is set to 0. 
            It indicates that Zerothread has time to print 0. After that, the condition is converted to 1 Oddthread for 
            printing odds is set to Eventhread to 2 to print even numbers. The Zerothreads can print the next 0 so that 
            the conditions are reset to 0 after printing. This process continues Currentnum exceeds the upper limit of n. 
            This program ensures that each stream is waiting for the turn. Others can sync sequence sequences when they 
            are completed.
*/


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
        private final NumberPrinter printer; // Instance of NumberPrinter to handle printing
        private final int n; // Upper limit of the sequence, the value until which numbers should be printed
        private volatile int currentNum = 1; // Current number to print (starts at 1 for odd numbers)
        private volatile int state = 0; // State to control which thread (zero, odd, even) can print

        // Constructor for ThreadController to initialize NumberPrinter and upper limit 'n'
        public ThreadController(NumberPrinter printer, int n) {
            this.printer = printer;
            this.n = n;
        }

        // Method for ZeroThread to print '0' between odd and even numbers
        public void printZero() throws InterruptedException {
            synchronized (this) {
                while (currentNum <= n) { // Continue until we exceed the upper limit 'n'
                    while (state != 0) { // Wait if it’s not the turn for '0' to be printed
                        wait();
                    }
                    if (currentNum <= n) { // Double-check to avoid printing an extra '0' after reaching 'n'
                        printer.printZero(); // Print "0"
                        if (currentNum % 2 == 1) { // If currentNum is odd, next number should be odd
                            state = 1; // Transition to odd number printing
                        } else { // If currentNum is even, next number should be even
                            state = 2; // Transition to even number printing
                        }
                    }
                    notifyAll(); // Notify other threads to proceed
                }
            }
        }

        // Method for OddThread to print odd numbers
        public void printOdd() throws InterruptedException {
            synchronized (this) {
                while (currentNum <= n) { // Continue until we exceed the upper limit 'n'
                    while (state != 1) { // Wait if it’s not the turn for odd numbers to be printed
                        wait();
                    }
                    if (currentNum <= n && currentNum % 2 == 1) { // If currentNum is odd and within the limit
                        printer.printOdd(currentNum); // Print the odd number
                        currentNum++; // Move to the next number (even)
                        state = 0; // Transition back to zero printing after odd number
                    }
                    notifyAll(); // Notify other threads to proceed
                }
            }
        }

        // Method for EvenThread to print even numbers
        public void printEven() throws InterruptedException {
            synchronized (this) {
                while (currentNum <= n) { // Continue until we exceed the upper limit 'n'
                    while (state != 2) { // Wait if it’s not the turn for even numbers to be printed
                        wait();
                    }
                    if (currentNum <= n && currentNum % 2 == 0) { // If currentNum is even and within the limit
                        printer.printEven(currentNum); // Print the even number
                        currentNum++; // Move to the next number (odd)
                        state = 0; // Transition back to zero printing after even number
                    }
                    notifyAll(); // Notify other threads to proceed
                }
            }
        }
    }

    // Main method to test the solution
    public static void main(String[] args) {
        int n = 5; // Example: print sequence up to 5 (this can be modified)
        NumberPrinter printer = new NumberPrinter(); // Create an instance of NumberPrinter to print numbers
        ThreadController controller = new ThreadController(printer, n); // Create ThreadController with NumberPrinter and upper limit 'n'

        // Create threads for ZeroThread, OddThread, and EvenThread
        Thread zeroThread = new Thread(() -> {
            try {
                controller.printZero(); // Start ZeroThread to print zeros
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "ZeroThread");

        Thread oddThread = new Thread(() -> {
            try {
                controller.printOdd(); // Start OddThread to print odd numbers
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "OddThread");

        Thread evenThread = new Thread(() -> {
            try {
                controller.printEven(); // Start EvenThread to print even numbers
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "EvenThread");

        // Start all threads
        zeroThread.start();
        oddThread.start();
        evenThread.start();

        // Wait for all threads to finish execution
        try {
            zeroThread.join(); // Wait for ZeroThread to finish
            oddThread.join(); // Wait for OddThread to finish
            evenThread.join(); // Wait for EvenThread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(); // Newline after output for readability
    }
}
