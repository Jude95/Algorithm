package com.jude.algorithm.sort;

import java.util.Comparator;

/**
 * 插入排序
 * Created by Mr.Jude on 2016/3/24.
 */
public class InsertionSorter<T> implements Sorter<T> {
    @Override
    public synchronized void sort(T[] array, Comparator<T> comparator) {
        if (array.length<=1)return;
        for (int i = 0; i < array.length-1; i++) {
            for (int j = i+1; j >= 1; j--) {
                if (comparator.compare(array[j-1],array[j])>0){
                    T temp = array[j-1];
                    array[j-1] = array[j];
                    array[j] = temp;
                }else break;
            }
        }
    }

    @Override
    public synchronized void reverse(T[] array, Comparator<T> comparator) {
        sort(array,comparator.reversed());
    }

    @Override
    public String toString() {
        return "InsertionSort -- 插入排序";
    }
}
