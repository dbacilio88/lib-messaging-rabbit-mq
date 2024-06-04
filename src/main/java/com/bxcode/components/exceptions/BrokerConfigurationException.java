package com.bxcode.components.exceptions;

/**
 * BrokerConfigurationException
 * <p>
 * BrokerConfigurationException class.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 1/06/2024
 */
public class BrokerConfigurationException extends RuntimeException {

    public BrokerConfigurationException(String message) {
        super(message);
    }

    public BrokerConfigurationException(Throwable cause) {
        super(cause);
    }

    public BrokerConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    protected BrokerConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BrokerConfigurationException() {
        super();
    }
}


