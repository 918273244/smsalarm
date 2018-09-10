package com.xsxx.exception;

public class ServiceException extends RuntimeException  {

    private int code = 0;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(int code,String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(int code,String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public Throwable fillInStackTrace() {
        return this;
    }

    public int getCode() {
        return code;
    }

}
