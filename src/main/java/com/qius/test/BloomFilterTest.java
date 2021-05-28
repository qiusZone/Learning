package com.qius.test;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * 布隆过滤器
 * <功能详细描述>
 *
 * @author qiusong
 * @date 2021/5/24.
 * @see [相关类/方法]
 * @since BloomFilterTest 1.0
 */
public class BloomFilterTest {

    public static void main(String[] args) {

        int total = 1000000;
        final BloomFilter<Integer> filter = BloomFilter.create(Funnels.integerFunnel(), total);

        // 初始化数据进行bloomFilter
        for(int i = 0; i < total; i++){
            filter.put(i);
        }

        System.out.println("开始测试~~~");
        int errorCount = 0;
        for (int i = 0; i < total; i++) {
            if (filter.mightContain(i)) {
                errorCount++;
            }
        }
        System.out.println("可能存在数量: " + errorCount);

        // 匹配不在过滤器中的10000个值，有多少匹配出来
        int count = 0;
        for (int i = total; i < total + 10000; i++) {
            if (filter.mightContain(i)) {
                count++;
            }
        }
        System.out.println("误伤的数量：" + count);

    }
}
