package com.microservice.messaging.broker.components.base;


import lombok.extern.log4j.Log4j2;

/**
 * MQBase
 * <p>
 * MQBase class.
 * <p>
 * This class specifies the requirements for the MQBase component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 11/10/2024
 */
@Log4j2
public abstract class MQBase {
    public MQBase(String name) {
        log.debug("load {} successfully", name);
    }
}
