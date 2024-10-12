package com.microservice.messaging.broker.services.factory;


import com.microservice.messaging.broker.components.annotations.MQConfirmCallBack;
import com.microservice.messaging.broker.components.base.MQBase;
import com.microservice.messaging.broker.components.exceptions.MQBrokerException;
import com.microservice.messaging.broker.dto.MQEvent;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static com.microservice.messaging.broker.constants.RabbitMQConstant.PARAMETER_NUMBER_TYPES_REQUIRED_CONFIRM;
import static com.microservice.messaging.broker.constants.RabbitMQConstant.PARAMETER_NUMBER_TYPES_REQUIRED_FIRST_INDEX;

/**
 * MQConfirme
 * <p>
 * MQConfirme class.
 * <p>
 * This class specifies the requirements for the MQConfirme component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */


@Log4j2
@Getter
@Service
public class MQConfirme extends MQBase implements IMQConfirme {
    private Object bean;
    private Method method;

    public MQConfirme() {
        super(MQConfirme.class.getSimpleName());
    }


    @Async
    @Override
    public void confirm(MQEvent<?> event) throws InvocationTargetException, IllegalAccessException {
        if (!Objects.isNull(method) && !Objects.isNull(bean)) {
            method.invoke(event);
        }
    }

    @Override
    public void register(Method method) {

    }

    @Override
    public void register(Object bean, String name) {

        final Stream<Method> methodStream = Arrays.stream(bean.getClass().getMethods());
        methodStream.parallel().forEach(method -> {
            final MQConfirmCallBack confirmCallBack = method.getAnnotation(MQConfirmCallBack.class);
            if (confirmCallBack != null) {
                log.info("Registering MQConfirme name annotation {}", confirmCallBack);
                if (Objects.isNull(this.method) && Objects.isNull(this.bean)) {
                    log.info("validating confirm callback service if: {}", confirmCallBack);
                    log.info("setting confirmCallBack bean");
                    final Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length != PARAMETER_NUMBER_TYPES_REQUIRED_CONFIRM) {
                        log.error("invalid confirm callback service 1: {}", confirmCallBack);
                    } else {
                        if (!MQEvent.class.equals(parameterTypes[PARAMETER_NUMBER_TYPES_REQUIRED_FIRST_INDEX])) {
                            log.error("invalid confirm callback service 0 : {}", confirmCallBack);
                            throw new MQBrokerException("invalid confirm callback service: " + confirmCallBack);
                        }
                        this.method = method;
                        this.bean = bean;
                    }
                }
            } else {
                log.debug("validating confirm callback service else:");
            }
        });
    }

}
