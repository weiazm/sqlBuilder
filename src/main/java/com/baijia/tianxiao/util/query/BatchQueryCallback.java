package com.baijia.tianxiao.util.query;

import java.util.Collection;

/**
 * 定义批量查询逻辑回调方法的接口，通常使用内部匿名类实现即可，内部匿名类允许调用外部的final对象，泛型Q为查询对象（一般与IN对应），泛型R为返回结果对象
 * @title BatchQueryCallback
 * @desc TODO 
 * @author LJX
 * @date 2014年9月17日
 * @version 1.0
 */
public interface BatchQueryCallback<Q, R> {

    /**
     * 批量查询逻辑
     * @param querySet  查询对象集合（一般与IN对应）
     * @return          查询结果集合
     */
    R doQuery(Collection<Q> querySet);
}
