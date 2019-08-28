public class RunnigTrack extends Barrier{
    private int length;

    public RunnigTrack(int length) {
        this.length = length;
    }

    public boolean overcome(Movable movable) {
        movable.run(length);
        return movable.isPass();
    }
}
