/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.bean;

import com.baijia.tianxiao.sqlbuilder.SqlBuilderContext;
import com.baijia.tianxiao.sqlbuilder.util.ColumnUtil;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author cxm
 * @version 1.0
 * @title Order
 * @desc TODO
 * @date 2015年8月13日
 */
public class Order implements SqlElement {

    private boolean ascending;
    private String[] propertyName;

    private boolean orderGBKFun;

    private Order(boolean ascending, boolean orderGBKFun, String... propertyName) {
        this.ascending = ascending;
        this.propertyName = propertyName;
        this.orderGBKFun = orderGBKFun;
    }

    private Order(boolean ascending, String... propertyName) {
        this(ascending, false, propertyName);
    }

    /**
     * Ascending order
     *
     * @param propertyName
     * @return Order
     */
    public static Order asc(String... propertyName) {
        return asc(false, propertyName);
    }

    public static Order asc(String propertyName, boolean orderGBKFun) {
        return new Order(true, orderGBKFun, propertyName);
    }

    public static Order asc(boolean orderGBKFun, String... propertyName) {
        return new Order(true, orderGBKFun, propertyName);
    }

    /**
     * Descending order
     *
     * @param propertyName
     * @return Order
     */
    public static Order desc(String... propertyName) {
        return desc(false, propertyName);
    }

    public static Order desc(String propertyName, boolean orderGBKFun) {
        return new Order(false, orderGBKFun, propertyName);
    }

    public static Order desc(boolean orderGBKFun, String... propertyName) {
        return new Order(false, orderGBKFun, propertyName);
    }

    public String toSql(SqlBuilderContext context) {
        Preconditions.checkArgument(ArrayUtils.isNotEmpty(this.propertyName), "can not order empty property");

        StringBuilder sb = new StringBuilder();
        String order = this.ascending ? " ASC" : " DESC";
        for (String column : this.propertyName) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            if (orderGBKFun) {
                sb.append(" CONVERT ( ");
            }
            sb.append(ColumnUtil.getColumnName(column, context.getFieldMapColumn(), context.getColumnMapField()));
            if (orderGBKFun) {
                sb.append(" USING GBK)");
            }
            sb.append(" ").append(order);
        }

        String prop = sb.toString();
        return " ORDER BY " + prop;
    }
}
