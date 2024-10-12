package com.microservice.messaging.broker.services.dispatch;


import com.microservice.messaging.broker.dto.MQEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * IMQConsumerDispatch
 * <p>
 * IMQConsumerDispatch interface.
 * <p>
 * This interface specifies the requirements for the IMQConsumerDispatch component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

public interface IMQConsumerDispatch {
    void callBack(Object object, Method method, MQEvent<?> message) throws InvocationTargetException, IllegalAccessException;
}
