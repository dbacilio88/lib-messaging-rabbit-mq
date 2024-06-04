package com.bxcode.services.implementations;

import com.bxcode.components.annotations.BrokerDeclareBinding;
import com.bxcode.components.helpers.Environments;
import com.bxcode.services.contracts.IBindingRegisterService;
import com.rabbitmq.client.AMQP;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * BindingRegisterService
 * <p>
 * BindingRegisterService class.
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
public class BindingRegisterService implements IBindingRegisterService {

    private final RabbitTemplate rabbitTemplate;
    private final Environments environments;

    public BindingRegisterService(final RabbitTemplate rabbitTemplate,
                                  final Environments environments) {
        this.rabbitTemplate = rabbitTemplate;
        this.environments = environments;
        log.debug("BindingRegisterService load successfully");
    }

    @Override
    public void register(Object bean, String nameBean) {
        BrokerDeclareBinding declareBinding = bean.getClass().getDeclaredAnnotation(BrokerDeclareBinding.class);
        Optional.ofNullable(declareBinding).ifPresent(current -> {
            final String queue = environments.get(current.queue());
            final String exchange = environments.get(current.exchange());
            final String routingKey = environments.get(current.routingKey());
            log.debug("annotation is present declare binding: {},{}", exchange, queue);
            final AMQP.Queue.BindOk bindOk = rabbitTemplate.execute(channel -> channel.queueBind(queue, exchange, routingKey));
            log.debug("declare is: {}", bindOk);
        });
    }
}