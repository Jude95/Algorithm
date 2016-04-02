package com.jude.algorithm.tree;

import java.util.AbstractMap;
import java.util.Map;

/**
 * Created by Mr.Jude on 2016/4/2.
 */
public abstract class AbsBinarySearchTree<K,V>{
    public abstract int size();
    public abstract V put(K key,V value);
    public abstract void putAll(Map<? extends K, ? extends V> m);
    public abstract V get(K key);
    public abstract V remove(K key);
    public abstract void clear();
    public abstract boolean containsKey(K key);
    public abstract boolean containsValue(V value);
    public abstract boolean isEmpty();
}
