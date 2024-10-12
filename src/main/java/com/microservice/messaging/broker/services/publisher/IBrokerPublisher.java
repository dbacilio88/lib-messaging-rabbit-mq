package com.microservice.messaging.broker.services.publisher;


import com.microservice.messaging.broker.dto.MQEvent;

/**
 * IBrokerPublisher
 * <p>
 * IBrokerPublisher interface.
 * <p>
 * This interface specifies the requirements for the IBrokerPublisher component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

public interface IBrokerPublisher {
    void failedHandle();

    void raiseEvent(MQEvent<?> event);
}
