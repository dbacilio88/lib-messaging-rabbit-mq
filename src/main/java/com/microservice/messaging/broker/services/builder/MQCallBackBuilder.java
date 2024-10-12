package com.microservice.messaging.broker.services.builder;


import com.microservice.messaging.broker.components.base.MQBase;
import com.microservice.messaging.broker.components.events.IMQEventMessageService;
import com.microservice.messaging.broker.components.events.messages.MQMessagingReturnsCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * MQCallBackBuilder
 * <p>
 * MQCallBackBuilder class.
 * <p>
 * This class specifies the requirements for the MQCallBackBuilder component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

@Service
public class MQCallBackBuilder extends MQBase implements IMQCallBackBuilder {
    private final IMQEventMessageService eventMessageService;

    public MQCallBackBuilder(IMQEventMessageService eventMessageService) {
        super(MQCallBackBuilder.class.getSimpleName());
        this.eventMessageService = eventMessageService;
    }

    @Override
    public RabbitTemplate.ReturnsCallback build(Method method, Object bean) {
        return new MQMessagingReturnsCallback(method, bean, eventMessageService);
    }
}
