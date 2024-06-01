package com.bxcode.services.contracts;

import com.bxcode.dto.Event;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * IConsumerDispatchService
 * <p>
 * IConsumerDispatchService interface.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 1/06/2024
 */
public interface IConsumerDispatchService {

    void callBack(Object object, Method method, Event<?> event) throws InvocationTargetException, IllegalAccessException;
}
