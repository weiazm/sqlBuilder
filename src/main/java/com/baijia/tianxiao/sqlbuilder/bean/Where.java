/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.bean;

import com.baijia.tianxiao.sqlbuilder.SqlBuilderContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @title Where
 * @desc TODO
 * @author cxm
 * @date 2015年8月10日
 * @version 1.0
 */
public class Where {

    private List<Expression> condtions = new ArrayList<Expression>();

    private List<String> conditionSql = null;
    private Map<String, Object> paramValueMap = null;

    private String sql = null;

    public void addCondition(Expression condition) {
        this.condtions.add(condition);
    }

    /**
     * @return the condtions
     */
    public List<Expression> getCondtions() {

        return condtions;
    }

    /**
     * @param condtions the condtions to set
     */
    public void setCondtions(List<Expression> condtions) {

        this.condtions = condtions;
    }

    public String toSql(SqlBuilderContext context) {
        if (StringUtils.isNoneBlank(sql)) {
            return sql;
        }
        init(context);
        return sql;
    }

    private void init(SqlBuilderContext context) {
        paramValueMap = new HashMap<String, Object>();
        sql = "";
        if (CollectionUtils.isNotEmpty(condtions)) {
            conditionSql = new ArrayList<String>(condtions.size());
            for (Expression condition : condtions) {
                conditionSql.add(condition.toSql(context));
                paramValueMap.putAll(condition.getParamNameValueMap());
            }
            sql = " WHERE " + StringUtils.join(conditionSql, " AND ");
        }
    }

    public Map<String, Object> collectParamValue(SqlBuilderContext context) {
        if (paramValueMap == null) {
            init(context);
        }
        return paramValueMap;
    }

}
