package com.microservice.messaging.broker.components.events.messages;


import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * IMQMessagingReturnsCallback
 * <p>
 * IMQMessagingReturnsCallback interface.
 * <p>
 * This interface specifies the requirements for the IMQMessagingReturnsCallback component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

public interface IMQMessagingReturnsCallback extends RabbitTemplate.ReturnsCallback {
}
