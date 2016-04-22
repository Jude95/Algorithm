package com.jude.algorithm.tree;

import java.util.*;

/**
 * Created by Mr.Jude on 2016/4/3.
 */
public class BalancedBinaryTree<K,V> extends AbsBinarySearchTree<K,V> {
    private static final int LH = 1;    //左高
    private static final int EH = 0;    //等高
    private static final int RH = -1;   //右高

    private Entry<K,V> root;
    private Comparator<K> comparator;
    private int size;
    private int modCount;
    public BalancedBinaryTree() {
    }
    public BalancedBinaryTree(Comparator<K> comparator) {
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
    public Iterator<K> keyIterator() {
        return new KeyIterator(getFirstEntry());
    }

    @Override
    public Iterator<V> valueIterator() {
        return new ValueIterator(getFirstEntry());
    }

    @Override
    public Iterator<Map.Entry<K, V>> EntryIterator() {
        return new EntryIterator(getFirstEntry());
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

        //自下向上回溯，查找最近不平衡节点
        while(parent!=null){
            cmp = compare(e.key,parent.key);
            if(cmp < 0){    //插入节点在parent的左子树中
                parent.balance++;
            }else{           //插入节点在parent的右子树中
                parent.balance--;
            }
            if(parent.balance == 0){    //此节点的balance为0，不再向上调整BF值，且不需要旋转
                break;
            }
            if(Math.abs(parent.balance) == 2){  //找到最小不平衡子树根节点
                fixAfterInsertion(parent);
                break;                  //不用继续向上回溯
            }
            parent = parent.parent;
        }
        size++;
        return null;
    }

    @Override
    public V remove(K key) {
        Entry<K,V> p = getEntry(key);
        if (p == null)return null;
        modCount++;
        size--;
        //如果p左右子树都不为空，找到其直接后继，替换p，之后p指向s，删除p其实是删除s
        //所有的删除左右子树不为空的节点都可以调整为删除左右子树有其一不为空，或都为空的情况。
        if (p.left != null && p.right != null) {

            Entry<K,V> s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        }
        Entry<K,V> replacement = (p.left != null ? p.left : p.right);
        if (replacement != null){

            replacement.parent = p.parent;
            if (p.parent == null)   //如果p为root节点
                root = replacement;
            else if (p == p.parent.left)    //如果p是左孩子
                p.parent.left  = replacement;
            else                            //如果p是右孩子
                p.parent.right = replacement;

            p.left = p.right = p.parent = null;     //p的指针清空

            //这里更改了replacement的父节点，所以可以直接从它开始向上回溯
            fixAfterDeletion(replacement);
        }else if (p.parent == null) { // 如果全树只有一个节点
            root = null;
        } else {  //左右子树都为空
            System.out.println("Case3");

            fixAfterDeletion(p);    //这里从p开始回溯
            if (p.parent != null) {
                if (p == p.parent.left)
                    p.parent.left = null;
                else if (p == p.parent.right)
                    p.parent.right = null;
                p.parent = null;
            }
        }


        return p.value;
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
     * 调整的方法：
     * 1.当最小不平衡子树的根(以下简称R)为2时，即左子树高于右子树：
     * 如果R的左子树的根节点的BF为1时，做右旋；
     * 如果R的左子树的根节点的BF为-1时，先左旋然后再右旋
     *
     * 2.R为-2时，即右子树高于左子树：
     * 如果R的右子树的根节点的BF为1时，先右旋后左旋
     * 如果R的右子树的根节点的BF为-1时，做左旋
     *
     * 至于调整之后，各节点的BF变化见代码
     */
    private void fixAfterInsertion(Entry<K,V> p){
        if(p.balance == 2){
            leftBalance(p);
        }
        if(p.balance == -2){
            rightBalance(p);
        }
    }

    /**
     * 删除某节点p后的调整方法：
     * 1.从p开始向上回溯，修改祖先的BF值，这里只要调整从p的父节点到根节点的BF值，
     * 调整原则为，当p位于某祖先节点(简称A)的左子树中时，A的BF减1，当p位于A的
     * 右子树中时A的BF加1。当某个祖先节点BF变为1或-1时停止回溯，这里与插入是相反的，
     * 因为原本这个节点是平衡的，删除它的子树的某个节点并不会改变它的高度
     *
     * 2.检查每个节点的BF值，如果为2或-2需要进行旋转调整，调整方法如下文，
     * 如果调整之后这个最小子树的高度降低了，那么必须继续从这个最小子树的根节点(假设为B)继续
     * 向上回溯，这里和插入不一样，因为B的父节点的平衡性因为其子树B的高度的改变而发生了改变，
     * 那么就可能需要调整，所以删除可能进行多次的调整。
     *
     */
    private void fixAfterDeletion(Entry<K,V> p){
        boolean heightLower = true;     //看最小子树调整后，它的高度是否发生变化，如果减小，继续回溯
        Entry<K,V> t = p.parent;
        int cmp;
        //自下向上回溯，查找不平衡的节点进行调整
        while(t!=null && heightLower){
            cmp = compare(p.key,t.key);
            /**
             * 删除的节点是右子树，等于的话，必然是删除的某个节点的左右子树不为空的情况
             * 例如：     10
             *          /    \
             *         5     15
             *       /   \
             *      3    6
             * 这里删除5，是把6的值赋给5，然后删除6，这里6是p，p的父节点的值也是6。
             * 而这也是右子树的一种
             */
            if(cmp >= 0 ){
                t.balance ++;
            }else{
                t.balance --;
            }
            if(Math.abs(t.balance) == 1){   //父节点经过调整平衡因子后，如果为1或-1，说明调整之前是0，停止回溯。
                break;
            }
            Entry<K,V> r = t;
            //这里的调整跟插入一样
            if(t.balance == 2){
                heightLower = leftBalance(r);
            }else if(t.balance==-2){
                heightLower = rightBalance(r);
            }
            t = t.parent;
        }
    }

    /**
     * @param p 最小旋转子树的根节点
     * 向左旋转之后p移到p的左子树处，p的右子树B变为此最小子树根节点，
     * B的左子树变为p的右子树
     * 比如：     A(-2)                   B(1)
     *              \        左旋转       /   \
     *             B(0)     ---->       A(0)   \
     *             /   \                   \    \
     *           BL(0)  BR(0)              BL(0) BR(0)
     *  旋转之后树的深度之差不超过1
     */
    private void rotateLeft(Entry<K,V> p) {
        if(p!=null){
            Entry<K,V> r = p.right;
            p.right = r.left;   //把p右子树的左节点嫁接到p的右节点，如上图，把BL作为A的右子节点
            if (r.left != null) //如果B的左节点BL不为空，把BL的父节点设为A
                r.left.parent = p;
            r.parent = p.parent;  //A的父节点设为B的父节点
            if (p.parent == null)         //如果p是根节点
                root = r;                 //r变为父节点，即B为父节点
            else if (p.parent.left == p)  //如果p是左子节点
                p.parent.left = r;        //p的父节点的左子树为r
            else                          //如果p是右子节点
                p.parent.right = r;       //p的父节点的右子树为r
            r.left = p;                   //p变为r的左子树，即A为B的左子树
            p.parent = r;                 //同时更改p的父节点为r，即A的父节点为B
        }
    }

    /**
     * @param p 最小旋转子树的根节点
     * 向右旋转之后，p移到p的右子节点处，p的左子树B变为最小旋转子树的根节点
     * B的右子节点变为p的左节点、
     * 例如:       A(2)                     B(-1)
     *            /         右旋转          /    \
     *          B(0)       ------>         /     A(0)
     *         /   \                      /      /
     *       BL(0) BR(0)                BL(0)  BR(0)
     */
    private void rotateRight(Entry<K,V> p){
        if(p!=null){
            Entry<K,V> l = p.left;
            p.left = l.right;    //把B的右节点BR作为A的左节点
            if (l.right != null)   //如果BR不为null，设置BR的父节点为A
                l.right.parent = p;
            l.parent = p.parent;  //A的父节点赋给B的父节点
            if (p.parent == null)   //如果p是根节点
                root = l;          //B为根节点
            else if (p.parent.right == p) //如果A是其父节点的左子节点
                p.parent.right = l;     //B为A的父节点的左子树
            else                        //如果A是其父节点的右子节点
                p.parent.left = l;      //B为A的父节点的右子树
            l.right = p;                //A为B的右子树
            p.parent = l;               //设置A的父节点为B
        }
    }

    /**
     * 做左平衡处理
     * 平衡因子的调整如图：
     *         t                       rd
     *       /   \                   /    \
     *      l    tr   左旋后右旋    l       t
     *    /   \       ------->    /  \    /  \
     *  ll    rd                ll   rdl rdr  tr
     *       /   \
     *     rdl  rdr
     *
     *   情况2(rd的BF为0)
     *
     *
     *         t                       rd
     *       /   \                   /    \
     *      l    tr   左旋后右旋    l       t
     *    /   \       ------->    /  \       \
     *  ll    rd                ll   rdl     tr
     *       /
     *     rdl
     *
     *   情况1(rd的BF为1)
     *
     *
     *         t                       rd
     *       /   \                   /    \
     *      l    tr   左旋后右旋    l       t
     *    /   \       ------->    /       /  \
     *  ll    rd                ll       rdr  tr
     *           \
     *          rdr
     *
     *   情况3(rd的BF为-1)
     *
     *
     *         t                         l
     *       /       右旋处理          /    \
     *      l        ------>          ll     t
     *    /   \                             /
     *   ll   rd                           rd
     *   情况4(L等高)
     */
    private boolean leftBalance(Entry<K,V> t){
        boolean heightLower = true;
        Entry<K,V> l = t.left;
        switch (l.balance) {
            case LH:            //左高，右旋调整,旋转后树的高度减小
                t.balance = l.balance = EH;
                rotateRight(t);
                break;
            case RH:            //右高，分情况调整
                Entry<K,V> rd = l.right;
                switch (rd.balance) {   //调整各个节点的BF
                    case LH:    //情况1
                        t.balance = RH;
                        l.balance = EH;
                        break;
                    case EH:    //情况2
                        t.balance = l.balance = EH;
                        break;
                    case RH:    //情况3
                        t.balance = EH;
                        l.balance = LH;
                        break;
                }
                rd.balance = EH;
                rotateLeft(t.left);
                rotateRight(t);
                break;
            case EH:      //特殊情况4,这种情况在添加时不可能出现，只在移除时可能出现，旋转之后整体树高不变
                l.balance = RH;
                t.balance = LH;
                rotateRight(t);
                heightLower = false;
                break;
        }
        return heightLower;
    }

    /**
     * 做右平衡处理
     * 平衡因子的调整如图：
     *           t                               ld
     *        /     \                          /     \
     *      tl       r       先右旋再左旋     t       r
     *             /   \     -------->      /   \    /  \
     *           ld    rr                 tl   ldl  ldr rr
     *          /  \
     *       ldl  ldr
     *       情况2(ld的BF为0)
     *
     *           t                               ld
     *        /     \                          /     \
     *      tl       r       先右旋再左旋     t       r
     *             /   \     -------->      /   \       \
     *           ld    rr                 tl   ldl      rr
     *          /
     *       ldl
     *       情况1(ld的BF为1)
     *
     *           t                               ld
     *        /     \                          /     \
     *      tl       r       先右旋再左旋     t       r
     *             /   \     -------->      /        /  \
     *           ld    rr                 tl        ldr rr
     *             \
     *             ldr
     *       情况3(ld的BF为-1)
     *
     *           t                                  r
     *             \          左旋                /   \
     *              r        ------->           t      rr
     *            /   \                          \
     *           ld   rr                         ld
     *        情况4(r的BF为0)
     */
    private boolean rightBalance(Entry<K,V> t){
        boolean heightLower = true;
        Entry<K,V> r = t.right;
        switch (r.balance) {
            case LH:            //左高，分情况调整
                Entry<K,V> ld = r.left;
                switch (ld.balance) {   //调整各个节点的BF
                    case LH:    //情况1
                        t.balance = EH;
                        r.balance = RH;
                        break;
                    case EH:    //情况2
                        t.balance = r.balance = EH;
                        break;
                    case RH:    //情况3
                        t.balance = LH;
                        r.balance = EH;
                        break;
                }
                ld.balance = EH;
                rotateRight(t.right);
                rotateLeft(t);
                break;
            case RH:            //右高，左旋调整
                t.balance = r.balance = EH;
                rotateLeft(t);
                break;
            case EH:       //特殊情况4
                r.balance = LH;
                t.balance = RH;
                rotateLeft(t);
                heightLower = false;
                break;
        }
        return heightLower;
    }

    /**
     * Returns the first Entry in the TreeMap (according to the TreeMap's
     * key-sort function).  Returns null if the TreeMap is empty.
     */
    final Entry<K,V> getFirstEntry() {
        Entry<K,V> p = root;
        if (p != null)
            while (p.left != null)
                p = p.left;
        return p;
    }

    /**
     * Returns the last Entry in the TreeMap (according to the TreeMap's
     * key-sort function).  Returns null if the TreeMap is empty.
     */
    final Entry<K,V> getLastEntry() {
        Entry<K,V> p = root;
        if (p != null)
            while (p.right != null)
                p = p.right;
        return p;
    }
    /**
     * Base class for TreeMap Iterators
     */
    abstract class PrivateEntryIterator<T> implements Iterator<T> {
        Entry<K,V> next;
        Entry<K,V> lastReturned;
        int expectedModCount;

        PrivateEntryIterator(Entry<K,V> first) {
            expectedModCount = modCount;
            lastReturned = null;
            next = first;
        }

        public final boolean hasNext() {
            return next != null;
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
            BalancedBinaryTree.this.remove(lastReturned.getKey());
            expectedModCount = modCount;
            lastReturned = null;
        }

    }

    private final class EntryIterator extends PrivateEntryIterator<Map.Entry<K,V>> {
        EntryIterator(Entry<K,V> first) {
            super(first);
        }
        public Map.Entry<K,V> next() {
            return nextEntry();
        }
    }

    private final class ValueIterator extends PrivateEntryIterator<V> {
        ValueIterator(Entry<K,V> first) {
            super(first);
        }
        public V next() {
            return nextEntry().value;
        }
    }

    private final class KeyIterator extends PrivateEntryIterator<K> {
        KeyIterator(Entry<K,V> first) {
            super(first);
        }
        public K next() {
            return nextEntry().key;
        }

    }

    static final class Entry<K,V> implements Map.Entry<K,V> {
        K key;
        V value;
        int balance;
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
     * Returns the successor of the specified Entry, or null if no such.
     */
    static <K,V> Entry<K,V> successor(Entry<K,V> t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            Entry<K,V> p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
            Entry<K,V> p = t.parent;
            Entry<K,V> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    /**
     * Returns the predecessor of the specified Entry, or null if no such.
     */
    static <K,V> Entry<K,V> predecessor(Entry<K,V> t) {
        if (t == null)
            return null;
        else if (t.left != null) {
            Entry<K,V> p = t.left;
            while (p.right != null)
                p = p.right;
            return p;
        } else {
            Entry<K,V> p = t.parent;
            Entry<K,V> ch = t;
            while (p != null && ch == p.left) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }
    /**
     * Compares two keys using the correct comparison method for this TreeMap.
     */
    @SuppressWarnings("unchecked")
    final int compare(K k1, K k2) {
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


    @Override
    public String toString() {
        return "BalancedBinaryTree -- 平衡二叉树";
    }
}
