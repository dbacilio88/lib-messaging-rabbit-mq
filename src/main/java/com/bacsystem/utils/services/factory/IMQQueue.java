package com.bacsystem.utils.services.factory;


import com.bacsystem.utils.components.annotations.MQDeclareQueue;

/**
 * IMQQueue
 * <p>
 * IMQQueue interface.
 * <p>
 * This interface specifies the requirements for the IMQQueue component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 11/10/2024
 */

public interface IMQQueue {
    void register(String queue, MQDeclareQueue annotation);

    void declare(String queue, MQDeclareQueue annotation);
}
