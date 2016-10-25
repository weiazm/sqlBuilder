package com.baijia.tianxiao.util.query;

import com.baijia.tianxiao.sqlbuilder.util.SplitMergeUtils;
import java.util.Collection;

/**
 * 为了避免单个查询sql过长，抽象出通过IN进行批量分片查询的逻辑，通过模板化，简化DAO开发批量查询的工作，泛型Q代表查询对象，泛型R代表返回结果对象
 * 
 * @title BatchQueryTemplate
 * @desc TODO
 * @author LJX
 * @date 2014年9月17日
 * @version 1.0
 */

public abstract class BatchQueryTemplate<Q, R> {

    private int size = 500;

    /**
     * 代替DAO实现分片逻辑，使用默认的返回对象创建逻辑和查询结果合并逻辑
     * 
     * @param querySet 批量查询的参数集合，允许所有Collection类型的集合，推荐使用Set，该对象会传递给回调方法实现批量查询逻辑
     * @param callback 实现批量查询逻辑的回调方法，通常使用内部匿名类实现即可，允许调用外部的final对象
     * @return 批量查询结果总集合
     */
    public R batchQuery(Collection<Q> querySet, BatchQueryCallback<Q, R> callback) {
        return batchQuery(querySet, callback, getDefCreator(), getDefMerge());
    }

    /**
     * 代替DAO实现分片逻辑
     * 
     * @param querySet 批量查询的参数集合，允许所有Collection类型的集合，推荐使用Set，该对象会传递给回掉方法实现批量查询逻辑
     * @param callback 实现批量查询逻辑的回调方法，通常使用内部匿名类实现即可，内部匿名类允许调用外部的final对象
     * @param creator 批量查询结果集创建对象（理论上允许为非集合对象），提供灵活的扩展性
     * @param merge 分片查询结果的合并算法，提供灵活的扩展性
     * @return 批量查询结果总集合
     */
    public R batchQuery(Collection<Q> querySet, BatchQueryCallback<Q, R> callback, Creator<R> creator, Merge<R> merge) {
        if (querySet == null || querySet.isEmpty()) {
            return creator.create();
        }
        Collection<Collection<Q>> splitSet = SplitMergeUtils.splitCollection(querySet, size);
        R result = creator.create();
        for (Collection<Q> split : splitSet) {
            merge.merge(callback.doQuery(split), result);
        }
        return result;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * 返回默认的批量查询结果集创建对象
     * 
     * @return
     */
    abstract protected Creator<R> getDefCreator();

    /**
     * 返回默认的分片查询结果的合并算法
     * 
     * @return
     */
    abstract protected Merge<R> getDefMerge();
}
