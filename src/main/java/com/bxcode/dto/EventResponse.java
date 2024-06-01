package com.bxcode.dto;

import com.rabbitmq.client.BasicProperties;
import lombok.*;

import java.io.Serializable;

/**
 * EventResponse
 * <p>
 * EventResponse class.
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
public class EventResponse<T> implements Serializable {


    private static final long serialVersionUID = 3693115058060590428L;
    private transient T body;
    private transient BasicProperties properties;
}