package com.microservice.messaging.broker.services.factory;


import com.microservice.messaging.broker.components.annotations.MQDeclareExchange;
import com.microservice.messaging.broker.components.base.MQBase;
import com.microservice.messaging.broker.components.enums.MQExchangesType;
import com.microservice.messaging.broker.components.environments.MQEnvironment;
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
        var exchangeName = this.environment.get(exchange);
        MQExchangesType exchangesType = MQExchangesType.valueOf(exchangeType);
        log.info("annotation {} is present, declaring exchange: {}, type: {}", MQDeclareExchange.class, exchangeName, exchangesType.getValue());
        var declareExchangeOk = this.rabbitTemplate.execute(channel -> {
            Map<String, Object> arguments = new HashMap<>();
            return channel.exchangeDeclare(exchangeName,
                    exchangesType.getValue(),
                    annotation.durability(),
                    annotation.autoDelete(),
                    annotation.internal(),
                    arguments
            );
        });
        log.info("declareExchangeOk: {}", declareExchangeOk);
    }
}
