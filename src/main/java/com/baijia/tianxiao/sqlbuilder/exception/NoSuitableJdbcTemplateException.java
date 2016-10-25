/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.exception;

import com.baijia.tianxiao.sqlbuilder.annotation.Entity;

/**
 * @title NoSuitableJdbcTemplateException
 * @desc TODO
 * @author cxm
 * @date 2015年12月2日
 * @version 1.0
 */
public class NoSuitableJdbcTemplateException extends RuntimeException {
    private static final long serialVersionUID = -824866428614019339L;

    private Class<?> poClass;

    private static final String ERROR_MSG_TEMPLATE = " can't found suitable jdbctemplate for po class: %s, entity: %s";

    public NoSuitableJdbcTemplateException(Class<?> poClass) {
        super(String.format(ERROR_MSG_TEMPLATE, poClass.getName(), poClass.getAnnotation(Entity.class)));
        this.poClass = poClass;
    }

    public Class<?> getPoClass() {
        return this.poClass;
    }

}
