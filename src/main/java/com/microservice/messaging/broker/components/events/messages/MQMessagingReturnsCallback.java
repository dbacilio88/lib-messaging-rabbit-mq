package com.microservice.messaging.broker.components.events.messages;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.messaging.broker.components.base.MQBase;
import com.microservice.messaging.broker.components.events.IMQEventMessageService;
import com.microservice.messaging.broker.dto.MQEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.ReturnedMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * MQMessagingReturnsCallback
 * <p>
 * MQMessagingReturnsCallback class.
 * <p>
 * This class specifies the requirements for the MQMessagingReturnsCallback component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

@Log4j2
public class MQMessagingReturnsCallback extends MQBase implements IMQMessagingReturnsCallback {
    private final Method method;
    private final Object bean;
    private final IMQEventMessageService eventMessageService;

    public MQMessagingReturnsCallback(final Method method, final Object bean, final IMQEventMessageService eventMessageService) {
        super(MQMessagingReturnsCallback.class.getSimpleName());
        this.method = method;
        this.bean = bean;
        this.eventMessageService = eventMessageService;
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        var parameter = method.getParameters()[2];
        var type = parameter.getParameterizedType();
        try {
            final MQEvent<?> event = this.eventMessageService.event(returnedMessage.getMessage(), type);
            method.invoke(bean, returnedMessage.getReplyCode(), returnedMessage.getReplyText(), event);
        } catch (JsonProcessingException | InvocationTargetException | IllegalAccessException e) {
            log.error("Exception, msg: {}, trace: {}", e.getMessage(), e);
        }
    }
}
