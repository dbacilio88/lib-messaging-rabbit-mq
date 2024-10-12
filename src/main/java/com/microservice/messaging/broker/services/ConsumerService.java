package com.microservice.messaging.broker.services;


import com.microservice.messaging.broker.components.annotations.MQBrokerConsumer;
import com.microservice.messaging.broker.components.annotations.MQDeclareBinding;
import com.microservice.messaging.broker.components.annotations.MQDeclareQueue;
import com.microservice.messaging.broker.components.base.MQBase;
import com.microservice.messaging.broker.components.environments.MQEnvironment;
import com.microservice.messaging.broker.components.events.IMQEventMessageService;
import com.microservice.messaging.broker.components.exceptions.MQBrokerException;
import com.microservice.messaging.broker.dto.MQEvent;
import com.microservice.messaging.broker.services.builder.IMQConsumerBuilder;
import com.microservice.messaging.broker.services.dispatch.IMQConsumerDispatch;
import com.microservice.messaging.broker.services.factory.IMQQueue;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ConsumerService
 * <p>
 * ConsumerService class.
 * <p>
 * This class specifies the requirements for the ConsumerService component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */


@Log4j2
@Service
public class ConsumerService extends MQBase implements IConsumerService {

    private final RabbitTemplate rabbitTemplate;
    private final MQEnvironment mqEnvironment;
    private final IMQConsumerBuilder consumerBuilder;
    private final IMQQueue imqQueue;
    private final IMQEventMessageService eventMessageService;
    private final IMQConsumerDispatch consumerDispatch;

    public ConsumerService(final RabbitTemplate rabbitTemplate,
                           final MQEnvironment mqEnvironment,
                           final IMQConsumerBuilder consumerBuilder,
                           final IMQQueue imqQueue,
                           final IMQEventMessageService eventMessageService,
                           final IMQConsumerDispatch consumerDispatch) {
        super(ConsumerService.class.getSimpleName());
        this.rabbitTemplate = rabbitTemplate;
        this.mqEnvironment = mqEnvironment;
        this.consumerBuilder = consumerBuilder;
        this.imqQueue = imqQueue;
        this.eventMessageService = eventMessageService;
        this.consumerDispatch = consumerDispatch;
    }


    @Override
    public List<AMQP.Queue.BindOk> executeBinding(String queue, MQDeclareBinding[] bindings) {
        log.debug("execute bindings process");
        final List<AMQP.Queue.BindOk> bindOkList = new ArrayList<>();
        for (MQDeclareBinding binding : bindings) {
            var routingKey = this.mqEnvironment.get(binding.routingKey());
            var exchange = this.mqEnvironment.get(binding.exchange());
            bindOkList.add(this.rabbitTemplate.execute(channel -> channel.queueBind(queue, exchange, routingKey)));
        }
        return bindOkList;
    }

    @Override
    public void register(Object bean, String name) {
        final Method[] methods = bean.getClass().getDeclaredMethods();

        for (final Method method : methods) {
            final MQBrokerConsumer annotation = method.getAnnotation(MQBrokerConsumer.class);
            if (Objects.nonNull(annotation)) {
                log.info("Registering MQBrokerConsumer name annotation {} auto-ack {}", annotation, annotation.isAck());
                if (method.getParameterCount() != 1) {
                    throw new MQBrokerException("MQBrokerConsumer must have exactly one parameter");
                }
                final Parameter parameter = method.getParameters()[0];
                if (!parameter.getType().equals(MQEvent.class)) {
                    throw new MQBrokerException("MQBrokerConsumer must have exactly one parameter");
                }
                for (String queue : annotation.queues()) {
                    log.info("queue to create {}", queue);
                    if (queue == null || queue.trim().isEmpty()) {
                        if (annotation.declareBindings().length < 1) {
                            throw new MQBrokerException("MQBrokerConsumer must have at least one binding");
                        }
                        final AMQP.Queue.DeclareOk declareOk = rabbitTemplate.execute(Channel::queueDeclare);
                        assert declareOk != null;
                        queue = declareOk.getQueue();
                        log.info("new queue name {}", queue);
                    }
                    final String finalQueue = mqEnvironment.get(queue);
                    final boolean automaticAck = Boolean.parseBoolean(mqEnvironment.get(Boolean.toString(annotation.isAck())));

                    Consumer consumer = this.consumerBuilder.build(this.rabbitTemplate.execute(channel -> channel),
                            method, bean, parameter.getType(), finalQueue, automaticAck, eventMessageService, consumerDispatch);


                    executeQueues(annotation.declareQueues());
                    executeBinding(queue, annotation.declareBindings());
                    final String consumerTag = this.rabbitTemplate.execute(channel -> channel.basicConsume(finalQueue, automaticAck, consumer));
                    log.debug("consumer registered, tag: {}, queueName: {}", consumerTag, finalQueue);
                }

            }
        }
    }

    private void executeQueues(MQDeclareQueue[] brokerDeclareQueues) {
        log.debug("execute queues process");
        for (MQDeclareQueue declare : brokerDeclareQueues)
            this.imqQueue.declare(declare);
    }
}