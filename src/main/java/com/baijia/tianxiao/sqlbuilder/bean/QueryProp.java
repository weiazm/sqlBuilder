package com.baijia.tianxiao.sqlbuilder.bean;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryProp {

    private String table;

    private String column;

    private String alias;

    private boolean isFunction;

    public QueryProp(String column, String alias, boolean isFunction) {
        this("", column, alias, isFunction);
    }

    public QueryProp(String column, String alias) {
        this(column, alias, false);
    }

    public String toSql() {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isNoneBlank(table)) {
            sql.append(table).append(".");
        }
        sql.append(column);
        if (StringUtils.isNoneBlank(alias)) {
            sql.append(" AS ").append(alias);
        }
        return sql.toString();
    }

}