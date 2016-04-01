package com.jude.algorithm.sort;

import java.util.Comparator;

/**
 * 选择排序
 * Created by Mr.Jude on 2016/3/24.
 */
public class SelectionSorter<T> implements Sorter<T> {
    @Override
    public synchronized void sort(T[] array, Comparator<T> comparator) {
        if (array.length<=1)return;
        for (int i = 0; i < array.length-1; i++) {
            int extPos = i;//极值
            for (int i1 = i+1; i1 < array.length; i1++) {
                if(comparator.compare(array[i1],array[extPos])<0){
                    extPos = i1;
                }
            }
            T temp = array[i];
            array[i] = array[extPos];
            array[extPos] = temp;
        }
    }

    @Override
    public synchronized void reverse(T[] array, Comparator<T> comparator) {
        sort(array,comparator.reversed());
    }

    @Override
    public String toString() {
        return "SelectionSort -- 选择排序";
    }
}
