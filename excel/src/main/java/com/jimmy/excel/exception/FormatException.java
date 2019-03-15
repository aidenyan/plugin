package com.jimmy.excel.exception;

import lombok.Data;

/**
 * @author : aiden
 * @ClassName :  FormatException
 * @Description :
 * @date : 2019/3/15/015
 */
@Data
public class FormatException extends RuntimeException {
    private String code;
    private String message;
    private Throwable cause;

    public FormatException(Throwable cause) {
        super(cause);
        this.cause = cause;
        this.message = cause.getMessage();
    }

    public FormatException(String message) {
        this(null, message);
    }

    public FormatException(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
