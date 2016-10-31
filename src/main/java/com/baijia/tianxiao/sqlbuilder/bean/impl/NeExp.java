/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.bean.impl;

import com.baijia.tianxiao.sqlbuilder.bean.SQLOperator;

/**
 * @author cxm
 * @version 1.0
 * @title NeExp
 * @desc TODO
 * @date 2015年12月1日
 */
public class NeExp extends SimpleExp {

    /**
     * @param propertyName
     * @param value
     * @param operator
     */
    public NeExp(String propertyName, Object value, SQLOperator operator) {
        super(propertyName, value, SQLOperator.NE);
    }

}
