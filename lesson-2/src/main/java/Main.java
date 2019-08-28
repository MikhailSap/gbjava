public class Main {
    public static void main(String[] args) {

        String[][] array = {{"0","1","2",","},{"4","5","6","7"},{"8","9","10","11"},{"12","13","14","15"}};

        int summ = 0;
        try {
            summ = checkArray(array);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(summ);
    }

    public static int checkArray(String[][] array) throws MyArraySizeExeption, MyArrayDataExeption{
        int checkArrayVolume = 16;
        int arrayVolume = 0;
        for (String[] line : array) {
            arrayVolume += line.length;
        }
        if (arrayVolume != checkArrayVolume) {
            throw new MyArraySizeExeption();
        }

        int summ = 0;
        int currentValue;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                try {
                    currentValue = Integer.parseInt(array[i][j]);
                } catch (NumberFormatException nfe) {
                    MyArrayDataExeption myArrayDataExeption = new MyArrayDataExeption();
                    myArrayDataExeption.setiCell(i);
                    myArrayDataExeption.setjCell(j);
                    throw myArrayDataExeption;
                }
                summ += currentValue;
            }
        }
        return summ;
    }
}
