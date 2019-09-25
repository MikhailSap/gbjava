import partOneAndTwo.ArrayChanger;
import partThree.Apple;
import partThree.Box;
import partThree.Orange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        Integer[] array = {2, 1, 3};
        ArrayChanger<Integer> arrayChanger = new ArrayChanger<>();

        //1
        arrayChanger.swapElements(array, 0, 1);
        String strArray = Arrays.asList(array).stream().map(String::valueOf).collect(Collectors.joining(" "));
        System.out.println("array " + strArray);

        //2
        ArrayList<Integer> list = arrayChanger.arrayToList(array);
        System.out.println("list " + list);


        //3
        Box<Apple> appleBox = new Box<>();

        for (int i = 0; i < 15; i++) {
            appleBox.addItem(new Apple(1.0f));
        }

        Box<Orange> orangeBox = new Box<>();

        for (int i = 0; i < 10; i++) {
            orangeBox.addItem(new Orange(1.5f));
        }

        System.out.println("Вес ящиков " + (appleBox.compare(orangeBox)? "одинаков" : "разный"));

        //appleBox.intersperse(orangeBox); так нельзя!

        appleBox.intersperse(new Box<Apple>());

        System.out.println("Ящик пересыпали и его вес теперь " + appleBox.getWeight());
    }
}
