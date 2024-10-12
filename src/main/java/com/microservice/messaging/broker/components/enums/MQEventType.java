package com.microservice.messaging.broker.components.enums;


/**
 * MQEventType
 * <p>
 * MQEventType enum.
 * <p>
 * This enum specifies the requirements for the MQEventType component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */


public enum MQEventType {
    METADATA,
    ERROR,
    NOTIFICATION,
    COMMAND,
    SERVICE
}
