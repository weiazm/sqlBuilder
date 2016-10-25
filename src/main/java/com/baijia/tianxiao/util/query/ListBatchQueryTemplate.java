package com.baijia.tianxiao.util.query;

import java.util.LinkedList;
import java.util.List;

/**
 * 实现返回结果为LIST<V>的BatchQueryTemplate
 * @title ListBatchQueryTemplate
 * @desc TODO 
 * @author LJX
 * @date 2014年9月17日
 * @version 1.0
 */
public class ListBatchQueryTemplate<Q, V> extends BatchQueryTemplate<Q, List<V>> {

    private final Creator<List<V>> defCreator = new Creator<List<V>>() {

        @Override
        public List<V> create() {
            return new LinkedList<V>();
        }
    };

    private final Merge<List<V>> defMerge = new Merge<List<V>>() {

        @Override
        public void merge(List<V> src, List<V> dest) {
            if (src != null && dest != null) {
                dest.addAll(src);
            }
        }
    };

    @Override
    protected Creator<List<V>> getDefCreator() {
        return defCreator;
    }

    @Override
    protected Merge<List<V>> getDefMerge() {
        return defMerge;
    }

}
