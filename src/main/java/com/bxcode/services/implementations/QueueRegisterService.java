package com.bxcode.services.implementations;

import com.bxcode.components.annotations.BrokerDeclareQueue;
import com.bxcode.components.annotations.QueueProperty;
import com.bxcode.components.configurations.MessageConfiguration;
import com.bxcode.components.helpers.Environments;
import com.bxcode.services.contracts.IQueueRegisterService;
import com.rabbitmq.client.AMQP;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * QueueRegisterService
 * <p>
 * QueueRegisterService class.
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
public class QueueRegisterService implements IQueueRegisterService {

    private final MessageConfiguration messageConfiguration;
    private final RabbitTemplate rabbitTemplate;
    private final Environments environments;

    public QueueRegisterService(MessageConfiguration messageConfiguration, RabbitTemplate rabbitTemplate, Environments environments) {
        this.messageConfiguration = messageConfiguration;
        this.rabbitTemplate = rabbitTemplate;
        this.environments = environments;
        log.debug("QueueRegisterService loaded successfully");
    }

    @Override
    public void declare(BrokerDeclareQueue annotation) {
        Optional.ofNullable(annotation).ifPresent(currentAnnotation -> {
            for (String queue : annotation.queues()) {
                final String queueName = environments.get(queue);
                final Map<String, Object> properties = buildQueueProperties(annotation.queueProperties());
                log.debug("annotation {} is present, declaring queue: {}", BrokerDeclareQueue.class, queueName);
                final AMQP.Queue.DeclareOk declareOk = this.rabbitTemplate.execute(channel ->
                        channel.queueDeclare(queueName, currentAnnotation.durable(), currentAnnotation.exclusive(), currentAnnotation.autoDelete(), properties)
                );
                log.debug("declareOk: {}", declareOk);
            }
        });
    }

    @Override
    public void register(Object bean, String nameBean) {
        final BrokerDeclareQueue annotation = bean.getClass().getDeclaredAnnotation(BrokerDeclareQueue.class);
        declare(annotation);
    }

    public Map<String, Object> buildQueueProperties(QueueProperty[] queueProperties) {
        final Map<String, Object> properties = new HashMap<>();
        for (QueueProperty property : queueProperties)
            properties.put(this.environments.get(property.name()), this.environments.get(property.value()));
        if (Optional.ofNullable(this.messageConfiguration.getQueueProperties()).isPresent())
            properties.putAll(this.messageConfiguration.getQueueProperties());
        return properties;
    }
}


