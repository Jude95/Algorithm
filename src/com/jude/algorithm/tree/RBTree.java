package com.jude.algorithm.tree;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Mr.Jude on 2016/4/3.
 */
public class RBTree<K,V> extends AbsBinarySearchTree<K,V>{
    private TreeMap<K,V> treeMap;

    public RBTree() {
        treeMap = new TreeMap<>();
    }

    public RBTree(Comparator<K> comparator) {
        treeMap = new TreeMap<>(comparator);
    }

    @Override
    public int size() {
        return treeMap.size();
    }

    @Override
    public V put(K key, V value) {
        return treeMap.put(key,value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        treeMap.putAll(m);
    }

    @Override
    public V get(K key) {
        return treeMap.get(key);
    }

    @Override
    public V remove(K key) {
        return treeMap.remove(key);
    }

    @Override
    public void clear() {
        treeMap.clear();
    }

    @Override
    public boolean containsKey(K key) {
        return treeMap.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        return treeMap.containsValue(value);
    }

    @Override
    public boolean isEmpty() {
        return treeMap.isEmpty();
    }

    @Override
    public Iterator<K> keyIterator() {
        return treeMap.keySet().iterator();
    }

    @Override
    public Iterator<V> valueIterator() {
        return treeMap.values().iterator();
    }

    @Override
    public Iterator<Map.Entry<K, V>> EntryIterator() {
        return treeMap.entrySet().iterator();
    }

    @Override
    public String toString() {
        return "RedBlackTree -- 红黑树";
    }
}
