package com.bacsystem.utils.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.amqp.core.MessageProperties;

import java.util.Map;

/**
 * MQStep
 * <p>
 * MQStep class.
 * <p>
 * This class specifies the requirements for the MQStep component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MQStep {
    private MQRoutingKey routingKey;
    private MessageProperties properties;
    private Map<String, Object> mapper;
}
