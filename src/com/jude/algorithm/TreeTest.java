package com.jude.algorithm;

import com.jude.algorithm.tree.AbsBinarySearchTree;
import com.jude.algorithm.tree.BalancedBinaryTree;
import com.jude.algorithm.tree.BinarySearchTree;
import com.jude.algorithm.tree.RBTree;
import com.jude.algorithm.utils.ArrayCreator;

import java.util.*;

/**
 * Created by Mr.Jude on 2016/4/3.
 */
public class TreeTest {
    public static void main(String[] args) {
        ArrayList<AbsBinarySearchTree<Integer,String>> treeList = new ArrayList<>();
        treeList.add(new BinarySearchTree<>());
        treeList.add(new BalancedBinaryTree<>());
        treeList.add(new RBTree<>());
//        testCorrectness(treeList);
        testSpeed(treeList);
    }

    public static void testCorrectness(ArrayList<AbsBinarySearchTree<Integer,String>> treeList){
        HashMap<Integer,String> data = new HashMap<>();
        List<Integer> list = Arrays.asList(ArrayCreator.create(20, i ->i));
        Collections.shuffle(list);
        for (Integer integer : list) {
            data.put(integer,integer+"");
        }
        for (AbsBinarySearchTree<Integer, String> tree : treeList) {

            System.out.println(tree.toString());
            tree.putAll(data);
            System.out.println("原始");
            showTree(tree);

            tree.remove(3);
            System.out.println("删除");
            showTree(tree);

            tree.put(50,"50");
            System.out.println("添加");
            showTree(tree);
        }
    }

    public static void testSpeed(ArrayList<AbsBinarySearchTree<Integer,String>> treeList){
        HashMap<Integer,String> data = new HashMap<>();
        List<Integer> list = Arrays.asList(ArrayCreator.create(20000, i ->i));
        Collections.shuffle(list);
        for (Integer integer : list) {
            data.put(integer,integer+"");
        }
        for (AbsBinarySearchTree<Integer, String> tree : treeList) {
            long time = System.currentTimeMillis();

            tree.putAll(data);
            list.subList(5000, 15000).forEach(tree::get);

            System.out.println(tree.toString()+"耗时"+(System.currentTimeMillis()-time));
        }
    }

    public static void showTree(AbsBinarySearchTree<Integer,String> tree){
        Iterator<String> iterator = tree.valueIterator();
        while (iterator.hasNext()){
            System.out.print(iterator.next()+" ");
        }
        System.out.println();
    }
}
