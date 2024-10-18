package com.bacsystem.utils.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.bacsystem.utils.dto.MQEvent;

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
    boolean producer(String exchange, MQEvent<?> event, boolean retry) throws JsonProcessingException;
}
