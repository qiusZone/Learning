package com.qius.concurrent;

import lombok.SneakyThrows;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 两个线程 交替打印1-100的数字
 */
public class TwoThreadsPrintTest {

    public static void main(String[] args) throws InterruptedException {

//        Thread thread0 = new PrintNumber(0);
//        Thread thread1 = new PrintNumber(1);
//
//        thread0.start();
//        thread1.start();

        // test print with synchronized
        new Thread(new Print()).start();
        new Thread(new Print()).start();

//        //test print with Condition
//        new Thread(new PrintWithCondition()).start();
//        new Thread(new PrintWithCondition()).start();

    }

    /**
     * 使用synchronized实现互斥 notify/wait来实现同步
     */
    public static class Print implements Runnable{
        static int value = 0;
        @Override
        public void run() {
            while (value < 100){
                // 互斥访问临界资源
                synchronized (Print.class){

                    // 打印
                    System.out.println(Thread.currentThread().getName() + " print " + ++value);
                    // 打印到100 需要唤醒 并终止程序
                    if(value == 100) {
                        Print.class.notify();
                        break;
                    }
                    // 执行完当前打印任务 唤醒其他线程
                    Print.class.notify();
                    try {
                        // 阻塞当前线程
                        Print.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 使用lock + condition来实现
     */
    public static class PrintWithCondition implements Runnable{
        static int value = 0;
        static ReentrantLock lock = new ReentrantLock();
        static Condition condition = lock.newCondition();

        @Override
        public void run() {
            while (value < 100){
                try {
                    // 互斥 上锁
                    lock.lock();
                    System.out.println(Thread.currentThread().getName() + " print " + ++value);
                    // 唤醒其他线程
                    condition.signal();
                    // 阻塞当前线程
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }

            }
        }
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

        @Override
        public void run(){

            while (count < 100){
                while (count % 2 == id){
                    try {
                        lock.lock();
                        System.out.println(Thread.currentThread().getName() + " print " + ++count);
                    }finally {
                        lock.unlock();
                    }
                }

            }
        }


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
//        @Override
//        public void run() {
//
//            while (count < 100){
//                lock.lock();
//                // 执行任务
//                System.out.println("thread_" + id + " print num " + (++count));
//                // 唤醒等待在该condition上的线程
//                condition.signal();
//                try {
//                    // 等待该condition
//                    condition.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}


