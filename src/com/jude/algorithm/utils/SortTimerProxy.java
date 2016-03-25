package com.jude.algorithm.utils;

import com.jude.algorithm.sort.Sorter;

import java.util.Comparator;

/**
 * Created by Mr.Jude on 2016/3/24.
 */
public class SortTimerProxy {
    public static <T> Sorter<T> obtain(Sorter<T> sorter){
        return new Sorter<T>() {
            @Override
            public void sort(T[] array, Comparator<T> comparator) {
                long time = System.currentTimeMillis();
                sorter.sort(array,comparator);
                System.out.println(sorter +"耗时"+ (System.currentTimeMillis()-time));
            }

            @Override
            public void reverse(T[] array, Comparator<T> comparator) {
                long time = System.currentTimeMillis();
                sorter.reverse(array,comparator);
                System.out.println(sorter +"耗时"+ (System.currentTimeMillis()-time));
            }
        };
    }
}
