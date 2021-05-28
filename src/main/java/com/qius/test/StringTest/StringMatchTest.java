package com.qius.test.StringTest;

/**
 * 字符串匹配
 * <功能详细描述>
 *
 * @author qiusong
 * @date 2021/4/21.
 * @see [相关类/方法]
 * @since StringMatchTest 1.0
 */
public class StringMatchTest {

    public static void main(String[] args) {
        System.out.println(testBruteForce2("BBC ABCDAB ABCDABCDABDE", "ABCDABD"));
    }

    /**
     * BF 暴力破解
     *
     * @param original 原始串
     * @param pattern  匹配串
     */
    private static int testBruteForce1(String original, String pattern) {

        int originalLength = original.length();
        int patternLength = pattern.length();

        for (int i = 0; i <= originalLength - patternLength; i++) {
            for (int j = 0; j < patternLength; j++) {
                // 匹配失败 则跳转到i的下一个点
                if (original.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
                // pattern全部匹配成功 返回i的位置
                if (j == patternLength - 1) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * 暴力匹配
     * @param original
     * @param pattern
     * @return
     */
    private static int testBruteForce2(String original, String pattern) {

        int oLens = original.length();
        int pLens = pattern.length();

        int i = 0;
        int j = 0;
        while (i < oLens && j < pLens) {
            // 依次向下匹配
            if (original.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            } else {
                // 匹配失败时，重新向后移动一位进行匹配
                i = i - j + 1;
                j = 0;
            }
        }

        if (j == pLens){
            return (i - j);
        }else {
            return -1;
        }

    }
}
