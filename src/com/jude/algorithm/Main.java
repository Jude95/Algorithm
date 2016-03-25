package com.jude.algorithm;

import com.jude.algorithm.sort.BubbleSorter;
import com.jude.algorithm.sort.InsertionSort;
import com.jude.algorithm.sort.SelectionSort;
import com.jude.algorithm.sort.Sorter;
import com.jude.algorithm.utils.ArrayCreator;
import com.jude.algorithm.utils.SortTimerProxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Main {

    public static void main(String[] args) {
        Integer[] data = ArrayCreator.create(50000, i->i);
        ArrayList<Sorter<Integer>> sortList = new ArrayList<>();
        sortList.add(SortTimerProxy.obtain(new BubbleSorter<>()));
        sortList.add(SortTimerProxy.obtain(new InsertionSort<>()));
        sortList.add(SortTimerProxy.obtain(new SelectionSort<>()));
        for (Sorter<Integer> integerSorter : sortList) {
            Integer[] newdata = Arrays.copyOf(data,data.length);
            integerSorter.sort(newdata, (o1, o2) -> o1-o2);
        }
    }
}
