package com.bxcode.services.implementations;

import com.bxcode.components.annotations.BrokerConsumer;
import com.bxcode.components.annotations.BrokerDeclareBinding;
import com.bxcode.components.annotations.BrokerDeclareQueue;
import com.bxcode.components.exceptions.ParameterException;
import com.bxcode.components.helpers.Environments;
import com.bxcode.dto.Event;
import com.bxcode.services.contracts.*;
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
 * ConsumerRegisterService
 * <p>
 * ConsumerRegisterService class.
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
public class ConsumerRegisterService implements IConsumerRegisterService {

    private final IConsumerBuilderService consumerBuilderService;
    private final IEventMapperService mapperService;
    private final IConsumerDispatchService dispatchService;
    private final IQueueRegisterService queueRegisterService;
    private final RabbitTemplate rabbitTemplate;
    private final Environments environments;

    public ConsumerRegisterService(final IConsumerBuilderService consumerBuilderService,
                                   final IEventMapperService mapperService,
                                   final IConsumerDispatchService dispatchService,
                                   final IQueueRegisterService queueRegisterService,
                                   final RabbitTemplate rabbitTemplate,
                                   final Environments environments) {
        this.consumerBuilderService = consumerBuilderService;
        this.mapperService = mapperService;
        this.dispatchService = dispatchService;
        this.queueRegisterService = queueRegisterService;
        this.rabbitTemplate = rabbitTemplate;
        this.environments = environments;
        log.debug("ConsumerRegisterService loaded successfully");
    }

    @Override
    public List<AMQP.Queue.BindOk> executeBindings(String queue, BrokerDeclareBinding[] bindings) {
        log.debug("execute bindings process");
        final List<AMQP.Queue.BindOk> bindOkList = new ArrayList<>();
        for (BrokerDeclareBinding binding : bindings) {
            String routingKey = environments.get(binding.routingKey());
            String exchange = environments.get(binding.exchange());
            bindOkList.add(rabbitTemplate.execute(channel -> channel.queueBind(queue, exchange, routingKey)));
        }
        return bindOkList;
    }

    @Override
    public void register(Object bean, String nameBean) {
        final Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            final BrokerConsumer annotation = method.getAnnotation(BrokerConsumer.class);
            if (Objects.isNull(annotation)) {
                log.debug("BrokerConsumer Present");
                log.debug("registering consumers...");
                log.debug("auto-ack: {}", annotation.automaticAck());
                if (method.getParameterCount() != 1) {
                    throw new ParameterException("Only 1 parameter allows for BrokerConsumer annotation implementation");
                }
                final Parameter parameter = method.getParameters()[0];
                log.debug("Parameter ParameterizedType: {}", parameter.getParameterizedType().getTypeName());
                log.debug("Parameter TypeName: {}", parameter.getType().getTypeName());
                log.debug("Parameter Name: {}", parameter.getType().getName());
                if (!parameter.getType().equals(Event.class)) {
                    throw new ParameterException("Parameter to consumer method must be Event.class");
                }

                for (String queue : annotation.queues()) {
                    log.debug("queue to create: {}", queue);
                    if (queue == null || queue.trim().isEmpty()) {
                        if (annotation.bindings().length < 1) {
                            throw new ParameterException("when queue name is null or empty, bindings should not be empty");
                        }
                        final AMQP.Queue.DeclareOk declareOk = rabbitTemplate.execute(Channel::queueDeclare);
                        assert declareOk != null;
                        queue = declareOk.getQueue();
                        log.debug("new queue name: {}", queue);
                    }

                    final String nameQueue = environments.get(queue);
                    final boolean ack = Boolean.parseBoolean(environments.get(Boolean.toString(annotation.automaticAck())));
                    final Consumer consumer = consumerBuilderService
                            .build(this.rabbitTemplate.execute(channel -> channel),
                                    method,
                                    bean,
                                    parameter.getParameterizedType(),
                                    nameQueue,
                                    ack,
                                    mapperService,
                                    dispatchService);
                    executeQueues(annotation.declareQueues());
                    executeBindings(nameQueue, annotation.bindings());

                    final String tag = rabbitTemplate.execute(channel -> channel.basicConsume(nameQueue, ack, consumer));
                    log.debug("consumer registered, tag: {}, queueName: {}", tag, nameQueue);
                }
            }
        }
    }

    private void executeQueues(BrokerDeclareQueue[] queues) {
        this.log.debug("execute queues process");
        for (BrokerDeclareQueue declare : queues)
            this.queueRegisterService.declare(declare);
    }
}


