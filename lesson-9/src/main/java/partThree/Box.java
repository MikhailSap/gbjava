package partThree;

import java.util.ArrayList;

public class Box<T extends Fruit> {
    private final ArrayList<T> ITEMS = new ArrayList<T>();

    public void addItem(T item) {
        ITEMS.add(item);
    }

    public float getWeight() {
        float boxWeight = 0;
        for (T item : ITEMS) {
            boxWeight += item.getWeight();
        }
        return boxWeight;
    }

    public boolean compare(Box another) {
        return this.getWeight() == another.getWeight();
    }

    public void intersperse(Box<T> another) {
        for (T item : ITEMS)
            another.addItem(item);
        ITEMS.clear();
    }
}
