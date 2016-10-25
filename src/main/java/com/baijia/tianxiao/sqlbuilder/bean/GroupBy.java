/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.bean;

import com.baijia.tianxiao.sqlbuilder.SqlBuilderContext;
import com.baijia.tianxiao.sqlbuilder.util.ColumnUtil;
import com.google.common.collect.Sets;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @title GroupBy
 * @desc TODO
 * @author cxm
 * @date 2015年10月29日
 * @version 1.0
 */
public class GroupBy implements SqlElement {

    private Set<String> groupByProps = Sets.newHashSet();

    public boolean add(String column) {
        return groupByProps.add(column);
    }

    public String toSql(SqlBuilderContext context) {
        if (CollectionUtils.isNotEmpty(groupByProps)) {
            Set<String> groupByColumns = Sets.newHashSet();
            for (String prop : groupByProps) {
                groupByColumns
                    .add(ColumnUtil.getColumnName(prop, context.getFieldMapColumn(), context.getColumnMapField()));
            }
            return " GROUP BY " + StringUtils.join(groupByColumns, ",") + " ";
        }
        return "";
    }
}
