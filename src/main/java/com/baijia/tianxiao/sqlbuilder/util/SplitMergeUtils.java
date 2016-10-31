package com.baijia.tianxiao.sqlbuilder.util;

import java.util.*;

public class SplitMergeUtils {
    public static int BATCH_SIZE = 500;

    public static <T> List<List<T>> splitList(List<T> orig, int batchSize) {
        if (orig == null || orig.isEmpty() || batchSize < 1) {
            return Arrays.asList(orig);
        } else {
            int size = orig.size();
            int len = calSplitLen(batchSize, size);
            List<List<T>> result = new ArrayList<>(len);
            for (int i = 0; i < len; i++) {
                result.add(orig.subList(i * batchSize, ((i + 1) * batchSize) < size ? (i + 1) * batchSize : size));
            }
            return result;
        }
    }

    public static <T> List<T> merge(List<List<T>> splits) {
        if (splits != null && splits.size() > 0) {
            List<T> list = new ArrayList<>();
            for (List<T> split : splits) {
                list.addAll(split);
            }
            return list;
        } else {
            return Collections.emptyList();
        }
    }

    public static <T> Set<Set<T>> splitSet(Set<T> orig, int batchSize) {
        if (orig == null || orig.isEmpty() || batchSize < 1) {
            return new HashSet<>(Arrays.asList(orig));
        } else {
            int size = orig.size();
            int len = calSplitLen(batchSize, size);
            Set<Set<T>> result = new HashSet<>(len);
            List<T> list = new ArrayList<>(orig);
            for (int i = 0; i < len; i++) {
                result.add(new HashSet<>(list.subList(i * batchSize,
                        ((i + 1) * batchSize) < size ? (i + 1) * batchSize : size)));
            }
            return result;
        }
    }

    public static <T> Set<T> merge(Set<Set<T>> splits) {
        if (splits != null && splits.size() > 0) {
            Set<T> list = new HashSet<>();
            for (Set<T> split : splits) {
                list.addAll(split);
            }
            return list;
        } else {
            return Collections.emptySet();
        }
    }

    public static <T> Collection<Collection<T>> splitCollection(Collection<T> orig, int batchSize) {
        if (orig == null || orig.isEmpty() || batchSize < 1) {
            Collection<Collection<T>> result = new HashSet<Collection<T>>();
            result.add(new HashSet<T>(orig));
            return result;
        } else {
            int size = orig.size();
            int len = calSplitLen(batchSize, size);
            Collection<Collection<T>> result = new HashSet<>(len);
            List<T> list = null;
            if (orig instanceof List) {
                list = (List<T>) orig;
            } else {
                list = new ArrayList<>(orig);
            }
            for (int i = 0; i < len; i++) {
                result.add(new HashSet<>(list.subList(i * batchSize,
                        ((i + 1) * batchSize) < size ? (i + 1) * batchSize : size)));
            }
            return result;
        }
    }

    private static int calSplitLen(int batchSize, int size) {
        return ((size - 1) / batchSize) + 1;
    }

    public static <T> List<List<T>> splitList(List<T> orig) {
        return splitList(orig, BATCH_SIZE);
    }

    public static <T> Set<Set<T>> splitSet(Set<T> orig) {
        return splitSet(orig, BATCH_SIZE);
    }

    public static <T> Collection<Collection<T>> splitCollection(Collection<T> orig) {
        return splitCollection(orig, BATCH_SIZE);
    }

}
