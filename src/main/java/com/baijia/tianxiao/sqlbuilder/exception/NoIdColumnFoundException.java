/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.exception;

/**
 * @title NoIdColumnFoundException
 * @desc TODO
 * @author cxm
 * @date 2015年12月12日
 * @version 1.0
 */
public class NoIdColumnFoundException extends RuntimeException {

    private static final long serialVersionUID = -3559178417889883206L;

    private Class<?> entityClass;

    public NoIdColumnFoundException(Class<?> entityClass) {
        super("can not found id column in entity class:" + entityClass);
        this.entityClass = entityClass;
    }

    public Class<?> getEntityClass() {
        return this.entityClass;
    }

}
