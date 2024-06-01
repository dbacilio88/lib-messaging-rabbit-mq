package com.bxcode.dto;

import com.bxcode.components.helpers.Serializer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.io.Serializable;

/**
 * EventMessage
 * <p>
 * EventMessage class.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 31/05/2024
 */

public class EventMessage extends Message implements Serializable {


    private static final long serialVersionUID = 613700091213471498L;

    public EventMessage(byte[] body) {
        super(body);
    }


    public EventMessage(byte[] body, String appId) {
        super(body, new MessageProperties());
        this.getMessageProperties().setAppId(appId);
    }

    public EventMessage(Object body, String appId) {
        super(Serializer.objectToArrayBytes(body), new MessageProperties());
        this.getMessageProperties().setAppId(appId);
    }

    public EventMessage(byte[] body, MessageProperties messageProperties) {
        super(body, messageProperties);
    }

    public EventMessage(Object body, MessageProperties messageProperties) {
        super(Serializer.objectToArrayBytes(body), messageProperties);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}