package com.bxcode.models.implementations;

import com.bxcode.dto.Event;
import com.bxcode.models.contract.IRabbitCallback;
import com.bxcode.services.contracts.IEventMapperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.ReturnedMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

import static com.bxcode.constants.ProcessConstant.PARAMETER_NUMBER_TYPES_REQUIRED_FIRST_INDEX;

/**
 * RabbitCallback
 * <p>
 * RabbitCallback class.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 1/06/2024
 */

@Log4j2
public class RabbitCallback implements IRabbitCallback {

    private final IEventMapperService mapperService;
    private final Method method;
    private final Object bean;

    public RabbitCallback(final IEventMapperService mapperService, final Method method, final Object bean) {
        this.mapperService = mapperService;
        this.method = method;
        this.bean = bean;
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.debug("message returned: {}", returnedMessage);
        log.debug("raise method: {}", method.getName());
        Parameter parameter = method.getParameters()[PARAMETER_NUMBER_TYPES_REQUIRED_FIRST_INDEX];
        Type type = parameter.getParameterizedType();
        try {
            final Event<?> event = mapperService.event(returnedMessage.getMessage(), type);
            method.invoke(bean, returnedMessage.getReplyCode(), returnedMessage.getReplyText(), event);
        } catch (JsonProcessingException | IllegalAccessException | InvocationTargetException e) {
            log.error("Exception, message: {},trace: {}", e.getMessage(), e);
        }
    }
}