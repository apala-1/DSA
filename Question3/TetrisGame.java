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
    private static final int ROWS = 20, COLS = 10, CELL_SIZE = 30;
    private Timer timer;
    private Queue<Block> blockQueue;
    private int[][] board;
    private Random random;
    private Block currentBlock;
    
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

    public TetrisGame() {
        this.setPreferredSize(new Dimension(COLS * CELL_SIZE, ROWS * CELL_SIZE));
        this.setBackground(Color.BLACK);
        board = new int[ROWS][COLS];
        blockQueue = new LinkedList<>();
        random = new Random();
        generateNewBlock();
        timer = new Timer(500, this);
        timer.start();
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }
        });
    }

    private void generateNewBlock() {
        currentBlock = new Block(random.nextInt(COLS - 2), 0, SHAPES[random.nextInt(SHAPES.length)]);
        blockQueue.offer(currentBlock);
    }

    private boolean canMove(int dx, int dy) {
        for (int i = 0; i < currentBlock.shape.length; i++) {
            for (int j = 0; j < currentBlock.shape[i].length; j++) {
                if (currentBlock.shape[i][j] == 1) {
                    int newX = currentBlock.x + j + dx;
                    int newY = currentBlock.y + i + dy;
                    if (newX < 0 || newX >= COLS || newY >= ROWS || (newY >= 0 && board[newY][newX] == 1)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void moveBlockDown() {
        if (canMove(0, 1)) {
            currentBlock.y++;
        } else {
            placeBlock();
        }
    }

    private void placeBlock() {
        for (int i = 0; i < currentBlock.shape.length; i++) {
            for (int j = 0; j < currentBlock.shape[i].length; j++) {
                if (currentBlock.shape[i][j] == 1) {
                    board[currentBlock.y + i][currentBlock.x + j] = 1;
                }
            }
        }
        checkRows();
        generateNewBlock();
    }

    private void checkRows() {
        for (int i = ROWS - 1; i >= 0; i--) {
            boolean fullRow = true;
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == 0) {
                    fullRow = false;
                    break;
                }
            }
            if (fullRow) {
                clearRow(i);
            }
        }
    }

    private void clearRow(int row) {
        for (int i = row; i > 0; i--) {
            board[i] = board[i - 1].clone();
        }
        board[0] = new int[COLS];
    }

    private void handleKeyPress(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                if (canMove(-1, 0)) currentBlock.x--;
                break;
            case KeyEvent.VK_RIGHT:
                if (canMove(1, 0)) currentBlock.x++;
                break;
            case KeyEvent.VK_DOWN:
                moveBlockDown();
                break;
            case KeyEvent.VK_UP:
                rotateBlock();
                break;
        }
        repaint();
    }

    private void rotateBlock() {
        int[][] rotatedShape = new int[currentBlock.shape[0].length][currentBlock.shape.length];
        for (int i = 0; i < currentBlock.shape.length; i++) {
            for (int j = 0; j < currentBlock.shape[i].length; j++) {
                rotatedShape[j][currentBlock.shape.length - 1 - i] = currentBlock.shape[i][j];
            }
        }
        currentBlock.shape = rotatedShape;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveBlockDown();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == 1) {
                    g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
        g.setColor(Color.RED);
        for (int i = 0; i < currentBlock.shape.length; i++) {
            for (int j = 0; j < currentBlock.shape[i].length; j++) {
                if (currentBlock.shape[i][j] == 1) {
                    g.fillRect((currentBlock.x + j) * CELL_SIZE, (currentBlock.y + i) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        TetrisGame game = new TetrisGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    class Block {
        int x, y;
        int[][] shape;
        Block(int x, int y, int[][] shape) {
            this.x = x;
            this.y = y;
            this.shape = shape;
        }
    }
}