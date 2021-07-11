package com.qius.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AlternatePrintTest {

    public static void main(String[] args) {
        new Thread(new T3()).start();
        new Thread(new T4()).start();
    }


    private static ReentrantLock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    /**
     * 线程T3 负责打印数字 1-26
     */
    static class T3 implements Runnable {
        @Override
        public void run() {
            lock.lock();
            for (int i = 1; i < 27; i++) {
                System.out.print(i + "-");
                condition.signal();
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 程序终止 唤醒
            condition.signal();
            lock.unlock();
        }

    }

    /**
     * 线程T4 负责打印小写字符 a-z
     */
    static class T4 implements Runnable {
        @Override
        public void run() {
            lock.lock();
            for (char ch = 'a'; ch <= 'z'; ch += 1) {
                System.out.print(ch + "-");

                condition.signal();
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            // 程序终止 唤醒
            condition.signal();
            lock.unlock();
        }
    }
}
