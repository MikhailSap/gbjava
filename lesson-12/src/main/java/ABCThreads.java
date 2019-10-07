/*
Приветствую! Попробовал Ваш код на Mac и получилась другая картина (A B A B A B...)
Попробовал на Windows и получилось как у Вас, но если отключить sleep то картина меняется (то A B C то B A C)
Сложилось впечатление что винда включает потоки на разных ядрах а мак нет, к тому же notifyAll не гарантирует что потоки
возобновят свою работу в том порядке в котором они попали в wait set.
В итоге помучившись пришел к выводу что таким способом реализовать гарантированно  AABCC неполучится и подглядев в методичку
сделал следующее.. получаеться для гарантии нам нужен внешний фактор который будет держать поток в wait set, мб я все же чот
не так понял
 */


public class ABCThreads {
    private static Runnable a, b, c;
    private static Object monitor = new Object();
    private volatile String leter = "A";


    public static void main(String[] args) throws Exception{
        ABCThreads abcThreads = new ABCThreads();

        a = () -> abcThreads.printA(2);
        b = () -> abcThreads.printB(1);
        c = () -> abcThreads.printC(2);

        Thread threadA = new Thread(a);
        Thread threadB = new Thread(b);
        Thread threadC = new Thread(c);

        threadA.start();
        threadB.start();
        threadC.start();
    }

    public void printA(int count) {
        synchronized (monitor) {
            try {
                for (int i = 0; i <5; i++) {
                    while (!leter.equals("A"))
                        monitor.wait();
                    for (int j = 0; j < count; j++) {
                        System.out.print(leter+" ");
                        Thread.sleep(1000);
                    }
                    leter = "B";
                    monitor.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printB(int count) {
        synchronized (monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (!leter.equals("B"))
                        monitor.wait();
                    for (int j = 0; j < count; j++) {
                        System.out.print(leter+" ");
                        Thread.sleep(1000);
                    }
                    leter = "C";
                    monitor.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printC(int count) {
        synchronized (monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (!leter.equals("C"))
                        monitor.wait();
                    for (int j = 0; j < count; j++) {
                        System.out.print(leter+" ");
                        Thread.sleep(1000);
                    }
                    leter = "A";
                    monitor.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
