package com.bacsystem.utils.components.events.messages;


import com.bacsystem.utils.components.utils.MQUtility;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.io.Serial;
import java.io.Serializable;

/**
 * MQEventMessage
 * <p>
 * MQEventMessage class.
 * <p>
 * This class specifies the requirements for the MQEventMessage component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */


public class MQEventMessage extends Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 8434157279978371241L;

    public MQEventMessage(byte[] body) {
        super(body);
    }

    public MQEventMessage(byte[] body, String id) {
        super(body);
        this.getMessageProperties().setAppId(id);
    }

    public MQEventMessage(Object body, String id) {
        super(MQUtility.objectToArrayBytes(body), new MessageProperties());
        this.getMessageProperties().setAppId(id);
    }

    public MQEventMessage(byte[] body, MessageProperties properties) {
        super(body, properties);
    }

    public MQEventMessage(Object body, MessageProperties messageProperties) {
        super(MQUtility.objectToArrayBytes(body), messageProperties);
    }
}

