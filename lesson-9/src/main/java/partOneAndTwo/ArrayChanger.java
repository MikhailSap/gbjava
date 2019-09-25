package partOneAndTwo;

import java.util.ArrayList;
import java.util.Collections;


public class ArrayChanger<T> {

    //1
    public void swapElements(T[] array, int index1, int index2) {
        T tmp = array[index1];
        array[index1] = array[index2];
        array[index2] = tmp;
    }

    //2
    public ArrayList<T> arrayToList(T[] array) {
        ArrayList<T> list = new ArrayList<T>();
        Collections.addAll(list, array);
        return list;
    }
}
