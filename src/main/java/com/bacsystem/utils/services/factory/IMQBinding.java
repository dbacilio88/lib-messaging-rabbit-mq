package com.bacsystem.utils.services.factory;


import com.bacsystem.utils.components.annotations.MQDeclareBinding;

/**
 * IMQBinding
 * <p>
 * IMQBinding interface.
 * <p>
 * This interface specifies the requirements for the IMQBinding component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 11/10/2024
 */

public interface IMQBinding {
    void register(MQDeclareBinding annotation);
}
