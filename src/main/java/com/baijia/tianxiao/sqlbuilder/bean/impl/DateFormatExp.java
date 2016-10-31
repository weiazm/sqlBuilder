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
 * @author zhangbing
 * @version 1.0
 * @title DateFormatExp
 * @desc TODO
 * @date 2016年1月11日
 */
public class DateFormatExp implements Expression {

    public static final SQLOperator OPERATOR = SQLOperator.DATEFORMAT;
    private String fieldName;
    private String format;
    private String value;
    private Map<String, Object> paramNameMap = new HashMap<String, Object>();

    public DateFormatExp(String fieldName, String format, String value) {
        this.fieldName = fieldName;
        this.format = format;
        this.value = value;
    }

    @Override
    public String toSql(SqlBuilderContext context) {
        StringBuilder sql = new StringBuilder();
        sql.append(" date_format(").append(fieldName).append(",'").append(format).append("')").append("='")
                .append(value).append("'");
        return sql.toString();
    }

    @Override
    public Map<String, Object> getParamNameValueMap() {
        return paramNameMap;
    }

    @Override
    public String checkAndGetColumn(Map<String, String> fieldMapColumn, Map<String, String> columnMapField) {
        Preconditions.checkArgument(StringUtils.isNoneBlank(this.fieldName),
                "can not to in properties because in properties is empty");
        Preconditions.checkArgument(format != null && value != null, "format: %s or value: %s is null", format, value);
        return ColumnUtil.getColumnName(this.fieldName, fieldMapColumn, columnMapField);

    }

}
