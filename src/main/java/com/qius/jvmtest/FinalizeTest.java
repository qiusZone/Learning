package com.qius.jvmtest;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <一句话功能描述>
 * <功能详细描述>
 *
 * @author qiusong
 * @date 2021/7/15.
 * @see [相关类/方法]
 * @since FinalizeTest 1.0
 */
@Slf4j
public class FinalizeTest {

    // 记录存活的对象
    static AtomicInteger aliveCount = new AtomicInteger(0);

    FinalizeTest() {
        aliveCount.incrementAndGet();
    }

    public static void main(String[] args) {

        for (int i = 0; i < 100_0000; i++) {
            FinalizeTest finalizeTest = new FinalizeTest();
            if ((i % 100_000) == 0) {
                System.out.format("After creating %d objects, %d are still alive.%n", i, FinalizeTest.aliveCount.get());
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        FinalizeTest.aliveCount.decrementAndGet();
    }
}
