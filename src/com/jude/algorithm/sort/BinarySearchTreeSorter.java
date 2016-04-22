package com.jude.algorithm.sort;

import com.jude.algorithm.tree.BinarySearchTree;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by Mr.Jude on 2016/4/3.
 */
public class BinarySearchTreeSorter<T> implements Sorter<T> {
    @Override
    public void sort(T[] array, Comparator<T> comparator) {
        BinarySearchTree<T,Object> tree = new BinarySearchTree<>(comparator);
        for (T t : array) {
            tree.put(t,null);
        }
        int index = 0;
        Iterator<T> iterator = tree.keyIterator();
        while (iterator.hasNext()){
            array[index++] = iterator.next();
        }
    }

    @Override
    public void reverse(T[] array, Comparator<T> comparator) {
        sort(array,comparator.reversed());
    }

    @Override
    public String toString() {
        return "BinarySearchTreeSorter -- 二叉查找树排序";
    }
}
