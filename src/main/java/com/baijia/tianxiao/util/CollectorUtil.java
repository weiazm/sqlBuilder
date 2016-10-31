package com.baijia.tianxiao.util;

import com.google.common.base.Function;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.TransformerUtils;

import java.util.*;

/**
 * @author cxm
 * @version 1.0
 * @title CollectorUtil
 * @desc TODO
 * @date 2015年9月27日
 */
public class CollectorUtil {
    public static <K, V> Map<K, List<V>> group(Collection<V> values, Function<V, K> keyFun) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }
        Map<K, List<V>> result = new HashMap<K, List<V>>();
        for (V v : values) {
            K key = keyFun.apply(v);
            if (key != null) {
                if (!result.containsKey(key)) {
                    result.put(key, new ArrayList<V>());
                }
                result.get(key).add(v);
            }
        }
        return result;
    }

    /**
     * @param values
     * @param keyTransformer 可以适用TransformerUtils
     * @return
     */
    public static <K, V> Map<K, List<V>> group(Collection<V> values, Transformer<V, K> keyTransformer) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }
        Map<K, List<V>> result = new HashMap<K, List<V>>();
        for (V v : values) {
            K key = keyTransformer.transform(v);
            if (key != null) {
                if (!result.containsKey(key)) {
                    result.put(key, new ArrayList<V>());
                }
                result.get(key).add(v);
            }
        }
        return result;
    }

    public static <K, V, O> Map<K, List<V>> group(Collection<O> values, Function<O, K> keyFun,
                                                  Function<O, V> valueFun) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }
        Map<K, List<V>> results = new HashMap<>();
        for (O o : values) {
            K key = keyFun.apply(o);
            if (key != null) {
                if (!results.containsKey(key)) {
                    results.put(key, new ArrayList<V>());
                }
                results.get(key).add(valueFun.apply(o));
            }
        }
        return results;
    }

    public static <K, V, O> Map<K, List<V>> group(Collection<O> values, Transformer<O, K> keyFun,
                                                  Transformer<O, V> valueFun) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }
        Map<K, List<V>> results = new HashMap<>();
        for (O o : values) {
            K key = keyFun.transform(o);
            if (key != null) {
                if (!results.containsKey(key)) {
                    results.put(key, new ArrayList<V>());
                }
                results.get(key).add(valueFun.transform(o));
            }
        }
        return results;
    }

    public static <K, V, O> Map<K, V> collectMap(Collection<O> values, Function<O, K> keyFun, Function<O, V> valueFun) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }
        Map<K, V> results = new HashMap<>();
        for (O o : values) {
            K key = keyFun.apply(o);
            if (key != null) {
                results.put(key, valueFun.apply(o));
            }
        }
        return results;
    }

    public static <K, V, O> Map<K, V> collectMap(Collection<O> values, Transformer<O, K> keyFun,
                                                 Transformer<O, V> valueFun) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }
        Map<K, V> results = new HashMap<>();
        for (O o : values) {
            K key = keyFun.transform(o);
            if (key != null) {
                results.put(key, valueFun.transform(o));
            }
        }
        return results;
    }

    public static <K, V, O> Map<K, V> collectMap(Collection<O> values, String keyMethodName, String valueMethodName) {
        return collectMap(values, TransformerUtils.<O, K>invokerTransformer(keyMethodName),
                TransformerUtils.<O, V>invokerTransformer(valueMethodName));
    }

    public static <K, O> Map<K, O> collectMap(Collection<O> values, Function<O, K> keyFun) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }
        Map<K, O> results = new HashMap<>();
        for (O o : values) {
            K key = keyFun.apply(o);
            if (key != null) {
                results.put(key, o);
            }
        }
        return results;
    }

    public static <K, O> Map<K, O> collectMap(Collection<O> values, Transformer<O, K> keyFun) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }
        Map<K, O> results = new HashMap<>();
        for (O o : values) {
            K key = keyFun.transform(o);
            if (key != null) {
                results.put(key, o);
            }
        }
        return results;
    }

    public static <K, O> Map<K, O> collectMap(Collection<O> values, String methodName) {
        return collectMap(values, TransformerUtils.<O, K>invokerTransformer(methodName));
    }

    public static <K, V> Map<K, V> groupAndCalcu(Collection<V> values, Function<V, K> keyFun,
                                                 Calculate<V> calculateFun) {
        return groupAndCalcu(values, null, keyFun, calculateFun);
    }

    public static <K, V> Map<K, V> groupAndCalcu(Collection<V> values, Map<K, V> result, Function<V, K> keyFun,
                                                 Calculate<V> calculateFun) {
        if (result == null) {
            result = new HashMap<>();
        }
        if (CollectionUtils.isEmpty(values)) {
            return result;
        }
        for (V v : values) {
            K key = keyFun.apply(v);
            if (key != null) {
                result.put(key, calculateFun.calc(result.get(key), v));
            }
        }
        return result;
    }

    public static <K, V> Map<K, V> groupAndCalcu(Collection<V> values, Transformer<V, K> keyFun,
                                                 Calculate<V> calculateFun) {
        Map<K, V> result = new HashMap<>();
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }
        for (V v : values) {
            K key = keyFun.transform(v);
            if (key != null) {
                result.put(key, calculateFun.calc(result.get(key), v));
            }
        }
        return result;
    }

    public static <K, O> Collection<K> collect(Collection<O> values, Function<O, K> keyFun) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }
        Set<K> results = new HashSet<>();
        for (O o : values) {
            if (keyFun.apply(o) != null) {
                results.add(keyFun.apply(o));
            }
        }
        return results;
    }

    public static <K, O> Collection<K> collect(Collection<O> values, String methodName) {
        return collect(values, TransformerUtils.<O, K>invokerTransformer(methodName));
    }

    public static <K, O> Collection<K> collect(Collection<O> values, Transformer<O, K> keyFun) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }
        Set<K> results = new HashSet<>();
        for (O o : values) {
            K key = keyFun.transform(o);
            if (key != null) {
                results.add(key);
            }
        }
        return results;
    }

    public interface Calculate<T> {

        T calc(T t1, T t2);
    }
}
