/*
            Question 3 b
            A Game of Tetris 
            Functionality: 
            Queue: Use a queue to store the sequence of falling blocks. 
            Stack: Use a stack to represent the current state of the game board. 
            GUI: 
            A game board with grid cells. 
            A preview area to show the next block. 
            Buttons for left, right, and rotate. 
            Implementation: 
            Initialization: 
             Create an empty queue to store the sequence of falling blocks. 
             Create an empty stack to represent the game board. 
             Initialize the game board with empty cells. 
             Generate a random block and enqueue it. 
            Game Loop: 
            While the game is not over: 
             Check for game over: If the top row of the game board is filled, the game is over. 
             Display the game state: Draw the current state of the game board and the next block in the 
            preview area. 
            Handle user input: 
             If the left or right button is clicked, move the current block horizontally if possible. 
             If the rotate button is clicked, rotate the current block if possible. 
             Move the block: If the current block can move down without colliding, move it down. Otherwise: 
             Push the current block onto the stack, representing its placement on the game board. 
             Check for completed rows: If a row is filled, pop it from the stack and add a new empty row at the 
            top. 
             Generate a new random block and enqueue it. 
            Game Over: 
             Display a game over message and the final score. 
            Data Structures: 
            Block: A class or struct to represent a Tetris block, including its shape, color, and current position. 
            GameBoard: A 2D array or matrix to represent the game board, where each cell can be empty or filled 
            with a block. 
            Queue: A queue to store the sequence of falling blocks. 
            Stack: A stack to represent the current state of the game board. 
            Additional Considerations: 
            Collision detection: Implement a function to check if a block can move or rotate without colliding with 
            other blocks or the game board boundaries. 
            Scoring: Implement a scoring system based on factors like completed rows, number of blocks placed, and 
            other game-specific rules. 
            Leveling: Increase the speed of the falling blocks as the player's score increases. 
            Power-ups: Add power-ups like clearing lines, adding extra rows, or changing the shape of the current 
            block.
 */


/*
            * The Tetrisgame class expands JPANEL and implements ActionListener to create a simple Tetris game in Java.
            *  He uses a two -dimensional payment of the array to present a queue for controlling the game grid and falling 
            * blocks. The game has seven types of blocks and each is 2D Massin, respectively. The logic of the game includes 
            * block creation, movement (left, right, down), rotation, line cleaning and block layout. The block falls automatically
            *  and the game checks and cleans the entire row. Timer objects are used to move blocks through regular intervals. 

            * The game listens to the key stroke and moves or rotates the current block. The Handlekeypress () method processes 
            * the user input to move the block to the left, right, down, or rotate. The PaintComponent () method is used to draw a 
            * game board and the current block. The block of the block represents each falling block having a form determined by 
            * position x and y and 2D arrays. The game works on JFRAME, and when the block falls, the screen updates the screen
            *  to confirm the collision and complete the line.
 */


