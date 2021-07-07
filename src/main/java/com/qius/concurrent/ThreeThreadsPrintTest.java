package com.qius.concurrent;

/**
 * 三个线程 按照ABC顺序打印
 */
public class ThreeThreadsPrintTest {

    private int num;
    private static final Object lock = new Object();

    public static void main(String[] args) {
        ThreeThreadsPrintTest threeThreadsPrintTest = new ThreeThreadsPrintTest();
        // 按照次序打印ABC
//        new Thread(() ->{
//            threeThreadsPrintTest.printABC(0, "A");
//        }).start();
//
//        new Thread(() ->{
//            threeThreadsPrintTest.printABC(1, "B");
//        }).start();
//
//        new Thread(() -> {
//            threeThreadsPrintTest.printABC(2, "C");
//        }).start();

        // 按照次序打印ABC 循环n次
        new Thread(() ->{
            threeThreadsPrintTest.printABCRecur(0, "A",10);
        }).start();

        new Thread(() ->{
            threeThreadsPrintTest.printABCRecur(1, "B", 10);
        }).start();

        new Thread(() -> {
            threeThreadsPrintTest.printABCRecur(2, "C", 10);
        }).start();

    }

    public void printABC(int targetNum, String str){
        // 使用synchronized + wait/notify
        // 同时使用num的值来判断由那个被唤醒的线程来执行
        synchronized (lock){
            while (num % 3 != targetNum){
                // 不相等则等待
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            // 执行任务
            num++;
            System.out.println("thread_" + str + " print num " + num);
            // 任务完成后 唤醒所有的线程
            lock.notifyAll();
        }
    }

    // 循环n次打印str字符 targetNum表示打印的顺序 从0开始
    public void printABCRecur(int targetNum, String str, int n){
        for (int i = 0; i < n; i++){
            synchronized (lock){
                if (num%3 != targetNum){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 执行任务
                num++;
                System.out.println("thread_" + str + " print num " + num);
                // 任务完成后 唤醒所有的线程
                /**
                 * 该唤醒操作可能引起同步锁的无意义竞争 建议使用lock+condition或semaphore
                 */
                lock.notifyAll();
            }
        }
    }


}
