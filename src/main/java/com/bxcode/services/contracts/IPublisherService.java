package com.bxcode.services.contracts;

import com.bxcode.dto.Event;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * IPublisherService
 * <p>
 * IPublisherService interface.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 1/06/2024
 */
public interface IPublisherService {

    Boolean publish(String exchange, Event<?> event) throws JsonProcessingException;

    Boolean publish(String exchange, Event<?> event, boolean reply) throws JsonProcessingException;

    <T> Event<T> publishAndReceived(String exchange, Event<?> event, Class<T> body) throws JsonProcessingException;
}
