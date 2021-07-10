package com.qius.concurrent;

import java.util.concurrent.Semaphore;

/**
 * 使用信号量
 * * Semaphore：用来控制同时访问某个特定资源的操作数量，或者同时执行某个制定操作的数量
 * * Semaphore内部维护了一个计数器，其值为可以访问的共享资源的个数。
 * *
 * * 一个线程要访问共享资源，先使用acquire()方法获得信号量，
 * * 如果信号量的计数器值大于等于1，意味着有共享资源可以访问，则使其计数器值减去1，再访问共享资源。
 * * 如果计数器值为0,线程进入休眠。
 * *
 * * 当某个线程使用完共享资源后，使用release()释放信号量，并将信号量内部的计数器加1，
 * * 之前进入休眠的线程将被唤醒并再次试图获得信号量。
 */
public class SemaphorePrintTest {

    public static void main(String[] args) {
        SemaphorePrintTest test = new SemaphorePrintTest();
        // test 使用信号量 循环打印ABC 10次
//        new Thread(() -> {
//            test.printABCWithSemaphore(s1, s2);
//        }, "A").start();
//        new Thread(() -> {
//            test.printABCWithSemaphore(s2, s3);
//        }, "B").start();
//        new Thread(() -> {
//            test.printABCWithSemaphore(s3, s1);
//        }, "C").start();

        // test 使用信号量 3个线程循环打印1-100
        new Thread(() -> {
            test.print(s1, s2);
        }, "A").start();
        new Thread(() -> {
            test.print(s2, s3);
        }, "B").start();
        new Thread(() -> {
            test.print(s3, s1);
        }, "C").start();
    }

    // s1为1 从获取信号量s1的线程开始执行
    private static Semaphore s1 = new Semaphore(1);
    private static Semaphore s2 = new Semaphore(0);
    private static Semaphore s3 = new Semaphore(0);

    private static int num;

    /**
     * 循环10次 依次打印ABC
     *
     * @param cur  当前信号量
     * @param next 下一个信号量
     */
    private void printABCWithSemaphore(Semaphore cur, Semaphore next) {
        for (int i = 0; i < 10; i++) {

            try {
                // 获取当前信号量
                cur.acquire();
                System.out.println(Thread.currentThread().getName());
                // 唤醒下一个信号量
                next.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 循环打印1-100
     * @param cur
     * @param next
     */
    private void print(Semaphore cur, Semaphore next){
        while (true){
            try {
                // 获取当前信号量
                cur.acquire();
                // 当到达100释放下一个信号量
                if (num == 100)  {
                    next.release();
                    break;
                }
                System.out.println(Thread.currentThread().getName() + " print " + ++num);

                next.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
