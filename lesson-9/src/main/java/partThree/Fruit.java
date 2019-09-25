package partThree;

public abstract class Fruit {
    private final float WEIGHT;

    public Fruit(float weight) {
        WEIGHT = weight;
    }

    public float getWeight() {
        return WEIGHT;
    }
}
