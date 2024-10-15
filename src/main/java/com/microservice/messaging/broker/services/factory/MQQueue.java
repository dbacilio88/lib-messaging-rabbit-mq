package com.microservice.messaging.broker.services.factory;


import com.microservice.messaging.broker.components.annotations.MQDeclareQueue;
import com.microservice.messaging.broker.components.base.MQBase;
import com.microservice.messaging.broker.components.environments.MQEnvironment;
import com.microservice.messaging.broker.components.exceptions.MQBrokerException;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * MQQueue
 * <p>
 * MQQueue class.
 * <p>
 * This class specifies the requirements for the MQQueue component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 11/10/2024
 */

@Log4j2
@Component
public class MQQueue extends MQBase implements IMQQueue {

    private final RabbitTemplate rabbitTemplate;
    private final MQEnvironment environment;

    public MQQueue(RabbitTemplate rabbitTemplate, MQEnvironment environment) {
        super(MQQueue.class.getSimpleName());
        this.rabbitTemplate = rabbitTemplate;
        this.environment = environment;
    }

    @Override
    public void register(String queue, MQDeclareQueue annotation) {
        declare(queue, annotation);
    }

    @Override
    public void declare(String queue, MQDeclareQueue annotation) {
        var queueName = this.environment.get(queue);
        if (queueName == null) {
            throw new MQBrokerException("Queue name cannot be null.");
        }
        log.debug("annotation {} is present, declaring queue: {}", MQDeclareQueue.class, queueName);
        try {
            var declareQueueOk = this.rabbitTemplate.execute(channel -> {
                Map<String, Object> arguments = new HashMap<>();
                // Add arguments: TODO
                return channel.queueDeclare(queueName, annotation.durability(), annotation.exclusive(), annotation.autoDelete(), arguments);
            });
            log.debug("Queue declared successfully: {}", declareQueueOk);
        } catch (Exception e) {
            log.error("Failed to declare queue: {}", queueName, e);
            throw new MQBrokerException("Error declaring queue", e);
        }
    }
}
