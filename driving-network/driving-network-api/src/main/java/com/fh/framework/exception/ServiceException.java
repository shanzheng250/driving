package com.fh.framework.exception;

/**
 * @ClassName:ServiceException
 * @Description:
 * @Author: shanzheng
 * @Date: 2019/12/17 16:07
 * @Version:1.0
 **/
public class ServiceException extends RuntimeException {

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
