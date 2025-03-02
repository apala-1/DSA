package Question6;

class NumberPrinter {
    private int currentNumber = 1; // Start from 1 as the next number after 0
    private final int maxNumber;
    private boolean zeroPrinted = false; // Flag to track if zero has been printed.

    public NumberPrinter(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    // Getter method for currentNumber
    public synchronized int getCurrentNumber() {
        return currentNumber;
    }

    // Getter method for maxNumber
    public synchronized int getMaxNumber() {
        return maxNumber;
    }

    // Print Zero method
    public synchronized void printZero() throws InterruptedException {
        while (zeroPrinted || currentNumber > maxNumber) {
            wait(); // Wait until zero has not been printed or all numbers are printed.
        }
        System.out.print("0");
        zeroPrinted = true; // Mark that zero has been printed.
        notifyAll(); // Notify other threads that it's time for them to print.
    }

    // Print Even method
    public synchronized void printEven() throws InterruptedException {
        while (currentNumber % 2 != 0 || currentNumber > maxNumber || !zeroPrinted) {
            wait(); // Wait for the turn to print even number and ensure zero is printed first.
        }
        System.out.print(currentNumber);
        currentNumber++; // Increment to allow the next number (even or odd) to print.
        notifyAll(); // Notify other threads that it's time for them to print.
    }

    // Print Odd method
    public synchronized void printOdd() throws InterruptedException {
        while (currentNumber % 2 == 0 || currentNumber > maxNumber || !zeroPrinted) {
            wait(); // Wait for the turn to print odd number and ensure zero is printed first.
        }
        System.out.print(currentNumber);
        currentNumber++; // Increment to allow the next number (even or odd) to print.
        notifyAll(); // Notify other threads that it's time for them to print.
    }
}

class ZeroThread extends Thread {
    private final NumberPrinter printer;

    public ZeroThread(NumberPrinter printer) {
        this.printer = printer;
    }

    @Override
    public void run() {
        try {
            while (printer.getCurrentNumber() <= printer.getMaxNumber()) {
                printer.printZero();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class EvenThread extends Thread {
    private final NumberPrinter printer;

    public EvenThread(NumberPrinter printer) {
        this.printer = printer;
    }

    @Override
    public void run() {
        try {
            while (printer.getCurrentNumber() <= printer.getMaxNumber()) {
                printer.printEven();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class OddThread extends Thread {
    private final NumberPrinter printer;

    public OddThread(NumberPrinter printer) {
        this.printer = printer;
    }

    @Override
    public void run() {
        try {
            while (printer.getCurrentNumber() <= printer.getMaxNumber()) {
                printer.printOdd();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

