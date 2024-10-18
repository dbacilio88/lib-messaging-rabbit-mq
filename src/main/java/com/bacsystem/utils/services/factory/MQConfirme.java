package com.bacsystem.utils.services.factory;


import com.bacsystem.utils.components.annotations.MQConfirmCallBack;
import com.bacsystem.utils.components.base.MQBase;
import com.bacsystem.utils.components.exceptions.MQBrokerException;
import com.bacsystem.utils.dto.MQEvent;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

import static com.bacsystem.utils.constants.RabbitMQConstant.PARAMETER_NUMBER_TYPES_REQUIRED_CONFIRM;
import static com.bacsystem.utils.constants.RabbitMQConstant.PARAMETER_NUMBER_TYPES_REQUIRED_FIRST_INDEX;

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
            method.invoke(bean, event);
        }
    }

    @Override
    public void register(Method method, Object bean) {
        Optional<MQConfirmCallBack> confirmCallBackOpt = Optional.ofNullable(method.getAnnotation(MQConfirmCallBack.class));
        if (confirmCallBackOpt.isPresent()) {
            log.debug("Registering MQConfirme name annotation {}", confirmCallBackOpt.get());
            if (this.method == null && this.bean == null) {
                log.debug("Validating confirm callback service: {}", confirmCallBackOpt.get());
                validateParameters(method);
                this.method = method;
                this.bean = bean;
            }
            log.debug("register confirm method {}", this.method);
            log.debug("register confirm bean {}", this.bean);
        } else {
            log.debug("No confirm callback annotation found.");
        }
    }

    private void validateParameters(final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();

        if (parameterTypes.length != PARAMETER_NUMBER_TYPES_REQUIRED_CONFIRM) {
            log.error("Invalid confirm callback service: expected {} parameters, got {}", PARAMETER_NUMBER_TYPES_REQUIRED_CONFIRM, parameterTypes.length);
            throw new MQBrokerException("Invalid confirm callback service: expected " + PARAMETER_NUMBER_TYPES_REQUIRED_CONFIRM + " parameters.");
        }

        if (!MQEvent.class.equals(parameterTypes[PARAMETER_NUMBER_TYPES_REQUIRED_FIRST_INDEX])) {
            log.error("Invalid confirm callback service parameter type: expected MQEvent, got {}", parameterTypes[PARAMETER_NUMBER_TYPES_REQUIRED_FIRST_INDEX]);
            throw new MQBrokerException("Invalid confirm callback service: first parameter must be MQEvent.");
        }
    }
}
