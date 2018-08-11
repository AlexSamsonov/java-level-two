package five.online;

public class MyThreads {

    private static final int size = 10000000;
    private static final int h = size / 2;
    static float[] arr = new float[size];

    public static void main(String[] args) {
        TwoArrays ta = new TwoArrays();
        System.out.println("Threads started!");
        new Thread(() -> ta.firstArray()).start();
        new Thread(() -> ta.secondArray()).start();
    }

    public static class TwoArrays extends Thread {

        static final Object lock = new Object();

        void firstArray() {
            long a = System.currentTimeMillis();
            for (int i = 0; i < arr.length; i++) {
                arr[i] = 1;
                arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) *
                        Math.cos(0.4f + i / 2));
            }
            System.out.println("First finished: " + (System.currentTimeMillis() - a) + " ms");
        }

        void secondArray() {
            float[] a1 = new float[h];
            float[] a2 = new float[h];
            long b = System.currentTimeMillis();
            for (int i = 0; i < arr.length; i++) {
                arr[i] = 1;
                arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) *
                        Math.cos(0.4f + i / 2));
            }
            //Separating
            synchronized (lock) {
                System.out.println("Separating began");
                long x = System.currentTimeMillis();
                System.arraycopy(arr, 0, a1, 0, h);
                System.arraycopy(arr, h, a2, 0, h);
                System.out.println("Separating finished: " + (System.currentTimeMillis() - x) + " ms");
            }
            //Connecting
            synchronized (lock) {
                System.out.println("Connecting began");
                long y = System.currentTimeMillis();
                System.arraycopy(a1, 0, arr, 0, h);
                System.arraycopy(a2, 0, arr, h, h);
                System.out.println("Connecting finished: " + (System.currentTimeMillis() - y) + " ms");
            }
            System.out.println("Second finished: " + (System.currentTimeMillis() - b) + " ms");
        }
    }
}