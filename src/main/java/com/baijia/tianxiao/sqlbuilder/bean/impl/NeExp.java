/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.bean.impl;

import com.baijia.tianxiao.sqlbuilder.bean.SQLOperator;

/**
 * @title NeExp
 * @desc TODO
 * @author cxm
 * @date 2015年12月1日
 * @version 1.0
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
