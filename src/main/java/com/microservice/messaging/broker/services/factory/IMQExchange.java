package com.microservice.messaging.broker.services.factory;


import com.microservice.messaging.broker.components.annotations.MQDeclareExchange;

/**
 * IMQExchange
 * <p>
 * IMQExchange interface.
 * <p>
 * This interface specifies the requirements for the IMQExchange component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 11/10/2024
 */

public interface IMQExchange {
    void register(String exchange, MQDeclareExchange annotation);
}
