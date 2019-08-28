public class Wall extends Barrier{
    private int height;

    public Wall(int height) {
        this.height = height;
    }

    public boolean overcome(Movable movable) {
        movable.jump(height);
        return movable.isPass();
    }
}
