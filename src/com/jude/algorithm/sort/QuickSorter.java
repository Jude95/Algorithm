package com.jude.algorithm.sort;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by zhuchenxi on 16/3/28.
 */
public class QuickSorter<T> implements Sorter<T>{

    @Override
    public synchronized void sort(T[] array, Comparator<T> comparator) {
        adjust(array,comparator, 0, array.length-1);
    }

    private int adjust(T[] array, Comparator<T> comparator , int start , int end){

        T base = array[start];
        int left=start,right=end;
        while (left != right){
            while (left != right){
                if (comparator.compare(array[right],base)<0){
                    array[left] = array[right];
                    left++;
                    break;
                }
                right--;
            }
            while (left != right){
                if (comparator.compare(array[left],base)>0){
                    array[right] = array[left];
                    right--;
                    break;
                }
                left++;
            }
        }
        array[left]=base;
        if (left>start+1)
            adjust(array, comparator, start,left-1);
        if (left<end-1)
            adjust(array, comparator, left+1, end);
        return left;
    }


    @Override
    public synchronized void reverse(T[] array, Comparator<T> comparator) {
        sort(array,comparator.reversed());
    }
    @Override
    public String toString() {
        return "QuickSort -- 快速排序";
    }
}
