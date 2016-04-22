package com.jude.algorithm;

import com.jude.algorithm.sort.*;
import com.jude.algorithm.tree.AbsBinarySearchTree;
import com.jude.algorithm.tree.BalancedBinaryTree;
import com.jude.algorithm.tree.BinarySearchTree;
import com.jude.algorithm.utils.ArrayCreator;
import com.jude.algorithm.utils.SortTimerProxy;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SorterTest {

    public static void main(String[] args) {

        ArrayList<Sorter<Integer>> sortList = new ArrayList<>();
//        三大慢排序
//        sortList.add(SortTimerProxy.obtain(new BubbleSorter<>()));
//        sortList.add(SortTimerProxy.obtain(new InsertionSorter<>()));
//        sortList.add(SortTimerProxy.obtain(new SelectionSorter<>()));
        sortList.add(SortTimerProxy.obtain(new QuickSorter<>()));
        sortList.add(SortTimerProxy.obtain(new ShellSorter<>()));
        sortList.add(SortTimerProxy.obtain(new HeapSorter<>()));
        sortList.add(SortTimerProxy.obtain(new MergeSorter<>()));
        sortList.add(SortTimerProxy.obtain(new BinarySearchTreeSorter<>()));

        testCorrectness(sortList);
//        testSpeed(sortList);
    }


    private static void testCorrectness(ArrayList<Sorter<Integer>> sortList){
        Random random = new Random();
        Integer[] data = ArrayCreator.create(20, i -> random.nextInt(100));
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        for (Sorter<Integer> integerSorter : sortList) {
            executorService.submit(() -> {
                Integer[] newdata = Arrays.copyOf(data, data.length);
                integerSorter.sort(newdata, (o1, o2) -> o1 - o2);
                if (newdata.length<30){
                    for (Integer integer : newdata) {
                        System.out.print(integer+" ");
                    }
                    System.out.println();
                }

            });
        }
    }

    private static void testSpeed(ArrayList<Sorter<Integer>> sortList){
        Random random = new Random();
        Integer[] data = ArrayCreator.create(1000000, i -> random.nextInt(10000000));
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (Sorter<Integer> integerSorter : sortList) {
            executorService.submit(() -> {
                Integer[] newdata = Arrays.copyOf(data, data.length);
                integerSorter.sort(newdata, (o1, o2) -> o1 - o2);
            });
        }
    }
}