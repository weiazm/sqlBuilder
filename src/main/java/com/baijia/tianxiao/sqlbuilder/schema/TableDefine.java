/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.schema;

import com.google.common.collect.Lists;

import java.util.List;

import lombok.Data;

/**
 * @title TableDefine
 * @desc TODO
 * @author cxm
 * @date 2015年8月10日
 * @version 1.0
 */
@Data
public class TableDefine {

    private String name;

    private String catalog;

    private boolean dynamicInsert;

    private boolean dynamicUpdate;

    private String dataSourceBeanName;

    private List<ColumnDefine> columnDefines;

    private Class<?> clazz;

    public TableDefine(Class<?> clazz) {
        this.clazz = clazz;
    }

    public TableDefine(String name, String catalog) {
        this.name = name;
        this.catalog = catalog;
    }

    public TableDefine(String name) {
        this.name = name;
    }

    /**
     * @return the columnDefines
     */
    public List<ColumnDefine> getColumnDefines() {
        if (this.columnDefines == null) {
            this.columnDefines = Lists.newArrayList();
        }
        return columnDefines;
    }

    /**
     * @param columnDefines the columnDefines to set
     */
    public void setColumnDefines(List<ColumnDefine> columnDefines) {

        this.columnDefines = columnDefines;
    }

}
