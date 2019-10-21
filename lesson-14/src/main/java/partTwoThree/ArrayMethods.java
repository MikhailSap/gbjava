package partTwoThree;


public class ArrayMethods {
    public int[] arrayFilter(int[] array) {
        int indexOfLastFourNumber = -1;
        for (int i = 0; i < array.length; i++)
            if (array[i] == 4)
                indexOfLastFourNumber = i;

        if (indexOfLastFourNumber == -1)
            throw new RuntimeException();

        int countElementsAfterFilter = array.length - (indexOfLastFourNumber+1);
        int[] result = new int[countElementsAfterFilter];
        System.arraycopy(array, indexOfLastFourNumber+1, result, 0, countElementsAfterFilter);

        return result;
    }

    public boolean arrayCheck(int[] array) {
        boolean oneExist = false;
        boolean fourExist = false;
        for (int i : array) {
            if (i != 1 && i != 4)
                return false;
            if (i == 1)
                oneExist = true;
            if (i == 4)
                fourExist = true;
        }

        return oneExist&&fourExist;
    }
}
