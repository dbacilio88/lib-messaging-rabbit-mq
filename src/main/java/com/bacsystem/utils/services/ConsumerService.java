package com.bacsystem.utils.services;


import com.bacsystem.utils.components.annotations.MQBrokerConsumer;
import com.bacsystem.utils.components.annotations.MQDeclareBinding;
import com.bacsystem.utils.components.annotations.MQDeclareQueue;
import com.bacsystem.utils.components.base.MQBase;
import com.bacsystem.utils.components.environments.MQEnvironment;
import com.bacsystem.utils.components.events.IMQEventMessageService;
import com.bacsystem.utils.components.events.messages.implementations.MQMessagingConsumer;
import com.bacsystem.utils.components.exceptions.MQBrokerException;
import com.bacsystem.utils.components.properties.RabbitMQProperties;
import com.bacsystem.utils.components.utils.MQUtility;
import com.bacsystem.utils.dto.MQEvent;
import com.bacsystem.utils.services.dispatch.IMQConsumerDispatch;
import com.bacsystem.utils.services.factory.IMQQueue;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.bacsystem.utils.constants.RabbitMQConstant.*;

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
    private final IMQQueue imqQueue;
    private final IMQEventMessageService eventMessageService;
    private final IMQConsumerDispatch consumerDispatch;
    private final RabbitMQProperties rabbitMQProperties;

    public ConsumerService(
            final RabbitTemplate rabbitTemplate,
            final MQEnvironment mqEnvironment,
            final IMQConsumerDispatch consumerDispatch,
            final IMQEventMessageService eventMessageService,
            final IMQQueue imqQueue, RabbitMQProperties rabbitMQProperties
    ) {
        super(ConsumerService.class.getSimpleName());
        this.rabbitTemplate = rabbitTemplate;
        this.mqEnvironment = mqEnvironment;
        this.imqQueue = imqQueue;
        this.eventMessageService = eventMessageService;
        this.consumerDispatch = consumerDispatch;
        this.rabbitMQProperties = rabbitMQProperties;
    }


    @Override
    public void executeBinding(String queue, MQDeclareBinding[] bindings) {
        log.debug("execute bindings process");
        final List<AMQP.Queue.BindOk> bindOkList = new ArrayList<>();
        for (MQDeclareBinding binding : bindings) {
            var routingKey = this.mqEnvironment.get(binding.routingKey());
            var exchange = this.mqEnvironment.get(binding.exchange());
            bindOkList.add(this.rabbitTemplate.execute(channel -> channel.queueBind(queue, exchange, routingKey)));
        }
        log.debug("bindOkList {}", bindOkList);

    }


    @Override
    public void register(Method method, Object bean) {

        final MQBrokerConsumer annotation = method.getAnnotation(MQBrokerConsumer.class);

        if (Objects.nonNull(annotation)) {
            log.debug("Registering MQBrokerConsumer name annotation {} auto-ack {}", annotation, annotation.isAck());

            validateConsumerMethod(method);

            for (String queue : annotation.queues()) {
                log.debug("queue to create {}", queue);

                String finalQueue = setupQueue(queue, annotation);

                final boolean automaticAck = Boolean.parseBoolean(mqEnvironment.get(Boolean.toString(annotation.isAck())));

                Consumer consumer = build(this.rabbitTemplate.execute(channel -> channel),
                        method,
                        bean,
                        method.getParameters()[0].getType(),
                        finalQueue,
                        automaticAck,
                        eventMessageService,
                        consumerDispatch
                );

                executeQueues(annotation.declareQueues());
                executeBinding(finalQueue, annotation.declareBindings());
                final String consumerTag = this.rabbitTemplate.execute(channel -> channel.basicConsume(finalQueue, automaticAck, consumer));
                log.debug("consumer registered, tag: {}, queueName: {}", consumerTag, finalQueue);
            }
        }
    }

    @Override
    public Consumer build(Channel channel, Method method, Object bean, Type type, String queueName, boolean automaticAck, IMQEventMessageService eventMessageService, IMQConsumerDispatch consumerDispatch) {
        validateChannel(channel);
        validateMethod(method);
        validateBean(bean);
        validateType(type);
        validateQueueName(queueName);

        return new MQMessagingConsumer(
                channel,
                method,
                bean,
                type,
                queueName,
                automaticAck,
                rabbitMQProperties,
                eventMessageService,
                consumerDispatch
        );
    }

    private void validateConsumerMethod(Method method) {
        if (method.getParameterCount() != 1 || !method.getParameterTypes()[0].equals(MQEvent.class)) {
            throw new MQBrokerException("MQBrokerConsumer must have exactly one parameter of type MQEvent");
        }
    }

    private String setupQueue(String queue, MQBrokerConsumer annotation) {

        if (queue == null || queue.trim().isEmpty()) {
            if (annotation.declareBindings().length < 1) {
                throw new MQBrokerException("MQBrokerConsumer must have at least one binding");
            }

            AMQP.Queue.DeclareOk declareOk = this.rabbitTemplate.execute(channel -> {
                log.debug("channel queueDeclare");
                return channel.queueDeclare();
            });

            assert declareOk != null;
            queue = declareOk.getQueue();
            log.debug("New queue declared: {}", queue);
        } else {
            queue = mqEnvironment.get(queue);
            log.debug("Using existing queue: {}", queue);
        }
        return queue;
    }


    private void executeQueues(MQDeclareQueue[] brokerDeclareQueues) {
        log.debug("execute queues process");
        for (MQDeclareQueue q : brokerDeclareQueues) {
            for (String queue : q.queues()) {
                this.imqQueue.declare(queue, q);
            }
        }
    }


    public void validateType(Type type) {
        Optional.ofNullable(type).orElseThrow(() -> new MQBrokerException(MQUtility.formatMessage(PARAM_NULL_MSG_EXCEPTION, PARAMETER_METHOD)));
    }


    public void validateMethod(Method method) {
        Optional.ofNullable(method).orElseThrow(() -> new MQBrokerException(MQUtility.formatMessage(PARAM_NULL_MSG_EXCEPTION, PARAMETER_METHOD)));

    }


    public void validateQueueName(String queueName) {
        Optional.ofNullable(queueName).orElseThrow(() -> new MQBrokerException(MQUtility.formatMessage(PARAM_NULL_MSG_EXCEPTION, PARAMETER_QUEUE_NAME)));

    }


    public void validateBean(Object bean) {
        Optional.ofNullable(bean).orElseThrow(() -> new MQBrokerException(MQUtility.formatMessage(PARAM_NULL_MSG_EXCEPTION, PARAMETER_BEAN)));

    }


    public void validateChannel(Channel channel) {
        Optional.ofNullable(channel).orElseThrow(() -> new MQBrokerException(MQUtility.formatMessage(PARAM_NULL_MSG_EXCEPTION, PARAMETER_CHANNEL)));
    }
}