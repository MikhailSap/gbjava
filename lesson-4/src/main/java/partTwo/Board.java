package partTwo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Board extends JFrame {
    private final int CELL_SIZE = 25;
    private final int AMENDMENT = 23;
    private int capacity;
    private Cell[][] cells;
    private final int LEFT_MOUSE_BUTTON = 1;
    private final int RIGHT_MOUSE_BUTTON = 3;
    private final int COUNT_OPEN_CELLS_FOR_WIN;
    private boolean isGameOver;
    private int openCells;

    public Board(int capacity, final Cell[][] cells, int countOpenCellsForWin) {
        this.capacity = capacity;
        this.cells = cells;
        this.COUNT_OPEN_CELLS_FOR_WIN = countOpenCellsForWin;
        setTitle("MINER");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int width = capacity * CELL_SIZE;
        int height = capacity * CELL_SIZE + AMENDMENT;
        setSize(width, height);
        int startX = dimension.width / 2 - width / 2;
        int startY = dimension.height / 3 - height / 2;
        setLocation(startX, startY);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MyPanel background = new MyPanel();
        background.setBackground(Color.white);
        add(background);
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (e.getButton() == LEFT_MOUSE_BUTTON)
                    defuse(e.getX() / CELL_SIZE, (e.getY() - AMENDMENT) / CELL_SIZE);
                else
                if (e.getButton() == RIGHT_MOUSE_BUTTON)
                    cells[e.getX() / CELL_SIZE][(e.getY() - AMENDMENT) / CELL_SIZE].turnFlag();
                repaint();
                if (isGameOver) {
                    setTitle("GAME OVER");
                    removeMouseListener(this);
                }
                if (openCells == COUNT_OPEN_CELLS_FOR_WIN) {
                    background.setBackground(Color.PINK);
                    setTitle("YOU WIN");
                    repaint();
                    removeMouseListener(this);
                }
            }
        });
        setVisible(true);
    }

    private void defuse(int x, int y) {
        Cell currentCell = cells[x][y];
        if (currentCell.isMine()) {
            currentCell.setExplosion(true);
            for (int i = 0; i < capacity; i++)
                for (int j = 0; j < capacity; j++)
                    cells[i][j].setOpen(true);
                isGameOver = true;
        } else {
            open(x, y);
        }
    }

    private void open(int x, int y) {
        Cell currentCell = cells[x][y];
        if (currentCell.isOpen())
            return;
        currentCell.setOpen(true);
        openCells++;
        for (int i = x - 1; i <= x + 1; i++)
            for (int j = y - 1; j <= y + 1; j++)
                try {
                    if (cells[i][j].isMine())
                        currentCell.incrementCountNearMines();
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
        if (currentCell.getCountNearMines() == 0)
            for (int i = x - 1; i <= x + 1; i++)
                for (int j = y - 1; j <= y + 1; j++)
                    try {
                        open(i, j);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        return;
                    }
    }

    class MyPanel extends JPanel {
        public void paint(Graphics graphics) {
            super.paint(graphics);
                for (int x = 0; x < capacity; x++)
                    for (int y = 0; y < capacity; y++)
                        cells[x][y].paint(graphics, x, y, CELL_SIZE);
        }
    }
}

