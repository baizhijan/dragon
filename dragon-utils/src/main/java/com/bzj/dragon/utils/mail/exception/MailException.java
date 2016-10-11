package com.bzj.dragon.utils.mail.exception;

/**
 * 邮件异常
 * User:aaronbai@tcl.com
 * Date:2016-10-11
 * Time:11:00
 */
public class MailException extends RuntimeException{

    public MailException() {
    }

    public MailException(String message) {
        super(message);
    }

    public MailException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailException(Throwable cause) {
        super(cause);
    }

    public MailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
