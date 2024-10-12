package com.microservice.messaging.broker.services.factory;


import com.microservice.messaging.broker.components.annotations.MQDeclareBinding;
import com.microservice.messaging.broker.components.annotations.MQDeclareQueue;

/**
 * IMQBinding
 * <p>
 * IMQBinding interface.
 * <p>
 * This interface specifies the requirements for the IMQBinding component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 11/10/2024
 */

public interface IMQBinding {
    void register(String binding, MQDeclareBinding annotation);
}
