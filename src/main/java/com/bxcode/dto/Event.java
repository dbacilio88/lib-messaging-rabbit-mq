package com.bxcode.dto;

import lombok.*;
import org.springframework.amqp.core.MessageProperties;

import java.io.Serializable;
import java.util.Map;

/**
 * Event
 * <p>
 * Event class.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 31/05/2024
 */

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event<T> implements Serializable {


    private static final long serialVersionUID = -2029162351448979460L;
    private RoutingKey routingKey;
    private MessageProperties properties;
    private Metadata metadata;
    private int code;
    private String message;
    private transient Map<String, ?> headers;
    private transient T data;
    private Trailer trailer;
}