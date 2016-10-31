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
 * @title EqConditon
 * @desc TODO
 * @date 2015年8月10日
 */
public class LikeExp implements Expression {

    public static final SQLOperator OPERATOR = SQLOperator.IN;
    private final String properties;
    private final Object value;
    private MatchMode matchMode = MatchMode.EXACT;
    private Map<String, Object> valueMap = new HashMap<String, Object>(1);

    public LikeExp(String properties, Object value) {
        this.properties = properties;
        this.value = value;
    }

    public LikeExp(String properties, Object value, MatchMode matchMode) {
        this.properties = properties;
        this.value = value;
        this.matchMode = matchMode;
    }

    @Override
    public String toSql(SqlBuilderContext context) {
        StringBuilder sql = new StringBuilder();
        String columnName = checkAndGetColumn(context.getFieldMapColumn(), context.getColumnMapField());
        String varName = context.getVarName(properties, OPERATOR);
        sql.append(columnName).append(" LIKE :").append(varName).append(" ");
        valueMap.put(varName, matchMode.toMatchString(value.toString()));
        return sql.toString();
    }

    @Override
    public Map<String, Object> getParamNameValueMap() {
        return valueMap;
    }

    /**
     * @return the matchMode
     */
    public MatchMode getMatchMode() {
        return matchMode;
    }

    @Override
    public String checkAndGetColumn(Map<String, String> fieldMapColumn, Map<String, String> columnMapField) {
        Preconditions.checkArgument(StringUtils.isNoneBlank(this.properties),
                "can not to in properties because in properties is empty");
        Preconditions.checkArgument(value instanceof String, "value is not string");
        Preconditions.checkArgument(StringUtils.isNoneBlank((String) value), "like value is null.");
        return ColumnUtil.getColumnName(this.properties, fieldMapColumn, columnMapField);
    }

}
