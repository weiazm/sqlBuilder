/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.schema;

import com.baijia.tianxiao.sqlbuilder.util.VariableChangeUtils;
import lombok.Data;

/**
 * @title ColumnDefine
 * @desc TODO
 * @author cxm
 * @date 2015年8月10日
 */
@Data
public class ColumnDefine {

    private String fieldName;

    private String columnName;

    private boolean isIdColumn;

    private boolean isAuto;

    public ColumnDefine(String fieldName, String columnName, boolean isIdColumn) {
        this.fieldName = fieldName;
        this.columnName = columnName;
        this.isIdColumn = isIdColumn;
    }

    public ColumnDefine(String fieldName, String columnName) {
        this(fieldName, columnName, false);
    }

    public ColumnDefine(String fieldName) {
        this(fieldName, VariableChangeUtils.camelToUnderline(fieldName));
    }

    public ColumnDefine(String fieldName, boolean isIdColumn) {
        this(fieldName, fieldName, isIdColumn);
    }

}
