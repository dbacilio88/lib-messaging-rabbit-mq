package com.bacsystem.utils.services.factory;


import com.bacsystem.utils.dto.MQEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * IMQConfirme
 * <p>
 * IMQConfirme interface.
 * <p>
 * This interface specifies the requirements for the IMQConfirme component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

public interface IMQConfirme {
    void confirm(MQEvent<?> event) throws InvocationTargetException, IllegalAccessException;

    void register(Method method, Object bean);
}
