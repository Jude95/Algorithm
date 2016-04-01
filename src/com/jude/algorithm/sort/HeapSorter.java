package com.jude.algorithm.sort;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by zhuchenxi on 16/3/28.
 */
public class HeapSorter<T> implements Sorter<T>{
    private T[] array;
    private Comparator<T> comparator;
    int size;

    @Override
    public synchronized void sort(T[] array, Comparator<T> comparator) {
        this.array = array;
        this.comparator = comparator;
        this.size=array.length;
        arrangeArray();
        overrideSortedArray();
    }

    private void overrideSortedArray(){
        T[] t = Arrays.copyOf(array,array.length);
        for (int i = 0; i < t.length; i++) {
            t[i] =getAndArrangeNext();
        }
        System.arraycopy(t, 0, array, 0, t.length);
    }

    private T getAndArrangeNext(){
        T top = array[0];
        array[0] = array[--size];
        arrangeNode(0);
        return top;
    }

    private void arrangeArray(){
        for (int i = size/2; i >= 0; i--) {
            arrangeNode(i);
        }
    }

    private void arrangeNode(int point){
        int left = point*2+1;
        if (left>=size)return;
        int right = left+1;
        int smallChild;
        if (right>=size){
            smallChild = left;
        }else {
            smallChild = (comparator.compare(array[left],array[right])<0)?left:right;
        }
        if (comparator.compare(array[point],array[smallChild])>0){
            T temp = array[smallChild];
            array[smallChild] = array[point];
            array[point] = temp;
            arrangeNode(smallChild);
        }
    }


    @Override
    public void reverse(T[] array, Comparator<T> comparator) {
        sort(array,comparator.reversed());
    }


    @Override
    public String toString() {
        return "HeapSorter -- 堆排序";
    }
}
