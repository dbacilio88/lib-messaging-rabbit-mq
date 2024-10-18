package com.bacsystem.utils.services;


import com.bacsystem.utils.components.annotations.MQDeclareBinding;
import com.bacsystem.utils.components.events.IMQEventMessageService;
import com.bacsystem.utils.services.dispatch.IMQConsumerDispatch;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

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
    String PARAM_NULL_MSG_EXCEPTION = "parameter %s is required, please set a value to this field";

    void register(Method method, Object bean);

    void executeBinding(String queue, MQDeclareBinding[] bindings);

    Consumer build(Channel channel, Method method, Object bean, Type type, String queueName, boolean automaticAck, IMQEventMessageService eventMessageService, IMQConsumerDispatch consumerDispatch);
}
