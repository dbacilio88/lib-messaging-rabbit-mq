package com.bxcode.models.implementations;

import com.bxcode.dto.Event;
import com.bxcode.models.contract.IRabbitConverter;
import com.bxcode.services.contracts.IEventMapperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.core.ParameterizedTypeReference;

/**
 * RabbitConverter
 * <p>
 * RabbitConverter class.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 1/06/2024
 */
@Log4j2
public class RabbitConverter implements IRabbitConverter {

    private final IEventMapperService mapperService;
    private final ObjectMapper objectMapper;

    public RabbitConverter(final IEventMapperService mapperService) {
        this.mapperService = mapperService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new Jdk8Module());
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Object fromMessage(Message message, Object o) throws MessageConversionException {
        log.debug("[from] message: {}, class: {}", message, o.getClass());
        final ParameterizedTypeReference<?> type = (ParameterizedTypeReference<?>) o;
        log.debug("type {}", type.getType());
        try {
            return mapperService.event(message, type.getType());
        } catch (JsonProcessingException e) {
            throw new MessageConversionException(e.getMessage());
        }
    }

    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        log.debug("[to] object: {}, properties: {}", o, messageProperties);
        try {
            final String message = this.objectMapper.writeValueAsString(o);
            final Event<?> event = this.objectMapper.readValue(message, Event.class);
            return mapperService.messageBroker(event,
                    messageProperties.getMessageId(),
                    messageProperties.getHeader("exchange"),
                    messageProperties.getReceivedRoutingKey());
        } catch (JsonProcessingException e) {
            throw new MessageConversionException(e.getMessage());
        }
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        try {
            log.debug("[from] message: {}", message);
            return mapperService.event(message, Event.class);
        } catch (JsonProcessingException e) {
            throw new MessageConversionException(e.getMessage());
        }
    }
}