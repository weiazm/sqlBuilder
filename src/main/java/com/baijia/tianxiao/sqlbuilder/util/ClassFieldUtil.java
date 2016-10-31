/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.util;

import com.baijia.tianxiao.sqlbuilder.annotation.*;
import com.baijia.tianxiao.sqlbuilder.schema.ColumnDefine;
import com.baijia.tianxiao.sqlbuilder.schema.TableDefine;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author cxm
 * @version 1.0
 * @title ClassFieldUtil
 * @desc 根据类的注解读取数据库配置, 待实现
 * @date 2015年8月7日
 */
public class ClassFieldUtil {

    public static TableDefine readFieldColumnFromEntityCalss(Class<?> clazz) {
        TableDefine table = null;
        if (clazz.isAnnotationPresent(Entity.class)) {
            Entity entity = clazz.getAnnotation(Entity.class);
            table = new TableDefine(clazz);
            if (StringUtils.isNoneBlank(entity.name())) {
                table.setName(entity.name());
            } else {
                table.setName(VariableChangeUtils.camelToUnderline(clazz.getSimpleName()));
            }
            table.setDynamicInsert(entity.dynamicInsert());
            table.setDynamicUpdate(entity.dynamicUpdate());
            table.setDataSourceBeanName(entity.dataSourceBeanName());
        }
        if (clazz.isAnnotationPresent(Table.class)) {
            Table tableAnno = clazz.getAnnotation(Table.class);
            if (table == null) {
                table = new TableDefine(clazz);
            }
            if (StringUtils.isNoneBlank(tableAnno.name())) {
                table.setName(tableAnno.name());
            }
            if (StringUtils.isNoneBlank(tableAnno.catalog())) {
                table.setCatalog(tableAnno.catalog());
            }
        }
        if (table != null && StringUtils.isBlank(table.getName())) {
            char[] nameArr = clazz.getName().toCharArray();
            nameArr[0] = (char) (nameArr[3] + 32);
            table.setName(new String(nameArr));
        }

        getColumnInfoFromMethod(table, clazz.getMethods());
        getColumnInfoFromFields(table, clazz.getDeclaredFields());

        return table;
    }

    private static void getColumnInfoFromFields(TableDefine table, Field[] fields) {
        String columnName = null;
        for (Field field : fields) {
            ColumnDefine columnDefine = null;
            boolean hasAnnotation = false;
            columnDefine = new ColumnDefine(field.getName());
            if (field.isAnnotationPresent(Column.class)) {
                columnName = field.getAnnotation(Column.class).name();
                if (StringUtils.isNoneBlank(columnName)) {
                    columnDefine.setColumnName(columnName);
                }
                hasAnnotation = true;
            }
            if (field.isAnnotationPresent(Id.class)) {
                columnDefine.setIdColumn(true);
                hasAnnotation = true;
                if (field.isAnnotationPresent(GeneratedValue.class)) {
                    columnDefine.setAuto(true);
                }
            }
            if (columnDefine != null && hasAnnotation) {
                table.getColumnDefines().add(columnDefine);
            }
        }
    }

    private static void getColumnInfoFromMethod(TableDefine table, Method[] methods) {
        String columnName = null;
        for (Method method : methods) {
            ColumnDefine columnDefine = null;
            boolean hasAnnotation = false;
            if (method.isAnnotationPresent(Column.class)) {
                columnDefine = new ColumnDefine(getFieldNameFromGetOrSetMethodName(method.getName()));
                columnName = method.getAnnotation(Column.class).name();
                if (StringUtils.isNoneBlank(columnName)) {
                    columnDefine.setColumnName(columnName);
                }
                hasAnnotation = true;
            }
            if (method.isAnnotationPresent(Id.class)) {
                if (columnDefine == null) {
                    columnDefine = new ColumnDefine(getFieldNameFromGetOrSetMethodName(method.getName()), true);
                } else {
                    columnDefine.setIdColumn(true);
                }
                if (method.isAnnotationPresent(GeneratedValue.class)) {
                    columnDefine.setAuto(true);
                }
                hasAnnotation = true;
            }
            if (columnDefine != null && hasAnnotation) {
                table.getColumnDefines().add(columnDefine);
            }
        }
    }

    public static String getFieldNameFromGetOrSetMethodName(String methodName) {
        if (StringUtils.isBlank(methodName) || (!methodName.startsWith("get") && !methodName.startsWith("set"))
                || methodName.length() <= 3) {
            throw new UnsupportedOperationException("unsupport method:" + methodName);
        }
        char[] nameArr = methodName.toCharArray();
        nameArr[3] = (char) (nameArr[3] + 32);

        return new String(Arrays.copyOfRange(nameArr, 3, nameArr.length));
    }

    public static String lowerIndexString(String source, int index) {
        checkIndex(source, index);
        char[] nameArr = source.toCharArray();
        nameArr[index] = (char) (nameArr[index] + 32);
        return new String(nameArr);
    }

    /**
     * @param source
     * @param index
     */
    private static void checkIndex(String source, int index) {
        if (StringUtils.isBlank(source)) {
            throw new IllegalArgumentException("source string can not be empty");
        }
        if (index < 0) {
            throw new IllegalArgumentException("index can not be Negative.");
        }
        if (source.length() < index + 1) {
            throw new IndexOutOfBoundsException("source length:" + source.length() + " change index:" + index);
        }
    }

    public static String uperIndexString(String source, int index) {
        checkIndex(source, index);
        char[] nameArr = source.toCharArray();
        nameArr[index] = (char) (nameArr[index] - 32);
        return new String(nameArr);
    }

}
