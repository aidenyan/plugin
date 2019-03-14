package com.jimmy.excel.exception;

import lombok.Data;

/**
 * @author : aiden
 * @ClassName :  StreamException
 * @Description :
 * @date : 2019/3/14/014
 */
@Data
public class StreamException extends RuntimeException {
    private String code;
    private String message;
    private Throwable cause;

    public StreamException(Throwable cause) {
        super(cause);
        this.cause = cause;
        this.message = cause.getMessage();
    }

    public StreamException(String message) {
        this(null, message);
    }

    public StreamException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
