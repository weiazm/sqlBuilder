package com.baijia.tianxiao.sqlbuilder.exception;

/**
 * @title NonUniqueResultException
 * @desc TODO
 * @author cxm
 * @date 2015年9月7日
 * @version 1.0
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
