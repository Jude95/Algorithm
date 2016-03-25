package com.jude.algorithm.utils;

import java.lang.reflect.Array;

/**
 * Created by Mr.Jude on 2016/3/24.
 */
public class ArrayCreator {
    public interface Creator<T>{
        T create(int index);
    }
    public static <T> T[] create(int count,Creator<T> creator){
        T[] ts = (T[]) Array.newInstance(creator.create(0).getClass(),count);
        for (int i = 0; i < count; i++) {
            ts[i] = creator.create(i);
        }
        return ts;
    }
}
