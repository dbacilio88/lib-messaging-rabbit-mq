package com.microservice.messaging.broker.components.exceptions;


import com.microservice.messaging.broker.components.utils.MQUtility;

import java.io.Serial;

/**
 * MQBrokerException
 * <p>
 * MQBrokerException class.
 * <p>
 * This class specifies the requirements for the MQBrokerException component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */


public class MQBrokerException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7630099897765025280L;

    public MQBrokerException(String message) {
        super(message);
    }

    public MQBrokerException(String message, int replyCode) {
        super(MQUtility.formatMessage("Event could not be publish. message: %s, code: %s", message, replyCode));
    }

    public MQBrokerException() {
        super();
    }

    public MQBrokerException(String message, Throwable cause) {
        super(message, cause);
    }

    public MQBrokerException(Throwable cause) {
        super(cause);
    }

    protected MQBrokerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

