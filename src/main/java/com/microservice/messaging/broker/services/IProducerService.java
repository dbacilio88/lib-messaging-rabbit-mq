package com.microservice.messaging.broker.services;


import com.microservice.messaging.broker.dto.MQEvent;

/**
 * IProducerService
 * <p>
 * IProducerService interface.
 * <p>
 * This interface specifies the requirements for the IProducerService component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

public interface IProducerService {
    boolean producer(String exchange, MQEvent<?> event);

    boolean producer(String exchange, MQEvent<?> event, boolean retry);

    <T> MQEvent<T> producerAndConsumer(String exchange, MQEvent<T> event, Class<T> clazz);
}
