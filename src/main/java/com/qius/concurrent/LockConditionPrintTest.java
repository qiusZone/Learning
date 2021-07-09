package com.qius.concurrent;

import javax.sound.midi.Soundbank;
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
//        new Thread(() ->{
//            test.printABCWithCondition(0, c1, c2);
//        }).start();
//        new Thread(() ->{
//            test.printABCWithCondition(1, c2, c3);
//        }).start();
//        new Thread(() ->{
//            test.printABCWithCondition(2, c3, c1);
//        }).start();

        new Thread(() ->{
            test.printABCRecur(0, c1, c2, "A");
        }).start();
        new Thread(() ->{
            test.printABCRecur(1, c2, c3, "B");
        }).start();
        new Thread(() ->{
            test.printABCRecur(2, c3, c1, "C");
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
                        //  释放当前锁 从锁的入口等待队列中唤醒一个线程 然后阻塞自己
                        cur.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (num >= 100) break;
                System.out.println(Thread.currentThread().getName() + " print " + ++num);
                // 唤醒阻塞在next的条件等待队列中的一个线程 重新竞争锁 如果拿不到 去入口等待队列
                next.signal();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

        }
    }

    /**
     * 使用condition循环打印ABC 10次
     * @param targetNum
     * @param cur
     * @param next
     * @param str
     */
    private void printABCRecur(int targetNum, Condition cur, Condition next, String str){
        for(int i = 0; i < 10; i++){
            lock.lock();
            while (num%3 != targetNum){
                try {
                    cur.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("thread " + targetNum + "print " + str);
            num++;
            next.signal();
        }
    }




}
