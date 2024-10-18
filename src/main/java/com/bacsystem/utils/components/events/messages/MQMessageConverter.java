package com.bacsystem.utils.components.events.messages;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.bacsystem.utils.components.base.MQBase;
import com.bacsystem.utils.components.events.IMQEventMessageService;
import com.bacsystem.utils.dto.MQEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.lang.NonNull;
import org.springframework.messaging.converter.MessageConversionException;

/**
 * MQMessageConverter
 * <p>
 * MQMessageConverter class.
 * <p>
 * This class specifies the requirements for the MQMessageConverter component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */


@Log4j2
public class MQMessageConverter extends MQBase implements IMQMessageConverter {

    private final ObjectMapper objectMapper;
    private final IMQEventMessageService eventMessageService;

    public MQMessageConverter(final IMQEventMessageService eventMessageService) {
        super(MQMessageConverter.class.getSimpleName());
        this.eventMessageService = eventMessageService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.registerModule(new Jdk8Module());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @NonNull
    @Override
    public Object fromMessage(@NonNull Message message, Object o) throws MessageConversionException {
        log.debug("[from] message: {}, class: {}", message, o.getClass());
        final ParameterizedTypeReference<?> type = (ParameterizedTypeReference<?>) o;
        log.debug("type {}", type.getType());
        try {
            return this.eventMessageService.event(message, type.getType());
        } catch (JsonProcessingException e) {
            throw new MessageConversionException(e.getMessage());
        }
    }

    @NonNull
    @Override
    public Message toMessage(@NonNull Object o, @NonNull MessageProperties messageProperties) throws MessageConversionException {
        log.debug("[to] object: {}, properties: {}", o, messageProperties);
        try {
            var message = this.objectMapper.writeValueAsString(o);
            final MQEvent<?> event = this.objectMapper.readValue(message, MQEvent.class);
            return this.eventMessageService.messageBroker(event, messageProperties.getMessageId(), messageProperties.getHeader("exchange"), messageProperties.getReceivedRoutingKey());
        } catch (JsonProcessingException e) {
            throw new MessageConversionException(e.getMessage());
        }
    }

    @NonNull
    @Override
    public Object fromMessage(@NonNull Message message) throws MessageConversionException {
        try {
            return eventMessageService.event(message, MQEvent.class);
        } catch (JsonProcessingException e) {
            throw new MessageConversionException(e.getMessage());
        }
    }
}

