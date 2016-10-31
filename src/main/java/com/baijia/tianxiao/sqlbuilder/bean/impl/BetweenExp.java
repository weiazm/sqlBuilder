/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.bean.impl;

import com.baijia.tianxiao.sqlbuilder.SqlBuilderContext;
import com.baijia.tianxiao.sqlbuilder.bean.Expression;
import com.baijia.tianxiao.sqlbuilder.bean.SQLOperator;
import com.baijia.tianxiao.sqlbuilder.util.ColumnUtil;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cxm
 * @version 1.0
 * @title Between
 * @desc TODO
 * @date 2015年8月13日
 */
public class BetweenExp implements Expression {

    public static final SQLOperator OPERATOR = SQLOperator.BETWEEN;
    private String properties;
    private Comparable<?> start;
    private Comparable<?> end;
    private Map<String, Object> paramNameMap = new HashMap<String, Object>();

    public BetweenExp(String properties, Comparable<?> start, Comparable<?> end) {
        this.properties = properties;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toSql(SqlBuilderContext context) {
        StringBuilder sql = new StringBuilder();
        String columnName = checkAndGetColumn(context.getFieldMapColumn(), context.getColumnMapField());
        String startName = context.getVarName("start_" + columnName, OPERATOR);
        String endName = context.getVarName("end_" + columnName, OPERATOR);
        sql.append(columnName).append(" between :").append(startName).append(" and :").append(endName);
        paramNameMap.put(startName, start);
        paramNameMap.put(endName, end);
        return sql.toString();
    }

    @Override
    public Map<String, Object> getParamNameValueMap() {
        return paramNameMap;
    }

    @Override
    public String checkAndGetColumn(Map<String, String> fieldMapColumn, Map<String, String> columnMapField) {
        Preconditions.checkArgument(StringUtils.isNoneBlank(this.properties),
                "can not to in properties because in properties is empty");
        Preconditions.checkArgument(start != null && end != null, "start: %s or end: %s is null", start, end);
        return ColumnUtil.getColumnName(this.properties, fieldMapColumn, columnMapField);

    }

}
