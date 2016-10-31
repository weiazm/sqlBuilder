/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.bean;

import com.baijia.tianxiao.sqlbuilder.SqlBuilderContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author cxm
 * @version 1.0
 * @title From
 * @desc TODO
 * @date 2015年8月13日
 */
public class From implements SqlElement {

    private String catalog;

    private String tableName;

    public From(String catalog, String tableName) {
        super();
        this.catalog = catalog;
        this.tableName = tableName;
    }

    public From(String tableName) {
        this.catalog = "";
        this.tableName = tableName;
    }

    public String toSql(SqlBuilderContext context) {
        if (StringUtils.isBlank(tableName)) {
            throw new IllegalArgumentException("table name is null");
        }
        if (StringUtils.isNoneBlank(catalog)) {
            return catalog + "." + tableName;
        } else {
            return tableName;
        }
    }

}
