/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.bean;

/**
 * @author cxm
 * @version 1.0
 * @title SqlOperator
 * @desc TODO
 * @date 2015年12月1日
 */
public enum SQLOperator {

    EQ(" = "),

    NE(" <> "),

    GT(" > "),

    GE(" >= "),

    LT(" < "),

    LE(" <= "),

    IN(" IN "),

    NOT_IN(" NOT_IN "),

    AND(" AND "),

    OR(" OR "),

    LIKE(" LIKE "),

    IS_NULL(" IS_NULL "),

    IS_NOT_NULL(" IS_NOT_NULL "),

    BETWEEN(" BETWEEN "),

    DATEFORMAT(" DATE_FORMAT ");

    private String operator;

    private SQLOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return this.operator;
    }

}
