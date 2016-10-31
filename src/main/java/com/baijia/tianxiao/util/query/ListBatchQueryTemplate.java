package com.baijia.tianxiao.util.query;

import java.util.LinkedList;
import java.util.List;

/**
 * 实现返回结果为LIST<V>的BatchQueryTemplate
 *
 * @author LJX
 * @version 1.0
 * @title ListBatchQueryTemplate
 * @desc TODO
 * @date 2014年9月17日
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
