/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.exception;

/**
 * @title NoColumnFoundException
 * @desc TODO
 * @author cxm
 * @date 2015年12月1日
 * @version 1.0
 */
public class NoColumnFoundException extends RuntimeException {

    private static final long serialVersionUID = -5985091083497610824L;

    private static final String ERROR_MSG_TEMPLATE = "can not found column by properties: %s !";

    private String properties;

    public NoColumnFoundException(String properties) {
        super(String.format(ERROR_MSG_TEMPLATE, properties));
        this.properties = properties;
    }

    public String getProperties() {
        return this.properties;
    }

}
