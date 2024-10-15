package com.microservice.messaging.broker.components.events.messages.implementations;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microservice.messaging.broker.components.base.MQBase;
import com.microservice.messaging.broker.components.events.IMQEventMessageService;
import com.microservice.messaging.broker.components.events.messages.contracts.IMQMessagingConverter;
import com.microservice.messaging.broker.components.exceptions.MQBrokerException;
import com.microservice.messaging.broker.dto.MQEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.lang.NonNull;
import org.springframework.messaging.converter.MessageConversionException;

/**
 * MQMessagingConverter
 * <p>
 * MQMessagingConverter class.
 * <p>
 * This class specifies the requirements for the MQMessagingConverter component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */


@Log4j2
public class MQMessagingConverter extends MQBase implements IMQMessagingConverter {

    private final IMQEventMessageService eventMessageService;
    private final ObjectMapper objectMapper;

    public MQMessagingConverter(final IMQEventMessageService eventMessageService) {
        super(MQMessagingConverter.class.getSimpleName());
        this.eventMessageService = eventMessageService;
        objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new Jdk8Module());
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @NonNull
    @Override
    public Object fromMessage(@NonNull Message message, Object instance) throws MessageConversionException {
        log.debug("[from] message: {}, class:{}", message, instance.getClass());
        final ParameterizedTypeReference<?> typeReference = (ParameterizedTypeReference<?>) instance;
        log.debug("type: {}", typeReference.getType());
        try {
            return this.eventMessageService.event(message, typeReference.getType());
        } catch (JsonProcessingException e) {
            log.error("Exception, msg: {}, trace: {}", e.getMessage(), e);
            throw new MQBrokerException(e);
        }
    }

    @NonNull
    @Override
    public Message toMessage(@NonNull Object instance, @NonNull MessageProperties messageProperties) throws MessageConversionException {
        log.debug("[to] object: {}, prop:{}", instance, messageProperties);
        try {
            final String jsonString = this.objectMapper.writeValueAsString(instance);
            final MQEvent<?> event = this.objectMapper.convertValue(jsonString, MQEvent.class);
            return this.eventMessageService.messageBroker(event,
                    messageProperties.getMessageId(),
                    messageProperties.getHeader("exchange"),
                    messageProperties.getReceivedRoutingKey());
        } catch (JsonProcessingException e) {
            log.error("Exception, msg: {}, trace: {}", e.getMessage(), e);
            throw new MQBrokerException(e);
        }
    }

    @NonNull
    @Override
    public Object fromMessage(@NonNull Message message) throws MessageConversionException {
        log.debug("[from] message: {}", message);
        try {
            return this.eventMessageService.event(message, MQEvent.class);
        } catch (JsonProcessingException e) {
            log.error("Exception, msg: {}, trace: {}", e.getMessage(), e);
            throw new MQBrokerException(e);
        }
    }
}
