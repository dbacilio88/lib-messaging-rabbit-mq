package com.bacsystem.utils.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.bacsystem.utils.components.base.MQBase;
import com.bacsystem.utils.components.enums.MQValidation;
import com.bacsystem.utils.components.events.IMQEventMessageService;
import com.bacsystem.utils.components.exceptions.MQBrokerException;
import com.bacsystem.utils.components.properties.validations.IRabbitMessagePropertiesValidation;
import com.bacsystem.utils.dto.MQEvent;
import com.bacsystem.utils.dto.MQMetadata;
import com.bacsystem.utils.services.factory.IMQCallBack;
import com.bacsystem.utils.services.factory.IMQConfirme;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.bacsystem.utils.constants.RabbitMQConstant.PARAMETER_NUMBER_REPLAY_CODE;

/**
 * ProducerService
 * <p>
 * ProducerService class.
 * <p>
 * This class specifies the requirements for the ProducerService component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */


@Log4j2
@Service
public class ProducerService extends MQBase implements IProducerService {
    private final RabbitTemplate rabbitTemplate;
    private final SimpleDateFormat simpleDateFormat;
    private final IMQEventMessageService eventMessageService;
    private final IMQConfirme imqConfirme;
    private final IMQCallBack imqCallBack;

    public ProducerService(final RabbitTemplate rabbitTemplate,
                           final IMQEventMessageService eventMessageService,
                           final IMQConfirme imqConfirme,
                           final IMQCallBack imqCallBack) {
        super(ProducerService.class.getSimpleName());
        this.rabbitTemplate = rabbitTemplate;
        this.eventMessageService = eventMessageService;
        this.imqConfirme = imqConfirme;
        this.imqCallBack = imqCallBack;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }


    @Override
    @Async
    public boolean producer(String exchange, MQEvent<?> event, boolean retry) throws JsonProcessingException {
        log.debug("producer exchange, {} - event, {},retry {}", exchange, event, retry);
        Optional.ofNullable(event).orElseThrow(() -> new MQBrokerException("return object is null"));

        boolean hasMetadata = false;

        final MQMetadata metadata = event.getMetadata();
        final StringBuilder builder = new StringBuilder(event.getRoutingKey().toString());

        if (metadata != null && metadata.getSteps() != null && !metadata.getSteps().isEmpty()) {
            builder.append(".metadata");
            hasMetadata = true;
        }


        final String routingKey = builder.toString();

        String messageId;
        final MessageProperties messageProperties = Optional.ofNullable(event.getProperties()).orElse(new MessageProperties());

        final CorrelationData correlationData = new CorrelationData();

        final MQValidation validateCorrelationId = IRabbitMessagePropertiesValidation.validateCorrelationId().apply(messageProperties);

        if (MQValidation.PROCESS_VALIDATION_SUCCESS.equals(validateCorrelationId)) {
            correlationData.setId(messageProperties.getCorrelationId());
            messageId = messageProperties.getCorrelationId();
        } else {
            final Date date = new Date();
            final Timestamp timestamp = new Timestamp(date.getTime());
            messageId = UUID.randomUUID().toString().toUpperCase(Locale.ENGLISH).concat("-").concat(simpleDateFormat.format(timestamp));
            correlationData.setId(messageId);
        }

        final MQValidation validateReplyTo = IRabbitMessagePropertiesValidation.validateReplyTo().apply(messageProperties);

        correlationData.getFuture().toCompletableFuture().thenAccept(confirm -> {

            StringBuilder reason = new StringBuilder();
            Optional.ofNullable(confirm).ifPresentOrElse(c -> reason.append(c.getReason()), () -> reason.append("error the confirm instance is null"));
            Message message;
            try {
                message = generateMessage(event, messageId, exchange, routingKey, messageProperties);
            } catch (JsonProcessingException e) {
                throw new MQBrokerException(reason.toString());
            }

            try {
                if (Objects.isNull(confirm) || !confirm.isAck()) {
                    throw new MQBrokerException(reason.toString());
                } else {
                    this.imqConfirme.confirm(event);
                }
            } catch (Exception e) {
                reason.append(" - ");
                reason.append(e.getMessage());
                imqCallBack.execute(message, PARAMETER_NUMBER_REPLAY_CODE, reason.toString(), exchange, routingKey);
                throw new MQBrokerException(reason.toString(), e);
            }
        }).exceptionally(throwable -> {
            log.error("message: {}", throwable.getMessage());
            log.error("stacktrace: {}", throwable.getStackTrace(), throwable);
            return null;
        });

        if (!hasMetadata && retry && MQValidation.PROCESS_VALIDATION_SUCCESS.equals(validateReplyTo)) {
            log.debug("reply message");
            log.debug("replyTo: {}, correlationData: {}", messageProperties.getReplyTo(), correlationData);
            messageProperties.setReplyTo(null);
            final var finalMessage = generateMessage(event, messageId, exchange, routingKey, messageProperties);
            finalMessage.getMessageProperties().setConsumerQueue(messageProperties.getReplyTo());
            this.rabbitTemplate.convertAndSend(messageProperties.getReplyTo(), finalMessage);
        } else {
            final var finalMessage = generateMessage(event, messageId, exchange, routingKey, messageProperties);
            this.rabbitTemplate.convertAndSend(exchange, routingKey, finalMessage, correlationData);
        }
        return Boolean.TRUE;
    }

