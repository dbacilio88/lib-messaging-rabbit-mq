package com.bxcode.services.implementations;

import com.bxcode.components.exceptions.ParameterException;
import com.bxcode.components.exceptions.PublishEventException;
import com.bxcode.dto.Event;
import com.bxcode.dto.Metadata;
import com.bxcode.services.contracts.ICallBackRegisterService;
import com.bxcode.services.contracts.IConfirmRegisterService;
import com.bxcode.services.contracts.IEventMapperService;
import com.bxcode.services.contracts.IPublisherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.bxcode.constants.ProcessConstant.PARAMETER_NUMBER_REPLAY_CODE;
import static com.bxcode.constants.ProcessConstant.SIMPLE_DATE_FORMAT;

/**
 * PublisherService
 * <p>
 * PublisherService class.
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
public class PublisherService implements IPublisherService {

    private static final ParameterizedTypeReference<Event<?>> type = ParameterizedTypeReference.forType(Event.class);
    private final RabbitTemplate rabbitTemplate;
    private final IEventMapperService eventMapperService;
    private final SimpleDateFormat simpleDateFormat;
    private final IConfirmRegisterService confirmService;
    private final ICallBackRegisterService returnCallbackService;

    public PublisherService(final RabbitTemplate rabbitTemplate,
                            final IEventMapperService eventMapperService,
                            IConfirmRegisterService confirmService,
                            ICallBackRegisterService returnCallbackService) {
        this.rabbitTemplate = rabbitTemplate;
        this.eventMapperService = eventMapperService;
        this.simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        this.confirmService = confirmService;
        this.returnCallbackService = returnCallbackService;
        log.debug("PublisherService loaded successfully");
    }

    @Override
    @Async
    public Boolean publish(String exchange, Event<?> event) throws JsonProcessingException {
        return publish(exchange, event, true);
    }

    @Override
    @Async
    public Boolean publish(String exchange, Event<?> event, boolean reply) throws JsonProcessingException {
        Optional.ofNullable(event).orElseThrow(() -> new ParameterException("return object is null"));
        boolean hasMetadata = false;
        final Metadata metadata = event.getMetadata();
        final StringBuilder builder = new StringBuilder(event.getRoutingKey().toString());
        if (metadata != null && metadata.getSteps() != null && !metadata.getSteps().isEmpty()) {
            builder.append(".metadata");
            hasMetadata = true;
        }
        final String routingKey = builder.toString();
        String messageId;
        final MessageProperties messageProperties = Optional.ofNullable(event.getProperties()).orElse(new MessageProperties());
        final CorrelationData correlationData = new CorrelationData();

        if (true) {
            correlationData.setId(messageProperties.getCorrelationId());
            messageId = messageProperties.getCorrelationId();
        } else {
            final Date date = new Date();
            final Timestamp timestamp = new Timestamp(date.getTime());
            messageId = UUID.randomUUID().toString().toUpperCase(Locale.ENGLISH).concat("-").concat(simpleDateFormat.format(timestamp));
            correlationData.setId(messageId);
        }

        correlationData.getFuture().addCallback(confirm -> {
            StringBuilder reason = new StringBuilder();
            Optional.ofNullable(confirm).ifPresentOrElse(c -> reason.append(c.getReason()), () -> reason.append("error the confirm instance is null"));
            Message message = null;
            try {
                message = generateMessage(event, messageId, exchange, routingKey, messageProperties);
            } catch (JsonProcessingException e) {
                throw new PublishEventException(reason.toString());
            }

            try {

                if (Objects.isNull(confirm) || !confirm.isAck()) {
                    throw new PublishEventException(reason.toString());
                } else {
                    this.confirmService.executeConfirm(event);
                }

            } catch (Exception e) {
                reason.append(" - ");
                reason.append(e.getMessage());
                returnCallbackService.executeReturn(message, PARAMETER_NUMBER_REPLAY_CODE, reason.toString(), exchange, routingKey);
                throw new PublishEventException(reason.toString(), e);
            }
        }, throwable -> {
            log.error("message: {}", throwable.getMessage());
            log.error("stacktrace: {}", throwable.getStackTrace(), throwable);
        });


        if (!hasMetadata && reply) {
            log.debug("reply message");
            log.debug("replyTo: {}, correlationData: {}", messageProperties.getReplyTo(), correlationData);
            messageProperties.setReplyTo(null);

            final var finalMessage = generateMessage(event, messageId, exchange, routingKey, messageProperties);
            log.debug("message to publish true: {}", finalMessage);
            finalMessage.getMessageProperties().setConsumerQueue(messageProperties.getReplyTo());
            this.rabbitTemplate.convertAndSend(messageProperties.getReplyTo(), finalMessage);
        } else {

            final var finalMessage = generateMessage(event, messageId, exchange, routingKey, messageProperties);
            log.debug("message to publish else: {}", finalMessage);
            log.debug("exchangeName: {}, routingKey: {}, CorrelationData: {}", exchange, routingKey, correlationData);
            this.rabbitTemplate.convertAndSend(exchange, routingKey, finalMessage, correlationData);
        }

        return Boolean.TRUE;
    }

    private Message generateMessage(Event<?> event, String messageId, String exchangeName, String routingKey, MessageProperties messageProperties) throws JsonProcessingException {
        return eventMapperService.messageBroker(event, messageId, exchangeName, routingKey, messageProperties);
    }

    @Override
    public <T> Event<T> publishAndReceived(String exchange, Event<?> event, Class<T> body) throws JsonProcessingException {
        log.debug("event to publish: {}", event);
        final Metadata metadata = event.getMetadata();
        final StringBuilder builder = new StringBuilder(event.getRoutingKey().toString());
        String routingKey;
        if (metadata != null && metadata.getSteps() != null && !metadata.getSteps().isEmpty()) {
            builder.append(".metadata");
        }
        routingKey = builder.toString();
        final Date date = new Date();
        final Timestamp timestamp = new Timestamp(date.getTime());
        final String messageId = UUID.randomUUID().toString().toUpperCase(Locale.ENGLISH).concat("-").concat(simpleDateFormat.format(timestamp));
        final CorrelationData correlationData = new CorrelationData(messageId);
        correlationData.getFuture().addCallback(confirm -> {
            if (Objects.nonNull(confirm)) {
                log.debug("ack: {}", confirm.isAck());
                log.debug("reason: {}", confirm.getReason());
            }
        }, throwable -> {
            log.debug("message: {}", throwable.getMessage());
            log.error("exception: {}", throwable.getMessage(), throwable);
        });

        final Message message = eventMapperService.messageBroker(event, messageId, exchange, routingKey);
        log.debug("message to publish: {}", message);
        var response = this.rabbitTemplate.convertSendAndReceiveAsType(exchange, routingKey, message, correlationData, type);
        log.debug("response: {}", response);
        return eventMapperService.event(response, body);
    }
}


