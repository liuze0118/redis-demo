package com.lz.redis.demo.chatgpt;

import java.util.Arrays;

/**
 * @author : liuze
 * @date: 2023/7/17 13:24
 **/
public class SortDemo {
    //冒泡排序
    public static void bubbleSort(int[] arr) {
        int n = arr.length;

        // 进行 n-1 轮冒泡
        for (int i = 0; i < n - 1; i++) {
            // 在每一轮中，比较相邻的元素并交换位置
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // 交换 arr[j] 和 arr[j+1] 的位置
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {5, 2, 8, 12, 1, 6, 3};
        System.out.println("排序前：" + Arrays.toString(arr));
        bubbleSort(arr);
        System.out.println("排序后：" + Arrays.toString(arr));
    }
}
