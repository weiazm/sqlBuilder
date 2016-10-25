/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import lombok.Data;

/**
 * @title Select
 * @desc 需要重构Select,现在太粗暴
 * @author cxm
 * @date 2015年8月10日
 * @version 1.0
 */
@Data
public class Select {

    private List<String> select = Lists.newArrayList();

    private List<QueryProp> queryProps = new ArrayList<QueryProp>();

    private Set<String> aliasSet = new HashSet<>();

    public void add(String exp) {
        String[] arr = exp.split(" ");
        if (arr.length > 1) {
            this.add(arr[0], arr[2]);
        } else {
            this.add(arr[0], arr[0]);
        }
    }

    public void add(String column, String alias) {
        if (this.aliasSet.add(alias)) {
            if (column.contains("(")) {
                queryProps.add(new QueryProp(column, alias, true));
            } else {
                queryProps.add(new QueryProp(column, alias));
            }
        }
    }

    public void add(String table, String column, String alias) {
        if (StringUtils.isBlank(table)) {
            add(column, alias);
        } else {
            if (this.aliasSet.add(alias)) {
                if (column.contains("(")) {
                    queryProps.add(new QueryProp(table, column, alias, true));
                } else {
                    queryProps.add(new QueryProp(table, column, alias, false));
                }
            }

        }
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(queryProps);
    }

    public String toSql() {
        select.clear();
        for (QueryProp queryProp : queryProps) {
            select.add(queryProp.toSql());
        }
        return "select " + StringUtils.join(select, ',');
    }

    /**
     * @return the queryProps
     */
    public List<QueryProp> getQueryProps() {

        return queryProps;
    }

}
