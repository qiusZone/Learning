package com.qius.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 采用Lock + Condition实现对线程的精准唤醒
 * 避免同步锁的无意义竞争
 */
public class LockConditionPrintTest {

    public static void main(String[] args) {
        LockConditionPrintTest test = new LockConditionPrintTest();
        new Thread(() ->{
            test.printABCWithCondition(0, c1, c2);
        }).start();
        new Thread(() ->{
            test.printABCWithCondition(1, c2, c3);
        }).start();
        new Thread(() ->{
            test.printABCWithCondition(2, c3, c1);
        }).start();
    }

    private int num;
    private static Lock lock = new ReentrantLock();
    private static Condition c1 = lock.newCondition();
    private static Condition c2 = lock.newCondition();
    private static Condition c3 = lock.newCondition();

    /**
     * 三个线程循环打印1-100
     * 使用lock+condition 唤醒特定线程
     * @param targetNum 线程编号
     * @param cur 当前condition
     * @param next 下一个condition
     */
    private void printABCWithCondition(int targetNum, Condition cur, Condition next){

        while (true){
            lock.lock();
            try{
                while (num % 3 != targetNum){

                    if (num >= 100) break;
                    try {
                        // 阻塞当前线程 释放当前锁 并处于等待状态
                        cur.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (num >= 100) break;
                System.out.println(Thread.currentThread().getName() + " print " + ++num);
                // 唤醒下一个线程 而不是唤醒所有线程
                next.signal();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

        }
    }


    /**
     * 使用信号量
     * {@link SemaphorePrintTest}
     * 见##
     */


}
