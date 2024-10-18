package com.bacsystem.utils.services.factory;


import com.bacsystem.utils.components.annotations.MQReturnCallBack;
import com.bacsystem.utils.components.base.MQBase;
import com.bacsystem.utils.components.events.IMQEventMessageService;
import com.bacsystem.utils.components.events.messages.implementations.MQMessagingReturnsCallback;
import com.bacsystem.utils.components.exceptions.MQBrokerException;
import com.bacsystem.utils.dto.MQEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Optional;

import static com.bacsystem.utils.constants.RabbitMQConstant.*;

/**
 * MQCallBack
 * <p>
 * MQCallBack class.
 * <p>
 * This class specifies the requirements for the MQCallBack component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */


@Log4j2
@Service
public class MQCallBack extends MQBase implements IMQCallBack {

    private final RabbitTemplate rabbitTemplate;
    private final IMQEventMessageService eventMessageService;
    private RabbitTemplate.ReturnsCallback callback;

    public MQCallBack(
            final RabbitTemplate rabbitTemplate,
            final IMQEventMessageService eventMessageService
    ) {
        super(MQCallBack.class.getSimpleName());
        this.rabbitTemplate = rabbitTemplate;
        this.eventMessageService = eventMessageService;
    }

    @Override
    public void register(Method method, Object bean) {
        Optional<MQReturnCallBack> annotationValidate = Optional.ofNullable(method.getAnnotation(MQReturnCallBack.class));
        if (annotationValidate.isPresent() && this.callback == null) {
            validateParameters(method);
            this.callback = build(method, bean);
            this.rabbitTemplate.setMandatory(true);
            this.rabbitTemplate.setReturnsCallback(this.callback);
        }
    }

    private void validateParameters(final Method method) {
        final Class<?>[] paramTypes = method.getParameterTypes();

        if (paramTypes.length != NUMBER_PARAMETER_TYPES_REQUIRED) {
            throw new MQBrokerException("Parameters of @ReturnCallBack should be (code:int, text:String, message:Event)");
        }

        if (!int.class.equals(paramTypes[PARAMETER_TYPES_REQUIRED_FIRST_INDEX])) {
            throw new MQBrokerException("Parameter 1 should be (code:int)");
        }

        if (!String.class.equals(paramTypes[PARAMETER_TYPES_REQUIRED_SECOND_INDEX])) {
            throw new MQBrokerException("Parameter 2 should be (text:String)");
        }

        if (!MQEvent.class.equals(paramTypes[PARAMETER_TYPES_REQUIRED_THIRD_INDEX])) {
            throw new MQBrokerException("Parameter 3 should be (message:Event)");
        }
    }

    @Override
    @Async
    public void execute(Message message, int code, String text, String exchange, String routingKey) {
        log.info("executing return call back");
        if (callback != null) {
            var returnedMessage = new ReturnedMessage(message, code, text, exchange, routingKey);
            this.callback.returnedMessage(returnedMessage);
        }
    }

    @Override
    public RabbitTemplate.ReturnsCallback build(Method method, Object bean) {
        return new MQMessagingReturnsCallback(method, bean, eventMessageService);
    }
}
