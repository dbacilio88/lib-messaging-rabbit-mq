package com.microservice.messaging.broker.services;


import com.microservice.messaging.broker.components.annotations.MQDeclareBinding;
import com.rabbitmq.client.AMQP;

import java.util.List;

/**
 * IConsumerService
 * <p>
 * IConsumerService interface.
 * <p>
 * This interface specifies the requirements for the IConsumerService component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

public interface IConsumerService {
    void register(Object bean, String name);
    List<AMQP.Queue.BindOk> executeBinding(String queue, MQDeclareBinding[] bindings);
}
