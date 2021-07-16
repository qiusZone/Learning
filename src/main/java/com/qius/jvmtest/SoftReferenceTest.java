package com.qius.jvmtest;

import java.lang.ref.SoftReference;

/**
 * 软引用
 * <功能详细描述>
 *
 * @author qiusong
 * @date 2021/7/14.
 * @see [相关类/方法]
 * @since SoftReferenceTest 1.0
 */
public class SoftReferenceTest {

    public static void main(String[] args) throws InterruptedException {
        //100M的缓存数据
        byte[] cacheData = new byte[100 * 1024 * 1024];
        //将缓存数据用软引用持有
        SoftReference<byte[]> cacheRef = new SoftReference<>(cacheData);
        //将缓存数据的强引用去除
        cacheData = null;
        System.out.println("GC前: " + cacheData);
        System.out.println("GC前: " + cacheRef.get());
        //进行一次GC后查看对象的回收情况
        System.gc();
        //等待GC 此时内存未满 不会回收
        Thread.sleep(500);
        System.out.println("GC后: " + cacheData);
        System.out.println("GC后: " + cacheRef.get());

        //在分配一个120M的对象，看看缓存对象的回收情况
        byte[] newData = new byte[120 * 1024 * 1024];
        System.out.println("分配后" + cacheData);
        System.out.println("分配后" + cacheRef.get());
    }
}
