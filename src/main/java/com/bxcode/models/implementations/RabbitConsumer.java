package com.bxcode.models.implementations;

import com.bxcode.components.configurations.MessageConfiguration;
import com.bxcode.dto.Event;
import com.bxcode.models.contract.IRabbitConsumer;
import com.bxcode.services.contracts.IConsumerDispatchService;
import com.bxcode.services.contracts.IEventMapperService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import static java.lang.Thread.sleep;

/**
 * RabbitConsumer
 * <p>
 * RabbitConsumer class.
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
public class RabbitConsumer implements IRabbitConsumer {

    private final Channel channel;
    private final IEventMapperService mapperService;
    private final Method method;
    private final Type type;
    private final String routingKey;
    private final boolean automaticAck;
    private final Object object;
    private final IConsumerDispatchService dispatchService;
    private boolean connected;
    private final MessageConfiguration messageConfiguration;

    public RabbitConsumer(final Channel channel,
                          final Method method,
                          final Object object,
                          final Type type,
                          final String routingKey,
                          final boolean automaticAck,
                          final IEventMapperService mapperService,
                          final IConsumerDispatchService dispatchService,
                          final MessageConfiguration messageConfiguration) {
        this.channel = channel;
        this.mapperService = mapperService;
        this.method = method;
        this.type = type;
        this.routingKey = routingKey;
        this.automaticAck = automaticAck;
        this.object = object;
        this.dispatchService = dispatchService;
        this.messageConfiguration = messageConfiguration;
    }

    @Override
    public void handleConsumeOk(String s) {
        log.debug("consumer creation ok, consumer tag {}", s);
        this.connected = true;
    }

    @Override
    public void handleCancelOk(String s) {
        log.debug("consumer canceled, consumer tag {}", s);
    }

    @Override
    public void handleCancel(String s) throws IOException {
        log.debug("consumer cancel handle: {}", s);
    }

    @Override
    public void handleShutdownSignal(String s, ShutdownSignalException e) {
        log.debug("handleShutdownSignal: tag={}, queue name={}", s, routingKey);
        log.error("handleShutdownSignal: ShutdownSignalException error: {}", e.getMessage());
        connected = false;
        int count = 0;
        int retries = this.messageConfiguration.getReconnectConsumerTries();
        long time = this.messageConfiguration.getReconnectConsumerTime();
        while (count <= retries && !connected) {
            log.debug("try to reconnect to consumer");
            try {
                sleep(time);
                String consumer = this.channel.basicConsume(routingKey, automaticAck, this);
                log.debug("new consumer, tag:{}, queue name:{}", consumer, routingKey);
                connected = true;
            } catch (IOException | AmqpException | InterruptedException ex) {
                log.error("ShutdownSignalException amqpException error: {}", e.getMessage());
            } finally {
                if (retries != 0) {
                    count++;
                }
            }
        }

    }

    @Override
    public void handleRecoverOk(String s) {
        log.debug("recover, consumer tag {}", s);
    }

    @Override
    public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
        try {
            long deliveryTag = envelope.getDeliveryTag();
            log.debug("consumer tag {}", s);
            log.debug("delivery {}", deliveryTag);
            log.debug("channel {}", channel.getChannelNumber());
            final MessagePropertiesConverter converter = new DefaultMessagePropertiesConverter();
            final MessageProperties properties = converter.toMessageProperties(basicProperties, envelope, StandardCharsets.UTF_8.name());
            final Message message = new Message(bytes, properties);
            final Event<?> event = mapperService.event(message, type);
            log.debug("message received: {}", message);
            dispatchService.callBack(this.object, method, event);
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.error("error in process handleDelivery, error: {} ", e.getMessage());
        }
    }
}