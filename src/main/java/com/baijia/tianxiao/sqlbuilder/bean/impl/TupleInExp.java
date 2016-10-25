/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.bean.impl;

import com.baijia.tianxiao.sqlbuilder.SqlBuilderContext;
import com.baijia.tianxiao.sqlbuilder.bean.Expression;
import com.baijia.tianxiao.sqlbuilder.bean.SQLOperator;
import com.baijia.tianxiao.sqlbuilder.util.ColumnUtil;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
public class TupleInExp implements Expression {

    private List<String> tupleProperty;

    private Collection<List<Object>> values;

    private Map<String, Object> inConditionMap = new HashMap<String, Object>();

    public static final SQLOperator OPERATOR = SQLOperator.IN;

    public TupleInExp(List<String> tupleProperty, Collection<List<Object>> values) {
        this.tupleProperty = tupleProperty;
        this.values = values;
    }

    @Override
    public String toSql(SqlBuilderContext context) {

        StringBuilder sql = new StringBuilder();
        String columnName = checkAndGetColumn(context.getFieldMapColumn(), context.getColumnMapField());
        if (columnName != null) {
            sql.append(columnName).append(" IN ( ").append(prepareTupleInValue(columnName, context)).append(") ");
            // inConditionMap.put(varName, prepareTupleInValue());
        } else {
            throw new IllegalArgumentException("can not found fix properties or column for:" + tupleProperty);
        }

        return sql.toString();
    }

    private String prepareTupleInValue(String columnName, SqlBuilderContext context) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(values), "tuple in value is empty");
        StringBuilder sb = new StringBuilder();

        String varName = null;
        int i = 0;
        for (Collection<?> tupleValue : this.values) {
            Preconditions.checkArgument(tupleValue.size() == this.tupleProperty.size(),
                "tuple value is not match property size");
            int j = 0;
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append("(");
            for (Object value : tupleValue) {
                if (j > 0) {
                    sb.append(",");
                }
                varName = context.getVarName(columnName + "_" + i + "_" + j, OPERATOR);
                sb.append(":").append(varName);
                this.inConditionMap.put(varName, value);
                j++;
            }
            sb.append(")");
            i++;
        }
        return sb.toString();
    }

    @Override
    public Map<String, Object> getParamNameValueMap() {
        return inConditionMap;
    }

    @Override
    public String checkAndGetColumn(Map<String, String> fieldMapColumn, Map<String, String> columnMapField) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(tupleProperty), "tuple in column is empty");
        StringBuilder sb = new StringBuilder();
        for (String properties : this.tupleProperty) {
            Preconditions.checkArgument(StringUtils.isNoneBlank(properties), "in properties is empty");
            if (sb.length() == 0) {
                sb.append("(");
            } else {
                sb.append(",");
            }
            sb.append(ColumnUtil.getColumnName(properties, fieldMapColumn, columnMapField));
        }
        sb.append(")");

        return sb.toString();
    }
}
