package com.qius.test.interruptTest;

/**
 * 测试Thread join方法
 * join方法的底层实现是通过调用wait方法阻塞住调用join方法的主线程 直到子线程自行结束 以此保证多线程的顺序
 *
 * 常见的应用场景是：主线程需要等待子线程执行结束后再进行后续操作 此时可通过调用子线程的join方法来等待子线程执行完毕 再进行后续操作
 *
 * <功能详细描述>
 *
 * @author qiusong
 * @date 2021/5/26.
 * @see [相关类/方法]
 * @since PlayerMatcher 1.0
 */
public class ThreadJoinTest extends Thread{

    int i;
    Thread previousThread;
    public ThreadJoinTest(Thread previousThread, int i){
        this.previousThread = previousThread;
        this.i = i;
    }

    @Override
    public void run() {
        try {
            previousThread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("num:" + i);
    }

    public static void main(String[] args) throws InterruptedException {
        Thread previousThread = Thread.currentThread();
        for(int i = 0; i < 10; i++){
            ThreadJoinTest joinTest = new ThreadJoinTest(previousThread, i);
            previousThread = joinTest;
            joinTest.start();
            joinTest.join();
            System.out.println("join finished:" + i);
        }
    }
}
