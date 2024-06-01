package com.bxcode.services.contracts;

import com.bxcode.dto.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.lang.reflect.Type;

/**
 * IEventMapperService
 * <p>
 * IEventMapperService interface.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 1/06/2024
 */
public interface IEventMapperService extends IRegisterService {
    Message messageBroker(Event<?> event, String messageId, String exchange, String routingKey);

    Message messageBroker(Event<?> event, String messageId, String exchange, String routingKey, MessageProperties messageProperties);

    Event<?> event(Message message, Type type) throws JsonProcessingException;

    <T> Event<T> event(Event<?> event, Class<T> body) throws JsonProcessingException;
}
