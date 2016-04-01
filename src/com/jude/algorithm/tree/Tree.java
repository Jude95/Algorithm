package com.jude.algorithm.tree;

/**
 * Created by zhuchenxi on 16/3/31.
 */
public interface Tree<T> {
    void add(T t);
    void remove(T t);
    void clean();
    T[] search(T left,T right);
}
