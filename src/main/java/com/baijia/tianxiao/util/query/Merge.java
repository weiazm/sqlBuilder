package com.baijia.tianxiao.util.query;

/**
 * 对象合并算法接口
 * @title Merge
 * @desc TODO 
 * @author LJX
 * @date 2014年9月17日
 * @version 1.0
 */
public interface Merge<T> {

    void merge(T src, T dest);
}
