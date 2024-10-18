package com.bacsystem.utils.dto;


import lombok.*;
import org.springframework.amqp.core.MessageProperties;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * MQEvent
 * <p>
 * MQEvent class.
 * <p>
 * This class specifies the requirements for the MQEvent component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MQEvent<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 4107356118633084015L;

    private int code;
    private String message;
    private transient T data;
    private transient Map<String, ?> headers;
    private MessageProperties properties;
    private MQTrailer MQTrailer;
    private MQRoutingKey routingKey;
    private MQMetadata metadata;
}