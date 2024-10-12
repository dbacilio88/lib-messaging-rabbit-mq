package com.microservice.messaging.broker.services.builder;


import com.microservice.messaging.broker.components.events.IMQEventMessageService;
import com.microservice.messaging.broker.services.dispatch.IMQConsumerDispatch;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * IMQConsumerBuilder
 * <p>
 * IMQConsumerBuilder interface.
 * <p>
 * This interface specifies the requirements for the IMQConsumerBuilder component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

public interface IMQConsumerBuilder {
    String PARAM_NULL_MSG_EXCEPTION = "parameter %s is required, please set a value to this field";

    Type validateType(Type type);

    Method validateMethod(Method method);

    String validateQueueName(String queueName);

    Object validateBean(Object bean);

    Channel validateChannel(Channel channel);

    Consumer build(Channel channel, Method method, Object bean, Type type, String queueName, boolean automaticAck, IMQEventMessageService eventMessageService, IMQConsumerDispatch consumerDispatch);
}
