package com.microservice.messaging.broker.services.factory;


import com.microservice.messaging.broker.components.annotations.MQDeclareBinding;
import com.microservice.messaging.broker.components.base.MQBase;
import com.microservice.messaging.broker.components.environments.MQEnvironment;
import com.microservice.messaging.broker.components.exceptions.MQBrokerException;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * MQBinding
 * <p>
 * MQBinding class.
 * <p>
 * This class specifies the requirements for the MQBinding component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

@Log4j2
@Component
public class MQBinding extends MQBase implements IMQBinding {
    private final RabbitTemplate rabbitTemplate;
    private final MQEnvironment environment;

    public MQBinding(RabbitTemplate rabbitTemplate, MQEnvironment environment) {
        super(MQBinding.class.getSimpleName());
        this.rabbitTemplate = rabbitTemplate;
        this.environment = environment;
    }

    @Override
    public void register(MQDeclareBinding annotation) {
        var exchange = this.environment.get(annotation.exchange());
        var queue = this.environment.get(annotation.queue());
        var routingKey = this.environment.get(annotation.routingKey());

        validateParameters(exchange, queue, routingKey);

        try {
            log.debug("annotation {} is present, declaring binding, exchange: {}, queue: {}, routingKey: {}", MQDeclareBinding.class, exchange, queue, routingKey);
            var declareBindingOk = this.rabbitTemplate.execute(channel -> channel.queueBind(queue, exchange, routingKey));
            log.debug("Binding declared successfully: {}", declareBindingOk);
        } catch (Exception e) {
            log.error("Failed to declare binding: exchange={}, queue={}, routingKey={}", exchange, queue, routingKey, e);
            throw new MQBrokerException("Error declaring binding", e);
        }
    }

    private void validateParameters(String exchange, String queue, String routingKey) {
        if (exchange == null || exchange.isEmpty()) {
            throw new MQBrokerException("Exchange cannot be null or empty");
        }
        if (queue == null || queue.isEmpty()) {
            throw new MQBrokerException("Queue cannot be null or empty");
        }
        if (routingKey == null || routingKey.isEmpty()) {
            throw new MQBrokerException("Routing key cannot be null or empty");
        }
    }
}
