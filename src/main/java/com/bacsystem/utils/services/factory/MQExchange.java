package com.bacsystem.utils.services.factory;


import com.bacsystem.utils.components.annotations.MQDeclareExchange;
import com.bacsystem.utils.components.base.MQBase;
import com.bacsystem.utils.components.enums.MQExchangesType;
import com.bacsystem.utils.components.environments.MQEnvironment;
import com.bacsystem.utils.components.exceptions.MQBrokerException;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * MQExchange
 * <p>
 * MQExchange class.
 * <p>
 * This class specifies the requirements for the MQExchange component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 11/10/2024
 */
@Log4j2
@Component
public class MQExchange extends MQBase implements IMQExchange {

    private final RabbitTemplate rabbitTemplate;
    private final MQEnvironment environment;

    public MQExchange(RabbitTemplate rabbitTemplate,
                      MQEnvironment environment) {
        super(MQExchange.class.getSimpleName());
        this.rabbitTemplate = rabbitTemplate;
        this.environment = environment;
    }

    @Override
    public void register(String exchange, MQDeclareExchange annotation) {
        String exchangeType = this.environment.get(annotation.type());
        String exchangeName = this.environment.get(exchange);

        if (exchangeName == null || exchangeType == null) {
            throw new MQBrokerException("Exchange name or type cannot be null.");
        }

        MQExchangesType exchangesType = MQExchangesType.valueOf(exchangeType);
        log.debug("annotation {} is present, declaring exchange: {}, type: {}", MQDeclareExchange.class, exchangeName, exchangesType.getValue());
        try {
            var declareExchangeOk = this.rabbitTemplate.execute(channel -> {
                Map<String, Object> arguments = new HashMap<>();
                return channel.exchangeDeclare(
                        exchangeName,
                        exchangesType.getValue(),
                        annotation.durability(),
                        annotation.autoDelete(),
                        annotation.internal(),
                        arguments
                );
            });
            log.debug("Exchange declared successfully: {}", declareExchangeOk);
        } catch (Exception e) {
            log.error("Failed to declare exchange: {}, type: {}", exchangeName, exchangesType.getValue(), e);
            throw new MQBrokerException("Error declaring exchange", e);
        }
    }
}
