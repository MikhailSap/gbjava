public class TimeTest {
    private static final int ARRAY_SIZE = 100000000;
    private static final int HALF_ARRAY_SIZE = ARRAY_SIZE/2;
    private static float array[] = new float[ARRAY_SIZE];

    public static void main(String[] args) {
        testForOneThread();
        testForTwoThreads();
    }


    public static void testForOneThread() {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = 1;
        }

        long t1 = System.currentTimeMillis();

        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = (float)(array[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }

        long t2 = System.currentTimeMillis();

        System.out.println(t2-t1);
    }

    public static void testForTwoThreads() {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = 1;
        }

        long t1 = System.currentTimeMillis();

        float array1[] = new float[HALF_ARRAY_SIZE];
        float array2[] = new float[HALF_ARRAY_SIZE];
        System.arraycopy(array, 0, array1, 0, array1.length);
        System.arraycopy(array, HALF_ARRAY_SIZE, array2, 0, array2.length);

        Thread thread1 = new Thread(new MyRunnable(array1, 0));
        Thread thread2 = new Thread(new MyRunnable(array2, HALF_ARRAY_SIZE));
        thread1.start();
        thread2.start();

        float resultArray[] = new float[ARRAY_SIZE];
        System.arraycopy(array1, 0, resultArray, 0, array1.length);
        System.arraycopy(array2, 0, resultArray, HALF_ARRAY_SIZE, array2.length);

        long t2 = System.currentTimeMillis();

        System.out.println(t2-t1);
    }

    public static class MyRunnable implements Runnable {
        float array[];
        int indexOFSourceArray;

        public MyRunnable(float[] array, int indexOFSourseArray) {
            this.array = array;
            this.indexOFSourceArray = indexOFSourseArray;
        }

        public void run() {
            for (int i = 0; i < this.array.length; i++) {
               this.array[i] = (float)(this.array[i] * Math.sin(0.2f + indexOFSourceArray / 5) * Math.cos(0.2f + indexOFSourceArray / 5) * Math.cos(0.4f + indexOFSourceArray / 2));
               indexOFSourceArray++;
            }
        }
    }
}
