/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.util;

import com.baijia.tianxiao.sqlbuilder.SqlBuilderContext;
import com.baijia.tianxiao.sqlbuilder.annotation.Column;
import com.baijia.tianxiao.sqlbuilder.annotation.Id;
import com.baijia.tianxiao.sqlbuilder.bean.SaveInfo;
import com.baijia.tianxiao.sqlbuilder.exception.NoColumnFoundException;
import com.baijia.tianxiao.sqlbuilder.schema.ColumnDefine;
import com.baijia.tianxiao.sqlbuilder.schema.TableDefine;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @title ColumnUtil
 * @desc TODO
 * @author cxm
 * @date 2015年8月10日
 * @version 1.0
 */
@Slf4j
public class ColumnUtil {

    public static ColumnDefine lookupColumn(TableDefine tableDefine, String properties) {
        ColumnDefine columnFix = null;
        ColumnDefine fieldFix = null;

        for (ColumnDefine column : tableDefine.getColumnDefines()) {
            if (column.getFieldName().equals(properties)) {
                if (fieldFix != null) {
                    throw new UnsupportedOperationException(
                        "properties is exist in column:" + fieldFix + " and column:" + column);
                }
                fieldFix = column;
            } else if (column.getColumnName().equals(properties)) {
                columnFix = column;
            }
        }
        ColumnDefine column = columnFix;
        if (fieldFix != null) {
            column = fieldFix;
        }
        return column;
    }

    public static String getColumnName(String properties, Map<String, String> fieldMapColumn,
        Map<String, String> columnMapField) {
        String columnName = fieldMapColumn.get(properties);
        if (columnName == null) {
            if (columnMapField.containsKey(properties)) {
                columnName = properties;
            }
        }
        if (StringUtils.isBlank(columnName)) {
            throw new NoColumnFoundException(properties);
        }
        return columnName;
    }

    public static String getColumnName(String properties, SqlBuilderContext context) {
        return getColumnName(properties, context.getFieldMapColumn(), context.getColumnMapField());
    }

    public static SaveInfo getSaveInfoFromObjs(Object obj, boolean saveNull, boolean useDefaultVal) {
        Preconditions.checkNotNull(obj, "save obj is null");
        SaveInfo result = new SaveInfo();
        for (Field field : obj.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (!field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(Id.class)) {
                    continue;
                }

                if (!saveNull && value == null) {
                    Column anno = field.getAnnotation(Column.class);
                    if (anno != null && StringUtils.isNoneBlank(anno.defaultVal()) && useDefaultVal) {
                        if (field.getType().isAssignableFrom(Date.class) && StringUtils.isNumeric(anno.defaultVal())) {
                            value = Integer.parseInt(anno.defaultVal());
                        } else {
                            value = anno.defaultVal();
                        }
                    } else {
                        continue;
                    }
                }

                result.getParamMap().put(field.getName(), value);
                result.getColumns().add(field.getName());
            } catch (IllegalArgumentException | IllegalAccessException e) {
                log.warn("get field:{} from obj :{} catch error:{}", field, obj, e);
            }
        }
        return result;
    }

    public static void main(String[] args) {

    }

    public static <T> Map<String, Object> collectBatchInsertValue(Collection<T> values, boolean isUpdate) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }
        Map<String, Object> paramsMap = Maps.newHashMap();
        int i = 0;
        for (T value : values) {
            for (Field field : value.getClass().getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object fieldValue = field.get(value);
                    String name = field.getName();
                    if (i > 0) {
                        name += "_" + i;
                    }
                    // if (!isUpdate && fieldValue == null) {
                    // fieldValue = getDefaultValueFromColumn(field.getAnnotation(Column.class), field);
                    // }
                    paramsMap.put(name, fieldValue);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    log.warn("get field:{} from obj :{} catch error:{}", field, value, e);
                }
            }
            i++;
        }
        return paramsMap;
    }

    // private static Object getDefaultValueFromColumn(Column column, Field field) {
    // if (column == null) {
    // return null;
    // }
    // if (StringUtils.isNoneBlank(column.defaultVal())) {
    // return column.defaultVal();
    // } else {
    // if (field.getType().equals(String.class)) {
    // return "";
    // } else {
    // try {
    // return field.getType().newInstance();
    // } catch (InstantiationException | IllegalAccessException e) {
    // log.error("new default catch error:", e);
    // throw new UnsupportedOperationException("column is not support type:" + field.getType());
    // }
    // }
    // }
    // }

}
