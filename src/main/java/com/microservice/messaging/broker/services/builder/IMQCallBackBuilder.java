package com.microservice.messaging.broker.services.builder;


import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.lang.reflect.Method;

/**
 * IMQCallBackBuilder
 * <p>
 * IMQCallBackBuilder interface.
 * <p>
 * This interface specifies the requirements for the IMQCallBackBuilder component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

public interface IMQCallBackBuilder {
    RabbitTemplate.ReturnsCallback build(Method method, Object bean);
}
