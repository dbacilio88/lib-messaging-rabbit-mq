package com.microservice.messaging.broker.components.events;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.messaging.broker.dto.MQEvent;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.lang.reflect.Type;

/**
 * IMQEventMessageService
 * <p>
 * IMQEventMessageService interface.
 * <p>
 * This interface specifies the requirements for the IMQEventMessageService component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

public interface IMQEventMessageService {
    Message messageBroker(MQEvent<?> event, String messageId, String exchange, String routingKey) throws JsonProcessingException;

    Message messageBroker(MQEvent<?> event, String messageId, String exchange, String routingKey, MessageProperties messageProperties) throws JsonProcessingException;

    MQEvent<?> event(Message message, Type type) throws JsonProcessingException;

    <T> MQEvent<T> event(MQEvent<?> event, Class<T> body) throws JsonProcessingException;
}

