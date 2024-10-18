package com.bacsystem.utils.components.enums;


import lombok.Getter;

/**
 * MQExchangesType
 * <p>
 * MQExchangesType enum.
 * <p>
 * This enum specifies the requirements for the MQExchangesType component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 11/10/2024
 */

@Getter
public enum MQExchangesType {

    DIRECT("direct"),
    HEADERS("headers"),
    FANOUT("fanout"),
    TOPIC("topic");

    private final String value;

    MQExchangesType(final String value) {
        this.value = value;
    }
}
