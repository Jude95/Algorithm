package com.jude.algorithm.sort;

import java.util.Comparator;

/**
 * Created by Mr.Jude on 2016/3/24.
 */
public interface Sorter<T> {
    /**
     * Sort the Array from small to big.
     * @param array the array to sort
     * @param comparator A comparison function
     * @return
     */
    void sort(T[] array, Comparator<T> comparator);
    void reverse(T[] array, Comparator<T> comparator);
}
