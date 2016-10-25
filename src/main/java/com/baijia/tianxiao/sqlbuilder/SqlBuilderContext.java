/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder;

import com.baijia.tianxiao.sqlbuilder.bean.SQLOperator;
import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @title SqlBuilderContext
 * @desc TODO
 * @author cxm
 * @date 2015年12月2日
 * @version 1.0
 */
@Data
public class SqlBuilderContext {
    private AtomicInteger conditionIndex = new AtomicInteger(0);

    private Map<String, String> fieldMapColumn;
    private Map<String, String> columnMapField;

    public SqlBuilderContext(Map<String, String> fieldMapColumn, Map<String, String> columnMapField) {
        super();
        this.fieldMapColumn = fieldMapColumn;
        this.columnMapField = columnMapField;
    }

    public String getVarName(String properties, SQLOperator operator) {
        Preconditions.checkArgument(StringUtils.isNoneBlank(properties), "properties can not be empty");
        Preconditions.checkNotNull(operator, "sql operator can not be null");
        properties =
            properties.replace('.', '_').replace(' ', '_').replace('(', '_').replace(')', '_').replace(',', '_');

        StringBuilder sb = new StringBuilder();
        sb.append(operator.name()).append("_").append(properties).append("_").append(conditionIndex.getAndIncrement());
        return sb.toString();
    }

}
