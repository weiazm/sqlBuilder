/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.bean.impl;

import com.baijia.tianxiao.sqlbuilder.SqlBuilderContext;
import com.baijia.tianxiao.sqlbuilder.bean.Expression;
import com.baijia.tianxiao.sqlbuilder.util.ColumnUtil;
import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * @title NullExpression
 * @desc TODO
 * @author cxm
 * @date 2015年8月14日
 * @version 1.0
 */
public class NotNullExp implements Expression {

    private final String properties;

    public NotNullExp(String properties) {
        this.properties = properties;
    }

    @Override
    public String toSql(SqlBuilderContext context) {
        StringBuilder sql = new StringBuilder();
        String columnName = checkAndGetColumn(context.getFieldMapColumn(), context.getColumnMapField());
        return sql.append(columnName).append(" IS NOT NULL").toString();

    }

    @Override
    public Map<String, Object> getParamNameValueMap() {
        return Collections.emptyMap();
    }

    @Override
    public String checkAndGetColumn(Map<String, String> fieldMapColumn, Map<String, String> columnMapField) {
        Preconditions.checkArgument(StringUtils.isNoneBlank(this.properties),
            "can not to in properties because in properties is empty");
        return ColumnUtil.getColumnName(this.properties, fieldMapColumn, columnMapField);
    }

}
