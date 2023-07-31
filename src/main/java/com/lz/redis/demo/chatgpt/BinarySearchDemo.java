package com.lz.redis.demo.chatgpt;

/**
 * @author : liuze
 * @date: 2023/7/17 13:19
 * @desc: 二分查找法demo
 **/
public class BinarySearchDemo {
    public static int binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Number
            if (arr[mid] == target) {
                return mid; // 找到目标值，返回索引
            } else if (arr[mid] < target) {
                left = mid + 1; // 目标值在右半部分，更新左边界
            } else {
                right = mid - 1; // 目标值在左半部分，更新右边界
            }
        }

        return -1; // 目标值不存在，返回-1
    }

    public static void main(String[] args) {
        int[] arr = {1, 3, 5, 7, 9, 11, 13, 15};
        int target = 7;
        int index = binarySearch(arr, target);

        if (index != -1) {
            System.out.println("目标值 " + target + " 在索引 " + index + " 处找到。");
        } else {
            System.out.println("目标值 " + target + " 不存在于数组中。");
        }
    }
}
