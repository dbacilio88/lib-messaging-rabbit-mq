package com.microservice.messaging.broker.services.factory;


import com.microservice.messaging.broker.components.annotations.MQReturnCallBack;
import com.microservice.messaging.broker.components.base.MQBase;
import com.microservice.messaging.broker.components.exceptions.MQBrokerException;
import com.microservice.messaging.broker.dto.MQEvent;
import com.microservice.messaging.broker.services.builder.IMQCallBackBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static com.microservice.messaging.broker.constants.RabbitMQConstant.*;

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
    private final IMQCallBackBuilder callBackBuilder;

    private RabbitTemplate.ReturnsCallback callback;

    public MQCallBack(final RabbitTemplate rabbitTemplate,
                      final IMQCallBackBuilder callBackBuilder) {
        super(MQCallBack.class.getSimpleName());
        this.rabbitTemplate = rabbitTemplate;
        this.callBackBuilder = callBackBuilder;
    }

    @Override
    public void register(Object bean, String name) {
        final Stream<Method> methods = Arrays.stream(bean.getClass().getMethods());
        methods.parallel().forEach(method -> {
            MQReturnCallBack annotation = method.getAnnotation(MQReturnCallBack.class);
            if (annotation != null && this.callback == null) {
                log.debug("validating return rollback service: {}", annotation);
                log.debug("setting returnCallBack bean");
                final Class<?>[] clazz = method.getParameterTypes();
                if (clazz.length != NUMBER_PARAMETER_TYPES_REQUIRED) {
                    throw new MQBrokerException("Parameters of @ReturnCallBack should be (code:int, text:String, message:Event)");
                } else {
                    if (!int.class.equals(clazz[PARAMETER_TYPES_REQUIRED_FIRST_INDEX])) {
                        throw new MQBrokerException("Parameters 1 should be (code:int)");
                    }

                    if (!String.class.equals(clazz[PARAMETER_TYPES_REQUIRED_SECOND_INDEX])) {
                        throw new MQBrokerException("Parameters 2 should be (text:String)");
                    }

                    if (!MQEvent.class.equals(clazz[PARAMETER_TYPES_REQUIRED_THIRD_INDEX])) {
                        throw new MQBrokerException("Parameters 3 should be (message:Event)");
                    }
                }
                this.callback = this.callBackBuilder.build(method, bean);
                this.rabbitTemplate.setMandatory(true);
                this.rabbitTemplate.setReturnsCallback(this.callback);

            }

        });
    }

    @Override
    @Async
    public void execute(Message message, int code, String text, String exchange, String routingKey) {
        log.debug("executing return call back");
        if (!Objects.isNull(callback)) {
            var returnedMessage = new ReturnedMessage(message, code, text, exchange, routingKey);
            this.callback.returnedMessage(returnedMessage);
        }
    }
}
