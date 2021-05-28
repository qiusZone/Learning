package com.qius.test;

import cn.hutool.core.lang.Console;

import java.io.UnsupportedEncodingException;

/**
 * <一句话功能描述>
 * <功能详细描述>
 *
 * @author qiusong
 * @date 2021/4/15.
 * @see [相关类/方法]
 * @since MainTest 1.0
 */
public class MainTest {

    public static void main(String[] args) throws UnsupportedEncodingException {
        final int i = calculateSize(8);
        Console.log(i);
    }

    private static int calculateSize(int numElements) {
        int initialCapacity = 8;
        // Find the best power of two to hold elements.
        // Tests "<=" because arrays aren't kept full.
        if (numElements >= initialCapacity) {
            initialCapacity = numElements;
            Console.log(initialCapacity >>> 1);
            initialCapacity |= (initialCapacity >>>  1);
            initialCapacity |= (initialCapacity >>>  2);
            initialCapacity |= (initialCapacity >>>  4);
            initialCapacity |= (initialCapacity >>>  8);
            initialCapacity |= (initialCapacity >>> 16);
            initialCapacity++;

            if (initialCapacity < 0)   // Too many elements, must back off
                initialCapacity >>>= 1;// Good luck allocating 2 ^ 30 elements
        }
        return initialCapacity;
    }
}
