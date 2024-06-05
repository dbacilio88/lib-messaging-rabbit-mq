package com.bxcode.services.implementations;

import com.bxcode.components.annotations.BrokerDeclareExchange;
import com.bxcode.components.enums.ExchangeType;
import com.bxcode.components.helpers.Environments;
import com.bxcode.services.contracts.IExchangeRegisterService;
import com.rabbitmq.client.AMQP;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * ExchangeRegisterService
 * <p>
 * ExchangeRegisterService class.
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
public class ExchangeRegisterService implements IExchangeRegisterService {

    private final RabbitTemplate rabbitTemplate;
    private final Environments environments;

    public ExchangeRegisterService(RabbitTemplate rabbitTemplate, Environments environments) {
        this.rabbitTemplate = rabbitTemplate;
        this.environments = environments;
        log.debug("ExchangeRegisterService loaded successfully");
    }

    @Override
    public void register(Object bean, String nameBean) {
        BrokerDeclareExchange annotation = bean.getClass().getDeclaredAnnotation(BrokerDeclareExchange.class);
        Optional.ofNullable(annotation).ifPresent(currentAnnotation -> {

            for (String exchange : annotation.exchanges()) {
                final String typeName = environments.get(currentAnnotation.typeName());
                final ExchangeType exchangeType = ExchangeType.valueOf(typeName);
                final String exchangeName = environments.get(exchange);
                log.debug("annotation {} is present, declaring exchange: {}, exchangeType: {}", BrokerDeclareExchange.class, exchangeName, exchangeType.name());

                final AMQP.Exchange.DeclareOk declareOk = rabbitTemplate.execute(channel -> {
                    Map<String, Object> arguments = new HashMap<>();
                    if (!annotation.alternateExchange().isEmpty()) {
                        arguments.put("alternate-exchange", annotation.alternateExchange());
                    }
                    return channel.exchangeDeclare(exchangeName
                            , exchangeType.getType()
                            , annotation.durable()
                            , annotation.autoDelete()
                            , annotation.internal()
                            , arguments);
                });
                log.debug("declareOk: {}", declareOk);
            }
        });
    }
}