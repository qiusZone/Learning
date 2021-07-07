package com.qius.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 两个线程 交替打印1-100的数字
 */
public class TwoThreadsPrintTest {

    public static void main(String[] args) throws InterruptedException {

        Thread thread0 = new PrintNumber(0);
        Thread thread1 = new PrintNumber(1);

        thread0.start();
        thread1.start();

    }

    public static class PrintNumber extends Thread {

        private static int count = 0;
        private int id;

        // 使用lock和condition
        private static Lock lock = new ReentrantLock();
        private static Condition condition = lock.newCondition();

        public PrintNumber(int id) {
            this.id = id;
        }

        /**
         * 使用synchronized同步方法 需要通过count%2判断为哪个线程
         */
//        @Override
//        public void run() {
//
//            while (count < 100) {
//                while (count % 2 == id) {
//                    synchronized (PrintNumber.class) {
//                        count++;
//                        System.out.println("thread_" + id + " print num " + count);
//                    }
//                }
//
//            }
//        }


        /**
         * 使用synchronized + notify/wait机制
         * 当一个线程执行完自己的工作，然后唤醒另一个线程，自己去休眠，这样每个线程就不用忙等。
         */
//        @Override
//        public void run() {
//            while (count < 100){
//                synchronized (PrintNumber.class){
//                    count++;
//                    System.out.println("thread_" + id + " print num " + count);
//
//                    // 通知其他线程 释放锁资源
//                    PrintNumber.class.notify();
//                    try {
//                        // 等待锁资源
//                        PrintNumber.class.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }

        /**
         *
         */
        @Override
        public void run() {

            while (count < 100){
                lock.lock();
                // 执行任务
                System.out.println("thread_" + id + " print num " + (++count));
                // 唤醒等待在该condition上的线程
                condition.signal();
                try {
                    // 等待该condition
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


