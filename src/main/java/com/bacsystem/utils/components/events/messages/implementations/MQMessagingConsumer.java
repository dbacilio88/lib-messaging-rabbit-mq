package com.bacsystem.utils.components.events.messages.implementations;


import com.bacsystem.utils.components.base.MQBase;
import com.bacsystem.utils.components.events.IMQEventMessageService;
import com.bacsystem.utils.components.events.messages.contracts.IMQMessagingConsumer;
import com.bacsystem.utils.components.exceptions.MQBrokerException;
import com.bacsystem.utils.components.properties.RabbitMQProperties;
import com.bacsystem.utils.dto.MQEvent;
import com.bacsystem.utils.services.dispatch.IMQConsumerDispatch;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * MQMessagingConsumer
 * <p>
 * MQMessagingConsumer class.
 * <p>
 * This class specifies the requirements for the MQMessagingConsumer component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */


@Log4j2
public class MQMessagingConsumer extends MQBase implements IMQMessagingConsumer {
    private final Channel channel;
    private final Method method;
    private final Object object;
    private final Type type;
    private final String routingKey;
    private final boolean automaticAck;
    private boolean connected;
    private final RabbitMQProperties rabbitMQProperties;
    private final IMQConsumerDispatch consumerDispatch;
    private final IMQEventMessageService eventMessageService;

    public MQMessagingConsumer(final Channel channel,
                               final Method method,
                               final Object object, final Type type,
                               final String routingKey,
                               final boolean automaticAck,
                               final RabbitMQProperties rabbitMQProperties,
                               final IMQEventMessageService eventMessageService,
                               final IMQConsumerDispatch consumerDispatch
    ) {
        super(MQMessagingConsumer.class.getSimpleName());
        this.channel = channel;
        this.method = method;
        this.object = object;
        this.type = type;
        this.routingKey = routingKey;
        this.automaticAck = automaticAck;
        this.rabbitMQProperties = rabbitMQProperties;
        this.consumerDispatch = consumerDispatch;
        this.eventMessageService = eventMessageService;
    }

    @Override
    public void handleConsumeOk(String s) {
        log.debug("consumer creation ok, consumerTag: {}", s);
        connected = true;
    }

    @Override
    public void handleCancelOk(String s) {
        log.debug("consumer canceled: {}", s);
    }

    @Override
    public void handleCancel(String s) {
        log.debug("consumer cancel handle: {}", s);
    }

    @Override
    public void handleShutdownSignal(String s, ShutdownSignalException e) {
        log.info("handleShutdownSignal: tag={}, queueName={}", s, routingKey);
        log.error("handleShutdownSignal ShutdownSignalException error: {}", e.getMessage());
        connected = false;
        int count = 0;
        var tries = this.rabbitMQProperties.getReconnectConsumerTries();
        var time = this.rabbitMQProperties.getReconnectConsumerTime();
        while (count <= tries && !connected) {
            log.debug("try to reconnect consumer");
            try {
                Thread.sleep(time);
                var basicConsume = this.channel.basicConsume(routingKey, automaticAck, this);
                log.info("new consumer, tag: {}, queueName: {}", basicConsume, routingKey);
                connected = true;
            } catch (InterruptedException | IOException ex) {
                log.error("Exception, msg: {}, trace: {}", e.getMessage(), e);
                throw new MQBrokerException(e);
            } finally {
                if (tries != 0) {
                    count++;
                }
            }

        }
    }

    @Override
    public void handleRecoverOk(String s) {
        log.debug("handleRecoverOk: {}", s);
    }

    @Override
    public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
        var deliveryTag = envelope.getDeliveryTag();
        log.debug("consumerTag: {}", s);
        log.debug("delivery: {}", deliveryTag);
        log.debug("channel: {}", channel.getChannelNumber());
        final MessagePropertiesConverter converter = new DefaultMessagePropertiesConverter();
        var toMessageProperties = converter.toMessageProperties(basicProperties, envelope, StandardCharsets.UTF_8.name());
        var message = new Message(bytes, toMessageProperties);
        final MQEvent<?> event = eventMessageService.event(message, type);
        log.debug("message received: {}", message);
        try {
            log.info("consumerDispatch {} {} {}", object, message, event);
            this.consumerDispatch.callBack(object, method, event);
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.error("Exception, msg: {}, trace: {}", e.getMessage(), e);
            throw new MQBrokerException(e);
        }
    }
}
