package com.jude.algorithm.sort;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by zhuchenxi on 16/3/28.
 */
public class MergeSorter<T> implements Sorter<T> {
    Comparator<T> comparator;

    @Override
    public synchronized void sort(T[] array, Comparator<T> comparator) {
        this.comparator = comparator;
        T[] temp = Arrays.copyOf(array,array.length);
        internalMergeSort(array,temp,0,temp.length-1);
    }

    private void internalMergeSort(T[] a, T[] b, int left, int right){
        //当left==right的时，已经不需要再划分了
        if (left<right){
            int middle = (left+right)/2;
            internalMergeSort(a, b, left, middle);          //左子数组
            internalMergeSort(a, b, middle+1, right);       //右子数组
            mergeSortedArray(a, b, left, middle, right);    //合并两个子数组
        }
    }
    // 合并两个有序子序列 arr[left, ..., middle] 和 arr[middle+1, ..., right]。temp是辅助数组。
    private void mergeSortedArray(T arr[], T temp[], int left, int middle, int right){
        int i=left;
        int j=middle+1;
        int k=0;
        while ( i<=middle && j<=right){
            if (comparator.compare(arr[i],arr[j])<=0){
                temp[k++] = arr[i++];
            }
            else{
                temp[k++] = arr[j++];
            }
        }
        while (i <=middle){
            temp[k++] = arr[i++];
        }
        while ( j<=right){
            temp[k++] = arr[j++];
        }
        //把数据复制回原数组
        for (i=0; i<k; ++i){
            arr[left+i] = temp[i];
        }
    }

    @Override
    public void reverse(T[] array, Comparator<T> comparator) {

    }
    @Override
    public String toString() {
        return "MergeSorter -- 归并排序";
    }
}
