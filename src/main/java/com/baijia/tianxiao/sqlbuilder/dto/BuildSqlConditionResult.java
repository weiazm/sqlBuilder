package com.baijia.tianxiao.sqlbuilder.dto;

import com.google.common.base.Preconditions;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cxm
 * @version 1.0
 * @title BuildSqlConditionResult
 * @desc TODO
 * @date 2016年5月9日
 */
@Data
public class BuildSqlConditionResult {

    private Map<String, Object> queryParams = new HashMap<>();

    private StringBuilder where = new StringBuilder();

    private StringBuilder from = new StringBuilder();

    private String groupBy;

    private StringBuilder select = new StringBuilder();

    private String orderBy;

    private String having;

    private PageDto page;

    public String toSql(String select) {
        if (StringUtils.isNoneBlank(from.toString().trim())
                && !from.toString().toLowerCase().trim().startsWith("from")) {
            from.append(" from ", 0, 6);
        }

        if (StringUtils.isNoneBlank(where.toString().trim())
                && !where.toString().toLowerCase().trim().startsWith("where")) {
            where.append(" where ", 0, 7);
        }

        StringBuilder sql = new StringBuilder();
        if (StringUtils.isBlank(select) && this.select.length() > 0) {
            select = this.select.toString();
        }

        if (StringUtils.isNoneBlank(select.trim()) && !select.toLowerCase().trim().startsWith("select")) {
            select = "SELECT " + select;
        }

        Preconditions.checkArgument(StringUtils.isNoneBlank(select), " no select ");
        sql.append(select);
        Preconditions.checkArgument(this.from.length() > 0, " no from ");
        sql.append(this.from.toString()).append(this.where.toString());
        if (StringUtils.isNoneBlank(this.groupBy)) {
            if (!groupBy.toLowerCase().trim().startsWith("group")) {
                sql.append(" group by ");
            }
            sql.append(this.groupBy);
        }
        if (StringUtils.isNoneBlank(having)) {
            if (!having.toLowerCase().trim().startsWith("having")) {
                sql.append(" having ");
            }
            sql.append(this.having);
        }
        if (StringUtils.isNoneBlank(this.orderBy)) {
            if (!orderBy.toLowerCase().trim().startsWith("order")) {
                sql.append(" order by ");
            }
            sql.append(this.orderBy);
        }
        if (this.page != null) {
            sql.append(" limit :begin, :end");
            this.queryParams.put("begin", this.page.firstNum());
            this.queryParams.put("end", this.page.getPageSize());
        }
        return sql.toString();
    }

    public String toCountSql(String countSelect) {
        if (StringUtils.isBlank(countSelect)) {
            countSelect = "select count(1) ";
        }
        StringBuilder sql = new StringBuilder();
        sql.append(countSelect);
        Preconditions.checkArgument(this.from.length() > 0, " no from ");
        sql.append(this.from.toString()).append(this.where.toString());
        return sql.toString();
    }
}
