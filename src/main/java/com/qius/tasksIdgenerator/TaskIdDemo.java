package com.qius.tasksIdgenerator;

import cn.hutool.core.lang.Console;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <一句话功能描述>
 * <功能详细描述>
 *
 * @author qiusong
 * @create 2019/9/19
 * @see
 * @since 1.0.0
 */
public class TaskIdDemo {

    public static void main(String[] args) throws InterruptedException {

        // 开始的倒数锁
        final CountDownLatch start = new CountDownLatch(1);

        // 结束的倒数锁
        final CountDownLatch end = new CountDownLatch(20);

        // 20个线程
        ExecutorService exec = Executors.newFixedThreadPool(20);

        // 启动定时任务 当前测试的定时任务为：每一分钟cache数据置0
        TaskIdUtil.getInstance().createTimingSchedule();
        // 设置初始时的taskId
        TaskIdUtil.getInstance().setCurrentMaxTaskId("CBX", 100);
        TaskIdUtil.getInstance().setCurrentMaxTaskId("CY", 0);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 20; i++) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        start.await();
                        Console.log(Thread.currentThread().getName() + " id: " + TaskIdUtil.getInstance().getTaskSequence("CBX"));
                    } catch (InterruptedException e) {
                    } finally {
                        end.countDown();
                    }
                }
            };
            exec.submit(run);
        }

        // 开始获取id
        start.countDown();
        // 等待end为0
        end.await();
        exec.shutdown();
        Console.log("time costs: "+ (System.currentTimeMillis() - startTime));
        Console.log(TaskIdUtil.getInstance().getTaskId("CY"));
        Console.log(TaskIdUtil.getInstance().getTaskId("BZ"));
        Console.log(TaskIdUtil.getInstance().getTaskId("CBX"));

    }
}
