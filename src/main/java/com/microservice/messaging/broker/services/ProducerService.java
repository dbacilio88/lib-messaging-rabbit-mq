package com.microservice.messaging.broker.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.messaging.broker.components.base.MQBase;
import com.microservice.messaging.broker.components.events.IMQEventMessageService;
import com.microservice.messaging.broker.dto.MQEvent;
import com.microservice.messaging.broker.services.factory.IMQCallBack;
import com.microservice.messaging.broker.services.factory.IMQConfirme;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

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
    private static final ParameterizedTypeReference<MQEvent<?>> type = ParameterizedTypeReference.forType(MQEvent.class);
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
    public boolean producer(String exchange, MQEvent<?> event) {
        log.info("Producer called");
        return false;
    }

    @Override
    public boolean producer(String exchange, MQEvent<?> event, boolean retry) {
        log.info("Producer called retry");
        return false;
    }

    @Override
    public <T> MQEvent<T> producerAndConsumer(String exchange, MQEvent<T> event, Class<T> clazz) {
        log.info("Producer called event");
        return null;
    }

    private Message generateMessage(MQEvent<?> event, String messageId, String exchangeName, String routingKey, MessageProperties messageProperties) throws JsonProcessingException {
        return eventMessageService.messageBroker(event, messageId, exchangeName, routingKey, messageProperties);
    }
}