/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.bean;

import com.baijia.tianxiao.sqlbuilder.SqlBuilderContext;

/**
 * @title SqlElement
 * @desc TODO
 * @author cxm
 * @date 2015年12月2日
 * @version 1.0
 */
public interface SqlElement {

    public abstract String toSql(SqlBuilderContext context);

}
