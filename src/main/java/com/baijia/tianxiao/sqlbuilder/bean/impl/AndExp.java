/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */

package com.baijia.tianxiao.sqlbuilder.bean.impl;

import com.baijia.tianxiao.sqlbuilder.SqlBuilderContext;
import com.baijia.tianxiao.sqlbuilder.bean.Expression;
import com.baijia.tianxiao.sqlbuilder.bean.SQLOperator;
import com.baijia.tianxiao.sqlbuilder.bean.SqlElement;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cxm
 * @version 1.0
 * @title AndExp
 * @desc TODO
 * @date 2015年12月1日
 */
public class AndExp implements Expression {

    public static final SQLOperator OPERATOR = SQLOperator.AND;
    private Expression[] conditions;

    public AndExp(Expression leftCondition, Expression rightCondition) {
        this.conditions = new Expression[]{leftCondition, rightCondition};
    }

    public AndExp(Expression... conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toSql(SqlBuilderContext context) {

        checkAndGetColumn(context.getFieldMapColumn(), context.getColumnMapField());

        StringBuilder sb = new StringBuilder();
        sb.append(" ( ");
        List<String> expSqlList = Lists.newArrayList();
        for (SqlElement expression : conditions) {
            expSqlList.add(expression.toSql(context));
        }
        if (expSqlList.size() > 1) {
            sb.append(StringUtils.join(expSqlList, " AND "));
        } else {
            sb.append(expSqlList.get(0));
        }
        sb.append(" ) ");

        return sb.toString();
    }

    @Override
    public Map<String, Object> getParamNameValueMap() {
        Map<String, Object> params = new HashMap<String, Object>();
        for (Expression expression : conditions) {
            params.putAll(expression.getParamNameValueMap());
        }
        return params;
    }

    @Override
    public String checkAndGetColumn(Map<String, String> fieldMapColumn, Map<String, String> columnMapField) {
        Preconditions.checkArgument(conditions != null && conditions.length > 0, "and conditions is empty");
        for (Expression expression : conditions) {
            Preconditions.checkNotNull(expression, "and condition: %s is null:", expression);
        }
        return null;

    }

}
