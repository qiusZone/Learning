package com.qius.test;

/**
 * <一句话功能描述>
 * <功能详细描述>
 *
 * @author qiusong
 * @date 2021/5/26.
 * @see [相关类/方法]
 * @since ThreadTest 1.0
 */
public class ThreadTest {

    public static void main(String[] args) {
        Thread loop = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程开始执行");
                while (true){
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    if(Thread.interrupted()){
//                        System.out.println("线程已被终止，跳出loop");
//                        System.out.println("当前线程中断状态：" + Thread.interrupted());
//                        break;
//                    }
                }
            }
        });
        System.out.println("执行前时间：" + System.currentTimeMillis());
        loop.start();

        loop.interrupt();
        System.out.println("线程已中断");
    }
}
