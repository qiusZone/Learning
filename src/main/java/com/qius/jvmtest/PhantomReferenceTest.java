package com.qius.jvmtest;

import lombok.extern.slf4j.Slf4j;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * 虚引用
 * <功能详细描述>
 *
 * @author qiusong
 * @date 2021/7/14.
 * @see [相关类/方法]
 * @since PhantomReferenceTest 1.0
 */
@Slf4j
public class PhantomReferenceTest {

    public static void main(String[] args) throws InterruptedException {
        Object obj = new Object();
        ReferenceQueue<Object> refQueue = new ReferenceQueue<Object>();
        PhantomReference<Object> phantom = new PhantomReference<Object>(obj,
                refQueue);
        // 虚引用 phantom.get() 在任何情况返回都是null
        log.info("gc前phantom:{}", phantom.get());
        log.info("gc前referenceQueue:{}", refQueue.poll());

        obj = null;
        System.gc();

        log.info("gc后phantom:{}", phantom.get());
        log.info("gc后referenceQueue:{}", refQueue.poll());

    }

}
