package com.microservice.messaging.broker.services.factory;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.lang.reflect.Method;

/**
 * IMQCallBack
 * <p>
 * IMQCallBack interface.
 * <p>
 * This interface specifies the requirements for the IMQCallBack component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

public interface IMQCallBack {
    void register(Method method, Object bean);

    void execute(Message message, int code, String text, String exchange, String routingKey);

    RabbitTemplate.ReturnsCallback build(Method method, Object bean);
}
