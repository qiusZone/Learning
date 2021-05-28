package com.qius.test.sort;

import java.util.Arrays;

/**
 * 参考 https://leetcode-cn.com/problems/sort-an-array/solution/fu-xi-ji-chu-pai-xu-suan-fa-java-by-liweiwei1419/
 * <功能详细描述>
 *
 * @author qiusong
 * @date 2021/5/27.
 * @see [相关类/方法]
 * @since SortTest 1.0
 */
public class SortTest {

    public static void main(String[] args) {
        int[] nums = {5, 2, 3, 1};
        SortTest solution = new SortTest();
//        int[] res = solution.selectionSort(nums);
//        int[] res = solution.insertionSort(nums);
        int[] res = solution.mergeSort(nums);
        System.out.println(Arrays.toString(res));
    }

    /**
     * 归并排序
     *
     * @param nums 待排序数组
     * @return 排序后的数组
     */
    public int[] mergeSort(int[] nums) {
        int len = nums.length;
        // 用于合并两个有序数组的辅助数组 全局使用一份 避免多次创建和销毁
        int[] temp = new int[len];
        mergeSort(nums, 0, len - 1, temp);
        return nums;
    }

    private void mergeSort(int[] nums, int left, int right, int[] temp) {

        if (left >= right) {
            return;
        }

        int mid = (left + right) >>> 1;
        // 分别对中间结点的左右子数组进行归并排序
        mergeSort(nums, left, mid, temp);
        mergeSort(nums, mid + 1, right, temp);
        // 若左右子数组排序后 本身是有序的 则无需排序
        if (nums[mid] <= nums[mid + 1]) {
            return;
        }

        // 合并两个有序数组
//        mergeOfTwoSortedArray(nums, left, mid, right, temp);
        mergeOfTwoSortedArray(nums, left, mid, right);
    }

    private void mergeOfTwoSortedArray(int[] nums, int left, int mid, int right){
        int[] temp = new int[right - left +1];
        // 左指针
        int i = left;
        // 右指针
        int j = mid + 1;
        int k = 0;
        // 将较小的数移动到temp中
        while(i <= mid && j <= right){
            if (nums[i] < nums[j]){
                temp[k++] = nums[i++];
            }else {
                temp[k++] = nums[j++];
            }
        }

        // 当某个子数组遍历完成时
        // 右数组遍历完成 将左数组剩余元素添加到temp
        while (i <= mid){
            temp[k++] = nums[i++];
        }

        // 左数组遍历完成 将右数组剩余元素添加到temp
        while (j <= right){
            temp[k++] = nums[j++];
        }

        // 将temp覆盖原数组
        for(int m = 0; m < temp.length; m++){
            System.out.println(temp[m]);
            nums[m+left] = temp[m];
        }
    }

    private void mergeOfTwoSortedArray(int[] nums, int left, int mid, int right, int[] temp) {

        // 将nums复制到临时数组temp中
        System.arraycopy(nums, left, temp, left, right - left + 1);
        int i = left;
        int j = mid + 1;

        for (int k = left; k <= right; k++) {
            // 左数组已经遍历完成 则后续将右数组依次设置
            if (i == mid + 1) {
                nums[k] = temp[j];
                j++;
            } else if (j == right + 1) {
                // 右数组已经遍历完成 则后续将左数组依次设置
                nums[k] = temp[i];
                i++;
            } else if (temp[i] <= temp[j]) {
                // 注意写成 < 就丢失了稳定性（相同元素原来靠前的排序以后依然靠前）
                nums[k] = temp[i];
                i++;
            } else {
                // temp[i] > temp[j]
                nums[k] = temp[j];
                j++;
            }
        }

    }

    /**
     * 插入排序
     * 将元素插入到已经有序的数组中
     * 时间复杂度 O(N^2)
     * 空间复杂度 O(1)
     * 适合基本有序的排序任务，以及短数组 在小区间内执行排序任务的时候可以转向[插入排序]
     *
     * @param nums 待排序数组
     * @return 排序后的数组
     */
    public int[] insertionSort(int[] nums) {
        int len = nums.length;

        // 将num[i]插入到[0,i)之中 使其成为有序数组
        for (int i = 1; i < len; i++) {
            // 先暂存num[i] 将其与前面的比较 若小于前面的元素 则依次向后移动
            int tmp = nums[i];
            int j = i;
            // 依次将第i个元素跟i-1,i-2...0的元素进行比较 若小于则将当前值向后移动
            while (j > 0 && nums[j - 1] > tmp) {
                nums[j] = nums[j - 1];
                j--;
            }
            // 将num[i]插入到适合的位置
            nums[j] = tmp;
        }

        return nums;
    }

    /**
     * 选择排序
     * 每一轮选择最小值交换到未排定的开头位置
     * 时间复杂度 O(N^2)
     * 空间复杂度 O(1)
     * 优势：适合交换成本较高的排序任务 选择排序最多只需要N次交换
     *
     * @param nums 待排序数组
     * @return 排序后的数组
     */
    public int[] selectionSort(int[] nums) {

        int len = nums.length;
        // 刚开始选择数组第一个元素为未排定的开头位置
        for (int i = 0; i < len - 1; i++) {
            int minIndex = i;
            // 找到未排序之后的数据中的最小值 选择区间[i, len -1]里面最小值的索引，交换到下标i
            for (int j = i + 1; j < len; j++) {
                if (nums[j] < nums[minIndex]) {
                    minIndex = j;
                }
            }
            swap(nums, i, minIndex);
        }

        return nums;
    }


    private void swap(int[] nums, int i, int minIndex) {
        int tmp = nums[i];
        nums[i] = nums[minIndex];
        nums[minIndex] = tmp;
    }
}