    private Message generateMessage(MQEvent<?> event, String messageId, String exchangeName, String routingKey, MessageProperties messageProperties) throws JsonProcessingException {
        return eventMessageService.messageBroker(event, messageId, exchangeName, routingKey, messageProperties);
    }

    private String buildRoutingKey(MQEvent<?> event) {
        StringBuilder builder = new StringBuilder(String.valueOf(event.getRoutingKey()));
        if (event.getMetadata() != null && event.getMetadata().getSteps() != null && !event.getMetadata().getSteps().isEmpty()) {
            builder.append(".metadata");
        }
        return builder.toString();
    }

    private String generateMessageId(MQEvent<?> event) {
        String messageId = UUID.randomUUID().toString().toUpperCase(Locale.ENGLISH);
        if (event.getProperties() != null && event.getProperties().getCorrelationId() != null) {
            messageId = event.getProperties().getCorrelationId();
        }
        return messageId.concat("-").concat(simpleDateFormat.format(new Date()));
    }

    private void configureCorrelationData(MessageProperties messageProperties, CorrelationData correlationData) {
        MQValidation validateCorrelationId = IRabbitMessagePropertiesValidation.validateCorrelationId().apply(messageProperties);
        if (MQValidation.PROCESS_VALIDATION_SUCCESS.equals(validateCorrelationId)) {
            correlationData.setId(messageProperties.getCorrelationId());
        }
    }

    private boolean shouldSendReply(MessageProperties messageProperties) {
        MQValidation validateReplyTo = IRabbitMessagePropertiesValidation.validateReplyTo().apply(messageProperties);
        return MQValidation.PROCESS_VALIDATION_SUCCESS.equals(validateReplyTo);
    }

    private void sendReply(MessageProperties messageProperties, Message message) {
        log.debug("Sending reply message to: {}", messageProperties.getReplyTo());
        messageProperties.setReplyTo(null);
        message.getMessageProperties().setConsumerQueue(messageProperties.getReplyTo());
        rabbitTemplate.convertAndSend(messageProperties.getReplyTo(), message);
    }

    private void publishMessage(String exchangeName, String routingKey, Message message, CorrelationData correlationData) {
        log.debug("Publishing message to exchange: {}, routingKey: {}", exchangeName, routingKey);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message, correlationData);
    }

    private Message createMessage(MQEvent<?> event, String messageId, String exchangeName, String routingKey, MessageProperties messageProperties) throws JsonProcessingException {
        return this.eventMessageService.messageBroker(event, messageId, exchangeName, routingKey, messageProperties);
    }

    private void validateEvent(MQEvent<?> event) {
        Optional.ofNullable(event).orElseThrow(() -> new MQBrokerException("Event cannot be null"));
    }
}