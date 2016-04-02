package com.jude.algorithm.tree;


import java.util.*;
import java.util.function.Consumer;

/**
 * Created by zhuchenxi on 16/3/31.
 */
public class BinarySearchTree<K,V> extends AbsBinarySearchTree<K,V>{
    private Entry<K,V> root;
    private Comparator<K> comparator;
    private int size;
    private int modCount;
    public BinarySearchTree() {
    }
    public BinarySearchTree(Comparator<K> comparator) {
        this.comparator = comparator;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root==null;
    }

    @Override
    public boolean containsValue(V value) {
        return false;
    }

    @Override
    public boolean containsKey(K key) {
        return getEntry(key)==null;
    }

    @Override
    public V get(K key) {
        Entry<K,V> data = getEntry(key);
        if (data!=null) return data.value;
        else return null;
    }

    @Override
    public V put(K key, V value) {
        modCount++;
        if(root==null){
            root = new Entry<>(key,value,null);
            size++;
            return null;
        }
        Entry<K,V> t = root;
        int cmp;
        Entry<K,V> parent;
        do {
            parent = t;
            cmp = compare(key, t.key);
            if (cmp < 0)
                t = t.left;
            else if (cmp > 0)
                t = t.right;
            else
                return t.setValue(value);
        } while (t != null);
        Entry<K,V> e = new Entry<>(key, value, parent);
        if (cmp < 0)
            parent.left = e;
        else
            parent.right = e;
        size++;
        return null;
    }

    @Override
    public V remove(K key) {
        modCount++;
        Entry<K,V> entry = getEntry(key);
        if (entry==null)return null;
        if (entry.left==null&&entry.right==null){
            if (entry == entry.parent.left){
                entry.parent.left=null;
            }else {
                entry.parent.right=null;
            }
        }else if (entry.right==null){
            if (entry == entry.parent.left){
                entry.parent.left=entry.left;
            }else {
                entry.parent.right=entry.left;
            }
        }else if (entry.left==null){
            if (entry == entry.parent.left){
                entry.parent.left=entry.right;
            }else {
                entry.parent.right=entry.right;
            }
        }else{
            Entry<K,V> t = entry.right;
            while (t.left != null){
                t = t.left;
            }
            t.parent.left = t.right;
            if (entry == entry.parent.left){
                entry.parent.left=t;
            }else {
                entry.parent.right=t;
            }
            t.left = entry.left;
            t.right = entry.right;
        }
        size--;
        return entry.value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(),entry.getValue());
        }
    }

    @Override
    public void clear() {
        modCount++;
        root = null;
        size=0;
    }


    final Entry<K,V> getEntry(K key) {
        if (key == null) throw new NullPointerException();
        Entry<K,V> p = root;
        while (p != null) {
            int cmp = compare(key, p.key);
            if (cmp < 0)
                p = p.left;
            else if (cmp > 0)
                p = p.right;
            else
                return p;
        }
        return null;
    }

    /**
     * Base class for TreeMap Iterators
     */
     class EntryIterator<T> implements Iterator<T> {
        Entry<K,V> next;
        Entry<K,V> lastReturned;
        int expectedModCount;

        EntryIterator(Entry<K,V> first) {
            expectedModCount = modCount;
            lastReturned = null;
            next = first;
        }

        public final boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            return null;
        }

        final Entry<K,V> nextEntry() {
            Entry<K,V> e = next;
            if (e == null)
                throw new NoSuchElementException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            next = successor(e);
            lastReturned = e;
            return e;
        }

        final Entry<K,V> prevEntry() {
            Entry<K,V> e = next;
            if (e == null)
                throw new NoSuchElementException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            next = predecessor(e);
            lastReturned = e;
            return e;
        }

        public void remove() {
            if (lastReturned == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            // deleted entries are replaced by their successors
            if (lastReturned.left != null && lastReturned.right != null)
                next = lastReturned;
            deleteEntry(lastReturned);
            expectedModCount = modCount;
            lastReturned = null;
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {

        }
    }

    static final class Entry<K,V> implements Map.Entry<K,V> {
        K key;
        V value;
        Entry<K,V> left;
        Entry<K,V> right;
        Entry<K,V> parent;

        /**
         * Make a new cell with given key, value, and parent, and with
         * {@code null} child links, and BLACK color.
         */
        Entry(K key, V value, Entry<K,V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        /**
         * Returns the key.
         *
         * @return the key
         */
        public K getKey() {
            return key;
        }

        /**
         * Returns the value associated with the key.
         *
         * @return the value associated with the key
         */
        public V getValue() {
            return value;
        }

        /**
         * Replaces the value currently associated with the key with the given
         * value.
         *
         * @return the value associated with the key before this method was
         *         called
         */
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<?,?> e = (Map.Entry<?,?>)o;
            return valEquals(key,e.getKey()) && valEquals(value,e.getValue());
        }

        public int hashCode() {
            int keyHash = (key==null ? 0 : key.hashCode());
            int valueHash = (value==null ? 0 : value.hashCode());
            return keyHash ^ valueHash;
        }

        public String toString() {
            return key + "=" + value;
        }
    }

    /**
     * Compares two keys using the correct comparison method for this TreeMap.
     */
    @SuppressWarnings("unchecked")
    final int compare(Object k1, Object k2) {
        return comparator==null ? ((Comparable<? super K>)k1).compareTo((K)k2)
                : comparator.compare((K)k1, (K)k2);
    }

    /**
     * Test two values for equality.  Differs from o1.equals(o2) only in
     * that it copes with {@code null} o1 properly.
     */
    static final boolean valEquals(Object o1, Object o2) {
        return (o1==null ? o2==null : o1.equals(o2));
    }

}
