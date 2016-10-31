package com.baijia.tianxiao.util.query;

import java.util.HashMap;
import java.util.Map;

/**
 * 实现返回结果为MAP<K,V>的BatchQueryTemplate
 *
 * @author LJX
 * @version 1.0
 * @title MapBatchQueryTemplate
 * @desc TODO
 * @date 2014年9月17日
 */
public class MapBatchQueryTemplate<Q, K, V> extends BatchQueryTemplate<Q, Map<K, V>> {

    private final Creator<Map<K, V>> defCreator = new Creator<Map<K, V>>() {

        @Override
        public Map<K, V> create() {
            return new HashMap<K, V>();
        }
    };

    private final Merge<Map<K, V>> defMerge = new Merge<Map<K, V>>() {

        @Override
        public void merge(Map<K, V> src, Map<K, V> dest) {
            if (src != null && dest != null) {
                dest.putAll(src);
            }
        }
    };

    @Override
    protected Creator<Map<K, V>> getDefCreator() {
        return defCreator;
    }

    @Override
    protected Merge<Map<K, V>> getDefMerge() {
        return defMerge;
    }

}
