package com.jimmy.excel.exception;

import lombok.Data;

/**
 * @author : aiden
 * @ClassName :  FileException
 * @Description :
 * @date : 2019/3/14/014
 */
@Data
public class FileException extends RuntimeException {
    private String code;
    private String message;
    private Throwable cause;
    public FileException(Throwable cause) {
        super(cause);
        this.cause = cause;
        this.message = cause.getMessage();
    }
    public FileException(String message) {
        this(null, message);
    }

    public FileException(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
