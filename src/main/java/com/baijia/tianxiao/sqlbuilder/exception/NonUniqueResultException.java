package com.baijia.tianxiao.sqlbuilder.exception;

/**
 * @author cxm
 * @version 1.0
 * @title NonUniqueResultException
 * @desc TODO
 * @date 2015年9月7日
 */
public class NonUniqueResultException extends RuntimeException {

    private static final long serialVersionUID = 7142092304563926529L;

    public NonUniqueResultException() {
        super();
    }

    public NonUniqueResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonUniqueResultException(String message) {

        super(message);
    }

    public NonUniqueResultException(Throwable cause) {
        super(cause);
    }

}
