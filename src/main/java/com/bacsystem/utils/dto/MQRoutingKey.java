package com.bacsystem.utils.dto;


import com.bacsystem.utils.components.enums.MQEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

import static com.bacsystem.utils.constants.RabbitMQConstant.SIMPLE_DOT_FORMAT;
import static com.bacsystem.utils.constants.RabbitMQConstant.SIMPLE_EMPTY_FORMAT;

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
        sb.append(SIMPLE_DOT_FORMAT);
        sb.append(origin);
        sb.append(SIMPLE_DOT_FORMAT);
        sb.append(destiny);
        sb.append(SIMPLE_DOT_FORMAT);
        sb.append(domain);
        sb.append(SIMPLE_DOT_FORMAT);
        sb.append(command);

        var route = Optional.ofNullable(additional).orElse(SIMPLE_EMPTY_FORMAT);
        if (!route.trim().isEmpty()) {
            sb.append(SIMPLE_DOT_FORMAT);
            sb.append(route);
        }
        return sb.toString();
    }
}