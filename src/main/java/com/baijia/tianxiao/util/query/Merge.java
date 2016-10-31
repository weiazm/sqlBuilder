package com.baijia.tianxiao.util.query;

/**
 * 对象合并算法接口
 *
 * @author LJX
 * @version 1.0
 * @title Merge
 * @desc TODO
 * @date 2014年9月17日
 */
public interface Merge<T> {

    void merge(T src, T dest);
}
