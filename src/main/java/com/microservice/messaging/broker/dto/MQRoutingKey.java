package com.microservice.messaging.broker.dto;


import com.microservice.messaging.broker.components.enums.MQEventType;
import com.microservice.messaging.broker.constants.RabbitMQConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * MQRoutingKey
 * <p>
 * MQRoutingKey class.
 * <p>
 * This class specifies the requirements for the MQRoutingKey component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MQRoutingKey {
    private MQEventType eventType;
    private String origin;
    private String destiny;
    private String domain;
    private String command;
    private String additional;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(eventType.name());
        sb.append(RabbitMQConstant.SIMPLE_DOT_FORMAT);
        sb.append(origin);
        sb.append(RabbitMQConstant.SIMPLE_DOT_FORMAT);
        sb.append(destiny);
        sb.append(RabbitMQConstant.SIMPLE_DOT_FORMAT);
        sb.append(domain);
        sb.append(RabbitMQConstant.SIMPLE_DOT_FORMAT);
        sb.append(command);
        Optional.ofNullable(additional).ifPresent(s -> sb.append(RabbitMQConstant.SIMPLE_DOT_FORMAT));
        sb.append(RabbitMQConstant.SIMPLE_DOT_FORMAT);
        return sb.toString();
    }
}