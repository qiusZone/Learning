package com.qius.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 阻塞队列
 * 当容量满了则阻塞不能添加
 * 当容量为空则阻塞不能取值
 *
 * @author qiusong
 * @date 2021/7/8.
 * @see [相关类/方法]
 * @since BlockingQueue 1.0
 */
public class BlockingQueue {

    // 队列容器
    private List<Integer> container = new ArrayList<>();
    // lock
    private Lock lock = new ReentrantLock();
    // condition
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    // 队列元素统计
    private volatile int size;
    private volatile int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    // 添加元素
    public void add(int data) {

        // 获取锁
        lock.lock();

        try {
            try {
                // 判断队列是否满
                while (size >= capacity) {
                    System.out.println("队列已满,释放锁,等待消息者消费数据");
                    notFull.await();
                }
            } catch (InterruptedException e) {
                notFull.notify();
                e.printStackTrace();
            }
            ++size;
            container.add(data);
            // 唤醒 消费者取数据
            notEmpty.notify();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 消费者 取数据
     *
     * @return 数据
     */
    public int take() {

        // 获取锁
        lock.lock();
        try {
            try {
                // 判断队列是否为空
                while (size == 0) {
                    System.out.println("队列已空,释放锁,等待生产者生产数据");
                    notEmpty.await();
                }
            } catch (InterruptedException e) {
                notEmpty.notify();
                e.printStackTrace();
            }

            --size;
            int res = container.remove(0);
            // 唤醒生产者生产数据
            notFull.notify();
            return res;
        } finally {
            lock.unlock();
        }

    }
}
