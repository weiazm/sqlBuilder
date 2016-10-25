/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.bean.impl;

import com.baijia.tianxiao.sqlbuilder.SqlBuilderContext;
import com.baijia.tianxiao.sqlbuilder.bean.Expression;
import com.baijia.tianxiao.sqlbuilder.bean.SQLOperator;
import com.baijia.tianxiao.sqlbuilder.util.ColumnUtil;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @title InCondition
 * @desc TODO
 * @author cxm
 * @date 2015年8月10日
 * @version 1.0
 */
public class InExp implements Expression {

    private String properties;

    private Collection<? extends Serializable> values;

    private Map<String, Object> inConditionMap = new HashMap<String, Object>();

    public static final SQLOperator OPERATOR = SQLOperator.IN;

    public InExp(String properties, Collection<? extends Serializable> values) {
        this.properties = properties;
        this.values = values;
    }

    @Override
    public String toSql(SqlBuilderContext context) {

        StringBuilder sql = new StringBuilder();
        String columnName = checkAndGetColumn(context.getFieldMapColumn(), context.getColumnMapField());
        String varName = context.getVarName(columnName, OPERATOR);
        if (columnName != null) {
            sql.append(columnName).append(" IN (:").append(varName).append(") ");
            inConditionMap.put(varName, values);
        } else {
            throw new IllegalArgumentException("can not found fix properties or column for:" + properties);
        }

        return sql.toString();
    }

    @Override
    public Map<String, Object> getParamNameValueMap() {

        return inConditionMap;

    }

    @Override
    public String checkAndGetColumn(Map<String, String> fieldMapColumn, Map<String, String> columnMapField) {
        Preconditions.checkArgument(StringUtils.isNoneBlank(this.properties),
            "can not to in properties because in properties is empty");
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(values), "in values is empty.");
        return ColumnUtil.getColumnName(this.properties, fieldMapColumn, columnMapField);
    }
}
