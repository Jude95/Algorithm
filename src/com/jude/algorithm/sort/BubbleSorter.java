package com.jude.algorithm.sort;

import java.util.Comparator;

/**
 * 冒泡排序
 * Created by Mr.Jude on 2016/3/24.
 */
public class BubbleSorter<T> implements Sorter<T> {

    @Override
    public synchronized void sort(T[] array, Comparator<T> comparator) {
        if (array.length<=1)return;
        for (int i = 0; i < array.length-1; i++) {
            for (int i1 = 0; i1 < array.length-1; i1++) {
                if (comparator.compare(array[i1],array[i1+1])>0){
                    T temp = array[i1];
                    array[i1] = array[i1+1];
                    array[i1+1] = temp;
                }
            }
        }
    }

    @Override
    public void reverse(T[] array, Comparator<T> comparator) {
        sort(array,comparator.reversed());
    }

    @Override
    public String toString() {
        return "BubbleSorter -- 冒泡排序";
    }
}
