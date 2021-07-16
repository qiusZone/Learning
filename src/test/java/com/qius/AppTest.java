package com.qius;

import org.junit.Test;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    /**
     * 测试 WeakReference
     */
    @Test
    public void weakReference() {
        Object referent = new Object();
        WeakReference weakReference = new WeakReference(referent);
        assertSame(referent, weakReference.get());
        /*
         * 一旦没有指向 referent 的强引用, weak reference 在 GC 后会被自动回收
         * */
        referent = null;
        System.gc();
        assertNull(weakReference.get());
    }

    /**
     * 测试 WeakHashMap
     *
     * @throws InterruptedException
     */
    @Test
    public void weakHashMap() throws InterruptedException {
        Map weakHashMap = new WeakHashMap();
        Object key = new Object();
        Object value = new Object();
        weakHashMap.put(key, value);
        assertTrue(weakHashMap.containsValue(value));
        key = null;
        // 一旦没有指向 key 的强引用, WeakHashMap 在 GC 后将自动删除相关的 entry
        System.gc();
        // WeakHashMap是通过将已经没有强引用的key加入到定义的ReferenceQueue中，在下一次getTable中调用时清理
        Thread.sleep(1000);
        assertFalse(weakHashMap.containsValue(value));
    }

    /**
     * 测试 phantomReference
     * Phantom Reference(幽灵引用) 与 WeakReference 和 SoftReference 有很大的不同, 因为它的 get()方法永远返回 null, 这也正是它名字的由来
     * PhantomReference 唯一的用处就是跟踪 referent 何时被加入到 ReferenceQueue 中
     * 其一，它可以让我们准确地知道对象何时被从内存中删除， 这个特性可以被用于一些特殊的需求中，清理工作
     * 其二，它可以避免 finalization 带来的一些根本性问题, 上文提到 PhantomReference 的唯一作用就是跟踪 referent 何时被 enqueue 到 ReferenceQueue 中,
     * 但是 WeakReference 也有对应的功能, 两者的区别到底在哪呢 ?
     * 这就要说到 Object 的 finalize 方法, 此方法将在 gc 执行前被调用, 如果某个对象重载了 finalize 方法并故意在方法内创建本身的强引用,
     * 这将导致这一轮的 GC 无法回收这个对象并有可能引起任意次 GC， 最后的结果就是明明 JVM 内有很多 Garbage 却 OutOfMemory，
     * 使用 PhantomReference 就可以避免这个问题， 因为 PhantomReference 是在 finalize 方法执行后回收的，也就意味着此时已经不可能拿到原来的引用,
     * 也就不会出现上述问题
     */
    @Test
    public void phantomReferenceAlwaysNull() {
        Object referent = new Object();
        PhantomReference phantomReference = new PhantomReference(referent, new ReferenceQueue()); /** * phantom reference 的 get 方法永远返回 null */
        assertNull(phantomReference.get());
    }


}
