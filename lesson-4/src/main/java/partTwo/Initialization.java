package partTwo;

import java.util.Random;

public class Initialization {
    private final int CAPACITY = 10;
    private final int COUNT_OF_MINES = 15;
    public void initGame() {
        Cell[][] cells = new Cell[CAPACITY][CAPACITY];
        Random random = new Random();
        int x;
        int y;

        for (int i = 0 ; i < CAPACITY ; i++)
            for (int j = 0 ; j < CAPACITY; j++)
                cells[i][j] = new Cell();

        for (int i = 0 ; i < COUNT_OF_MINES ; i++)
            while (true) {
                x = random.nextInt(CAPACITY);
                y = random.nextInt(CAPACITY);
                if (cells[x][y].isMine()) {
                    continue;
                } else {
                    cells[x][y].setMine(true);
                    break;
                }
            }
        new Board(CAPACITY, cells, (int)Math.pow(CAPACITY, 2)-COUNT_OF_MINES);
    }
}
