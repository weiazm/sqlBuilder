/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.bean;

import com.baijia.tianxiao.sqlbuilder.SqlBuilderContext;
import com.baijia.tianxiao.sqlbuilder.util.ColumnUtil;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @title OrderByField
 * @desc TODO
 * @author cxm
 * @date 2015年11月26日
 * @version 1.0
 */
public class OrderByField implements Expression {

    private String propertyName;

    private Collection<? extends Serializable> values;

    private Map<String, Object> fieldConditionMap = new HashMap<String, Object>();

    public OrderByField(String propertyName, Collection<?> values) {
        this.propertyName = propertyName;
    }

    @Override
    public String toSql(SqlBuilderContext context) {
        String fieldSql = toFieldSql(context);
        if (StringUtils.isNoneBlank(fieldSql)) {
            return " ORDER BY " + fieldSql;
        }
        return "";
    }

    public String toFieldSql(SqlBuilderContext context) {
        if (CollectionUtils.isNotEmpty(values)) {
            String property = checkAndGetColumn(context.getFieldMapColumn(), context.getColumnMapField());
            String varName = "FIELD_" + property;
            fieldConditionMap.put(varName, values);
            return " FIELD( " + propertyName + ", :" + varName + ")";
        }
        return "";
    }

    @Override
    public Map<String, Object> getParamNameValueMap() {
        return fieldConditionMap;
    }

    @Override
    public String checkAndGetColumn(Map<String, String> fieldMapColumn, Map<String, String> columnMapField) {
        Preconditions.checkArgument(StringUtils.isNoneBlank(this.propertyName),
            "can not order by field by empty property");
        return ColumnUtil.getColumnName(this.propertyName, fieldMapColumn, columnMapField);
    }

}
