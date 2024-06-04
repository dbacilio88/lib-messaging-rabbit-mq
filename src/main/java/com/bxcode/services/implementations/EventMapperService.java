package com.bxcode.services.implementations;

import com.bxcode.components.exceptions.ParameterException;
import com.bxcode.dto.Event;
import com.bxcode.dto.EventMessage;
import com.bxcode.dto.Trailer;
import com.bxcode.services.contracts.IEventMapperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * EventMapperService
 * <p>
 * EventMapperService class.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 4/06/2024
 */
@Log4j2
@Service
public class EventMapperService implements IEventMapperService {
    private final ObjectMapper objectMapper;

    public EventMapperService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new Jdk8Module());
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        log.debug("EventMapperService loaded successfully");
    }

    @Override
    public Message messageBroker(Event<?> event, String messageId, String exchange, String routingKey) throws JsonProcessingException {
        final Trailer trailer = Optional.ofNullable(event.getTrailer()).orElse(new Trailer());
        trailer.setSent(LocalDateTime.now());
        trailer.setLastAgentId(event.getRoutingKey().getOrigin());
        trailer.setDataSize(event.getData().toString().getBytes(StandardCharsets.UTF_8).length);
        event.setTrailer(trailer);

        final Message eventMessage = new EventMessage(encryptBody(event), event.getRoutingKey().getOrigin());
        eventMessage.getMessageProperties().setMessageId(messageId);
        eventMessage.getMessageProperties().setHeader("request-id", messageId);
        eventMessage.getMessageProperties().setHeader("created", LocalDateTime.now());
        final MessagePostProcessor messagePostProcessor = message -> {
            log.debug("postProcessorMessage: {}", message);
            return message;
        };
        return messagePostProcessor.postProcessMessage(eventMessage, null);
    }

    @Override
    public Message messageBroker(Event<?> event, String messageId, String exchange, String routingKey, MessageProperties messageProperties) throws JsonProcessingException {
        final Trailer trailer = Optional.ofNullable(event.getTrailer()).orElse(new Trailer());
        trailer.setSent(LocalDateTime.now());
        trailer.setLastAgentId(event.getRoutingKey().getOrigin());
        Optional.ofNullable(event.getData()).ifPresent(data -> {
            try {
                String json = this.objectMapper.writeValueAsString(data);
                trailer.setDataSize(json.length());
            } catch (JsonProcessingException e) {
                throw new ParameterException(e);
            }
        });

        event.setTrailer(trailer);

        final Message eventMessage = new EventMessage(encryptBody(event), messageProperties);
        eventMessage.getMessageProperties().setMessageId(messageId);
        eventMessage.getMessageProperties().setHeader("request-id", messageId);
        eventMessage.getMessageProperties().setHeader("created", LocalDateTime.now());
        eventMessage.getMessageProperties().setAppId(event.getRoutingKey().getOrigin());

        final MessagePostProcessor messagePostProcessor = message -> {
            log.debug("MessagePostProcessor: {}", message);
            return message;
        };
        return messagePostProcessor.postProcessMessage(eventMessage, null);
    }

    @Override
    public Event<?> event(Message message, Type type) throws JsonProcessingException {
        log.debug("mapping msg: {}, to Type: {}", message, type);
        final String data = new String(Base64Utils.decode(message.getBody()), StandardCharsets.UTF_8);
        log.debug("data: {}", data);
        JavaType javaType = buildJavaTypeFromType(type);
        final Event<?> event = objectMapper.readValue(data, javaType);
        log.debug("event: {}", event);
        event.setProperties(message.getMessageProperties());
        return event;
    }

    @Override
    public <T> Event<T> event(Event<?> event, Class<T> body) throws JsonProcessingException {
        final var data = objectMapper.writeValueAsString(event.getData());
        final var out = objectMapper.readValue(data, body);
        final Event<T> eventOut = new Event<>();
        eventOut.setMetadata(event.getMetadata());
        eventOut.setRoutingKey(event.getRoutingKey());
        eventOut.setHeaders(event.getHeaders());
        eventOut.setTrailer(event.getTrailer());
        eventOut.setProperties(event.getProperties());
        eventOut.setData(out);
        eventOut.setCode(event.getCode());
        eventOut.setMessage(event.getMessage());
        return eventOut;
    }

    public <T> byte[] encryptBody(final T body) throws JsonProcessingException {
        final String bodyTmp = this.objectMapper.writeValueAsString(body);
        return Base64Utils.encode(bodyTmp.getBytes(StandardCharsets.UTF_8));
    }

    public JavaType buildJavaTypeFromType(Type type) {
        return this.objectMapper.getTypeFactory().constructType(type);
    }
}


