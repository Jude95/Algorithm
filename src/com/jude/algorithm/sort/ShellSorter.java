package com.jude.algorithm.sort;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * Created by zhuchenxi on 16/3/28.
 */
public class ShellSorter<T> implements Sorter<T> {
    @Override
    public synchronized void sort(T[] array, Comparator<T> comparator) {
        run(array,comparator,array.length/2);
    }

    void run(T[] array, Comparator<T> comparator,int step){
        for (int offset = 0; offset < step; offset++) {
            for (int i = offset; i < array.length-step; i+=step) {
                for (int j = i+step; j >= offset+step; j-=step) {
                    if (comparator.compare(array[j-step],array[j])>0){
                        T temp = array[j-step];
                        array[j-step] = array[j];
                        array[j] = temp;
                    }else break;
                }
            }
        }
        if (step>1){
            run(array, comparator, step/2);
        }
    }

    @Override
    public synchronized void reverse(T[] array, Comparator<T> comparator) {
        sort(array,comparator.reversed());
    }


    @Override
    public String toString() {
        return "ShellSort -- 希尔排序";
    }
}
