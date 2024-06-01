package com.bxcode.components.enums;

import lombok.Getter;

/**
 * ExchangeType
 * <p>
 * ExchangeType enum.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 31/05/2024
 */
@Getter
public enum ExchangeType {

    DIRECT("direct"),
    HEADERS("headers"),
    FANOUT("fanout"),
    TOPIC("topic");

    private final String type;

    ExchangeType(String type) {
        this.type = type;
    }
}