package Question3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class TetrisGame extends JPanel implements ActionListener {
    // Constants for the game grid dimensions and cell size
    private static final int ROWS = 20, COLS = 10, CELL_SIZE = 30;
    
    // Timer to control the speed of the game
    private Timer timer;
    
    // Queue to hold the blocks (though not used actively here)
    private Queue<Block> blockQueue;
    
    // Board to represent the game grid
    private int[][] board;
    
    // Random object to generate random numbers for block shapes
    private Random random;
    
    // Current block object being controlled by the player
    private Block currentBlock;
    
    // Predefined shapes for the blocks (Tetris pieces)
    private static final int[][][] SHAPES = {
        {{1, 1}, {1, 1}}, // Square block
        {{1, 1, 1, 1}},   // Horizontal line
        {{1}, {1}, {1}, {1}}, // Vertical line
        {{0, 1, 1}, {1, 1, 0}}, // Z shape
        {{1, 1, 0}, {0, 1, 1}}, // S shape
        {{1, 1, 1}, {0, 1, 0}}, // T shape
        {{1, 1, 1}, {1, 0, 0}}, // L shape
        {{1, 1, 1}, {0, 0, 1}}  // Reverse L shape
    };

    // Constructor to set up the game panel and initialize variables
    public TetrisGame() {
        this.setPreferredSize(new Dimension(COLS * CELL_SIZE, ROWS * CELL_SIZE)); // Set the panel size
        this.setBackground(Color.BLACK); // Set the background color of the panel
        board = new int[ROWS][COLS]; // Initialize the game board
        blockQueue = new LinkedList<>(); // Initialize the block queue
        random = new Random(); // Create a random object for randomizing block shapes
        generateNewBlock(); // Generate the first block
        timer = new Timer(500, this); // Timer to move the block down every 500ms
        timer.start(); // Start the timer
        setFocusable(true); // Allow the panel to receive keyboard events
        addKeyListener(new KeyAdapter() {
            // Listen for key presses to move or rotate the block
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }
        });
    }

    // Method to generate a new block at the top of the screen
    private void generateNewBlock() {
        currentBlock = new Block(random.nextInt(COLS - 2), 0, SHAPES[random.nextInt(SHAPES.length)]); // Create a new block at a random position
        blockQueue.offer(currentBlock); // Add the new block to the queue
    }

    // Method to check if a block can move in the given direction (dx, dy)
    private boolean canMove(int dx, int dy) {
        for (int i = 0; i < currentBlock.shape.length; i++) {
            for (int j = 0; j < currentBlock.shape[i].length; j++) {
                if (currentBlock.shape[i][j] == 1) {
                    int newX = currentBlock.x + j + dx;
                    int newY = currentBlock.y + i + dy;
                    // Check if the move goes outside the grid or collides with another block
                    if (newX < 0 || newX >= COLS || newY >= ROWS || (newY >= 0 && board[newY][newX] == 1)) {
                        return false; // Cannot move
                    }
                }
            }
        }
        return true; // Can move
    }

    // Method to move the block down by one row
    private void moveBlockDown() {
        if (canMove(0, 1)) {
            currentBlock.y++; // Move the block down if possible
        } else {
            placeBlock(); // Place the block on the board if it cannot move further
        }
    }

    // Method to place the current block on the board
    private void placeBlock() {
        for (int i = 0; i < currentBlock.shape.length; i++) {
            for (int j = 0; j < currentBlock.shape[i].length; j++) {
                if (currentBlock.shape[i][j] == 1) {
                    board[currentBlock.y + i][currentBlock.x + j] = 1; // Mark the block position on the board
                }
            }
        }
        checkRows(); // Check if any rows are full and need to be cleared
        generateNewBlock(); // Generate a new block after placing the current one
    }

    // Method to check for full rows and clear them
    private void checkRows() {
        for (int i = ROWS - 1; i >= 0; i--) {
            boolean fullRow = true;
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == 0) {
                    fullRow = false; // Row is not full
                    break;
                }
            }
            if (fullRow) {
                clearRow(i); // Clear the full row
            }
        }
    }

    // Method to clear a full row and move the rows above it down
    private void clearRow(int row) {
        for (int i = row; i > 0; i--) {
            board[i] = board[i - 1].clone(); // Move the rows down
        }
        board[0] = new int[COLS]; // Clear the top row
    }

    // Method to handle key presses (left, right, down, up for movement and rotation)
    private void handleKeyPress(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                if (canMove(-1, 0)) currentBlock.x--; // Move left
                break;
            case KeyEvent.VK_RIGHT:
                if (canMove(1, 0)) currentBlock.x++; // Move right
                break;
            case KeyEvent.VK_DOWN:
                moveBlockDown(); // Move down
                break;
            case KeyEvent.VK_UP:
                rotateBlock(); // Rotate the block
                break;
        }
        repaint(); // Redraw the screen
    }

    // Method to rotate the current block 90 degrees clockwise
    private void rotateBlock() {
        int[][] rotatedShape = new int[currentBlock.shape[0].length][currentBlock.shape.length];
        for (int i = 0; i < currentBlock.shape.length; i++) {
            for (int j = 0; j < currentBlock.shape[i].length; j++) {
                rotatedShape[j][currentBlock.shape.length - 1 - i] = currentBlock.shape[i][j];
            }
        }
        currentBlock.shape = rotatedShape; // Update the block shape with the rotated shape
    }

    // Method to update the game state every 500ms (move the block down)
    @Override
    public void actionPerformed(ActionEvent e) {
        moveBlockDown();
        repaint(); // Redraw the screen after moving the block
    }

    // Method to render the game board and current block
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw the board (filled cells are part of placed blocks)
        g.setColor(Color.GRAY);
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == 1) {
                    g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE); // Draw filled cell
                }
            }
        }
        
        // Draw the current block (active block)
        g.setColor(Color.RED);
        for (int i = 0; i < currentBlock.shape.length; i++) {
            for (int j = 0; j < currentBlock.shape[i].length; j++) {
                if (currentBlock.shape[i][j] == 1) {
                    g.fillRect((currentBlock.x + j) * CELL_SIZE, (currentBlock.y + i) * CELL_SIZE, CELL_SIZE, CELL_SIZE); // Draw block
                }
            }
        }
    }

    // Main method to set up the game window and start the game
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        TetrisGame game = new TetrisGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true); // Show the game window
    }

    // Block class to represent the individual blocks (pieces) in the game
    class Block {
        int x, y; // Position of the block
        int[][] shape; // Shape of the block (array representation)

        Block(int x, int y, int[][] shape) {
            this.x = x;
            this.y = y;
            this.shape = shape; // Initialize the block with a position and shape
        }
    }
}
