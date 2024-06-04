package com.bxcode.components.exceptions;

import com.bxcode.components.helpers.StringFormat;

/**
 * PublishEventException
 * <p>
 * PublishEventException class.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 4/06/2024
 */
public class PublishEventException extends RuntimeException {
    private static final long serialVersionUID = 1455454112312393692L;

    public PublishEventException() {
        super();
    }

    public PublishEventException(String message) {
        super(message);
    }

    public PublishEventException(String message, Throwable cause) {
        super(message, cause);
    }

    public PublishEventException(Throwable cause) {
        super(cause);
    }

    protected PublishEventException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public PublishEventException(String message, int replyCode) {
        super(StringFormat.formatMessage("Event could not be publish. message: %s, code: %s", message, replyCode));
    }
}


