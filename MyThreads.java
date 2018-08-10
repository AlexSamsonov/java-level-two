package five.online;

import java.util.Arrays;

public class MyThreads {

    private static final int size = 10000000;
    private static final int h = size / 2;

    public static void main(String[] args) {
        TwoArrays ta = new TwoArrays();
        System.out.println("Race began!");
        new Thread(() -> ta.firstArray()).start();
        new Thread(() -> ta.secondArray()).start();
        new Thread(() -> ta.arrayEquality()).start();
    }

    public static class TwoArrays extends Thread {

        Object lock = new Object();
        float[] arr1 = new float[size];
        float[] arr2 = new float[size];

        float[] firstArray() {
            long a = System.currentTimeMillis();
            for (int i = 0; i < arr1.length; i++) {
                arr1[i] = 1;
                arr1[i] = (float) (arr1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) *
                        Math.cos(0.4f + i / 2));
            }
            System.out.println("First finished: " + (System.currentTimeMillis() - a) + " ms");
            return arr1;
        }

        synchronized void secondArray() {
            float[] a1 = new float[h];
            float[] a2 = new float[h];
            long b = System.currentTimeMillis();
            for (int i = 0; i < arr2.length; i++) {
                arr2[i] = 1;
                arr2[i] = (float) (arr2[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) *
                        Math.cos(0.4f + i / 2));
            }
            //Separating
            synchronized (lock) {
                System.out.println("Separating began");
                long x = System.currentTimeMillis();
                System.arraycopy(arr2, 0, a1, 0, h);
                System.arraycopy(arr2, h, a2, 0, h);
                System.out.println("Separating finished: " + (System.currentTimeMillis() - x) + " ms");
            }
            //Connecting
            synchronized (lock) {
                System.out.println("Connecting began");
                long y = System.currentTimeMillis();
                System.arraycopy(a1, 0, arr2, 0, h);
                System.arraycopy(a2, 0, arr2, h, h);
                System.out.println("Connecting finished: " + (System.currentTimeMillis() - y) + " ms");
            }
            System.out.println("Second finished: " + (System.currentTimeMillis() - b) + " ms");
        }

        synchronized void arrayEquality() {
            if (Arrays.equals(arr1, arr2)) {
                System.out.println("Arrays are equal!");
            } else {
                System.out.println("Arrays are not equal!");
            }
        }
    }
}