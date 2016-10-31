/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.util;

import com.baijia.tianxiao.sqlbuilder.bean.Expression;
import com.baijia.tianxiao.sqlbuilder.bean.SQLOperator;
import com.baijia.tianxiao.sqlbuilder.bean.impl.*;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author cxm
 * @version 1.0
 * @title SqlConditions
 * @desc 生成查询条件的接口
 * @date 2015年12月1日
 */
public class Expressions {

    public static <T extends Serializable> Expression createSimpleExpression(String fieldName, T value,
                                                                             SQLOperator operator) {
        return new SimpleExp(fieldName, value, operator);
    }

    public static <T extends Serializable> Expression eq(String fieldName, T value) {
        return new SimpleExp(fieldName, value, SQLOperator.EQ);
    }

    public static <T extends Serializable> Expression ne(String fieldName, T value) {
        return new SimpleExp(fieldName, value, SQLOperator.NE);
    }

    public static <T extends Serializable> Expression gt(String fieldName, T value) {
        return new SimpleExp(fieldName, value, SQLOperator.GT);
    }

    public static <T extends Serializable> Expression ge(String fieldName, T value) {
        return new SimpleExp(fieldName, value, SQLOperator.GE);
    }

    public static <T extends Serializable> Expression le(String fieldName, T value) {
        return new SimpleExp(fieldName, value, SQLOperator.LE);
    }

    public static <T extends Serializable> Expression lt(String fieldName, T value) {
        return new SimpleExp(fieldName, value, SQLOperator.LT);
    }

    public static <T extends Serializable> Expression like(String fieldName, T value) {
        return new LikeExp(fieldName, value);
    }

    public static <T extends Serializable> Expression like(String fieldName, T value, MatchMode matchMode) {
        return new LikeExp(fieldName, value, matchMode);
    }

    public static <T extends Serializable> Expression in(String fieldName, T[] values) {
        return new InExp(fieldName, Lists.newArrayList(values));
    }

    public static <T extends Serializable> Expression in(String fieldName, Collection<T> values) {
        return new InExp(fieldName, values);
    }

    public static Expression tupleIn(List<String> fieldNames, Collection<List<Object>> values) {
        return new TupleInExp(fieldNames, values);
    }

    public static <T extends Serializable> Expression notin(String fieldName, T[] values) {
        return new NotInExp(fieldName, Lists.newArrayList(values));
    }

    public static <T extends Serializable> Expression notin(String fieldName, Collection<T> values) {
        return new NotInExp(fieldName, values);
    }

    public static <T extends Serializable> Expression isNull(String fieldName) {
        return new NullExp(fieldName);
    }

    public static <T extends Serializable> Expression isNotNull(String fieldName) {
        return new NotNullExp(fieldName);
    }

    public static <T extends Comparable<?>> Expression between(String fieldName, T startTime, T endTime) {
        return new BetweenExp(fieldName, startTime, endTime);
    }

    public static <T extends Serializable> Expression and(Expression leftExp, Expression rightExp) {
        return new AndExp(leftExp, rightExp);
    }

    public static <T extends Serializable> Expression and(Expression... exps) {
        return new AndExp(exps);
    }

    public static <T extends Serializable> Expression or(Expression leftExp, Expression rightExp) {
        return new OrExp(leftExp, rightExp);
    }

    public static <T extends Serializable> Expression or(Expression... exps) {
        return new OrExp(exps);
    }

    public static <T extends Serializable> Expression dateformat(String fieldName, String format, String value) {
        return new DateFormatExp(fieldName, format, value);
    }
}
