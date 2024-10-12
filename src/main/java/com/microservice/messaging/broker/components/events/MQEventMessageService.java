package com.microservice.messaging.broker.components.events;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microservice.messaging.broker.components.base.MQBase;
import com.microservice.messaging.broker.components.events.messages.MQEventMessage;
import com.microservice.messaging.broker.components.exceptions.MQBrokerException;
import com.microservice.messaging.broker.dto.MQEvent;
import com.microservice.messaging.broker.dto.MQTrailer;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

/**
 * MQEventMessageService
 * <p>
 * MQEventMessageService class.
 * <p>
 * This class specifies the requirements for the MQEventMessageService component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */
@Log4j2
@Component
public class MQEventMessageService extends MQBase implements IMQEventMessageService {
    private final ObjectMapper objectMapper;

    public MQEventMessageService() {
        super(MQEventMessageService.class.getSimpleName());
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.registerModule(new Jdk8Module());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Message messageBroker(MQEvent<?> event, String messageId, String exchange, String routingKey) throws JsonProcessingException {
        var trailer = Optional.ofNullable(event.getMQTrailer()).orElse(new MQTrailer());

        trailer.setSent(LocalDateTime.now());
        trailer.setLastAgentId(event.getRoutingKey().getOrigin());
        trailer.setReceived(LocalDateTime.now());
        trailer.setSize(event.getData().toString().getBytes(StandardCharsets.UTF_8).length);
        event.setMQTrailer(trailer);

        var eventMessage = new MQEventMessage(encryptBody(event), event.getRoutingKey().getOrigin());
        eventMessage.getMessageProperties().setMessageId(messageId);
        eventMessage.getMessageProperties().setHeader("request-id", messageId);
        eventMessage.getMessageProperties().setHeader("created", LocalDateTime.now().toString());
        final MessagePostProcessor messagePostProcessor = message -> {
            log.debug("postProcessorMessage 1: {}", message);
            return message;
        };
        return messagePostProcessor.postProcessMessage(eventMessage, null);
    }

    @Override
    public Message messageBroker(MQEvent<?> event, String messageId, String exchange, String routingKey, MessageProperties messageProperties) throws JsonProcessingException {
        var trailer = Optional.ofNullable(event.getMQTrailer()).orElse(new MQTrailer());

        trailer.setSent(LocalDateTime.now());
        trailer.setLastAgentId(event.getRoutingKey().getOrigin());

        Optional.ofNullable(event.getData()).ifPresent(data -> {
            try {
                var json = this.objectMapper.writeValueAsString(data);
                trailer.setSize(json.getBytes(StandardCharsets.UTF_8).length);
            } catch (JsonProcessingException e) {
                throw new MQBrokerException(e.getMessage());
            }
        });

        event.setMQTrailer(trailer);

        var eventMessage = new MQEventMessage(encryptBody(event), event.getRoutingKey().getOrigin());
        eventMessage.getMessageProperties().setMessageId(messageId);
        eventMessage.getMessageProperties().setHeader("request-id", messageId);
        eventMessage.getMessageProperties().setHeader("created", LocalDateTime.now().toString());
        eventMessage.getMessageProperties().setAppId(event.getRoutingKey().getOrigin());
        final MessagePostProcessor messagePostProcessor = message -> {
            log.debug("postProcessorMessage: {}", message);
            return message;
        };
        return messagePostProcessor.postProcessMessage(eventMessage, null);
    }

    @Override
    public MQEvent<?> event(Message message, Type type) throws JsonProcessingException {
        log.debug("mapping msg: {}, to Type: {}", message, type);
        var data = new String(Base64.getDecoder().decode(message.getBody()), StandardCharsets.UTF_8);
        log.debug("data: {}, to Type: {}", data, type);
        JavaType javaType = buildJavaTypeFromType(type);
        final MQEvent<?> event = objectMapper.readValue(data, javaType);
        event.setProperties(message.getMessageProperties());
        return event;
    }

    @Override
    public <T> MQEvent<T> event(MQEvent<?> event, Class<T> body) throws JsonProcessingException {
        final var data = objectMapper.writeValueAsString(event.getData());
        final var out = objectMapper.readValue(data, body);
        final MQEvent<T> eventOut = new MQEvent<>();
        eventOut.setMetadata(event.getMetadata());
        eventOut.setRoutingKey(event.getRoutingKey());
        eventOut.setHeaders(event.getHeaders());
        eventOut.setMQTrailer(event.getMQTrailer());
        eventOut.setProperties(event.getProperties());
        eventOut.setData(out);
        eventOut.setCode(event.getCode());
        eventOut.setMessage(event.getMessage());
        return eventOut;
    }


    public <T> byte[] encryptBody(final T body) throws JsonProcessingException {
        final String bodyTmp = this.objectMapper.writeValueAsString(body);
        return Base64.getEncoder().encode(bodyTmp.getBytes(StandardCharsets.UTF_8));
    }

    public JavaType buildJavaTypeFromType(Type type) {
        return this.objectMapper.getTypeFactory().constructType(type);
    }
}
