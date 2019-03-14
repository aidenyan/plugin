package com.jimmy.excel.exception;

/**
 * @author : aiden
 * @ClassName :  ReadIOException
 * @Description :读取流错误信息
 * @date : 2019/3/14/014
 */
public class ReadIOException extends RuntimeException {
    private String code;
    private String message;
    private Throwable cause;

    public ReadIOException(Throwable cause) {
        this.cause = cause;
        this.message = cause.getMessage();
    }

    public ReadIOException(String message) {
        this(null, message);
    }


    public ReadIOException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
