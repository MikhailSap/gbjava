package partTwo;

import java.awt.*;

public class Cell {
    private boolean isOpen;
    private boolean isMine;
    private boolean isFlag;
    private boolean isExplosion;
    private int countNearMines;
    private Color[] colors = {Color.GRAY, Color.BLUE, Color.GREEN, Color.ORANGE,
            Color.MAGENTA, Color.RED, Color.CYAN, Color.YELLOW, Color.PINK};

    public void paint(Graphics graphics, int x, int y, int cellSize) {
        graphics.setFont(new Font("MyFont", Font.LAYOUT_NO_LIMIT_CONTEXT, cellSize));
        if (!isOpen) {
            graphics.setColor(Color.lightGray);
            graphics.fill3DRect(x*cellSize, y*cellSize, cellSize, cellSize, true);
            if (isFlag) {
                graphics.setColor(Color.RED);
                graphics.drawString("P", x * cellSize+8, y * cellSize+21);
                graphics.fillRect(x * cellSize+7, y * cellSize+21, 8, 2);
            }
        } else {
            if (isMine && isExplosion) {
                paintMine(graphics, x, y, cellSize, Color.RED);
            } else if(isMine) {
                paintMine(graphics, x, y, cellSize, Color.BLACK);
            } else if (countNearMines>0) {
                paintNumber(graphics, x * cellSize+5, y * cellSize+21, cellSize, colors[countNearMines]);
            }
            graphics.setColor(Color.lightGray);
            graphics.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
        }
    }

    private void paintNumber(Graphics graphics, int x, int y, int cellSize, Color color) {
        graphics.setColor(colors[countNearMines]);
        graphics.drawString(String.valueOf(countNearMines), x, y);
    }

    private void paintMine(Graphics graphics, int x, int y, int cellSize, Color color) {
        graphics.setColor(color);
        graphics.fillOval(x*cellSize+3, y*cellSize+3, cellSize-5, cellSize-5);
        graphics.setColor(Color.WHITE);
        graphics.fillOval(x*cellSize+7, y*cellSize+7, cellSize/3, cellSize/3);

    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public void turnFlag() {
        isFlag = !isFlag;
    }

    public void setExplosion(boolean explosion) {
        isExplosion = explosion;
    }

    public int getCountNearMines() {
        return countNearMines;
    }

    public void incrementCountNearMines() {
        countNearMines++;
    }
}
