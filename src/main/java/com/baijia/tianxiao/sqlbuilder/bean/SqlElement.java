/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.bean;

import com.baijia.tianxiao.sqlbuilder.SqlBuilderContext;

/**
 * @author cxm
 * @version 1.0
 * @title SqlElement
 * @desc TODO
 * @date 2015年12月2日
 */
public interface SqlElement {

    public abstract String toSql(SqlBuilderContext context);

}
