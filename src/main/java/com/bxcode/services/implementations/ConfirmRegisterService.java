package com.bxcode.services.implementations;

import com.bxcode.components.annotations.ConfirmCallBack;
import com.bxcode.components.exceptions.ParameterException;
import com.bxcode.dto.Event;
import com.bxcode.services.contracts.IConfirmRegisterService;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static com.bxcode.constants.ProcessConstant.PARAMETER_NUMBER_TYPES_REQUIRED_CONFIRM;
import static com.bxcode.constants.ProcessConstant.PARAMETER_NUMBER_TYPES_REQUIRED_FIRST_INDEX;

/**
 * ConfirmRegisterService
 * <p>
 * ConfirmRegisterService class.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 4/06/2024
 */

@Getter
@Log4j2
@Service
public class ConfirmRegisterService implements IConfirmRegisterService {

    private Method method;
    private Object bean;

    public ConfirmRegisterService() {
        log.debug("ConfirmRegisterService loaded successfully");
    }

    @Override
    public void executeConfirm(Event<?> event) throws InvocationTargetException, IllegalAccessException {
        log.debug("executing confirm call back");
        if (!Objects.isNull(method) && !Objects.isNull(bean)) {
            method.invoke(bean, event);
        }
    }

    @Override
    public void register(Object bean, String nameBean) {
        final Stream<Method> methods = Arrays.stream(bean.getClass().getDeclaredMethods());
        methods.parallel().forEach(method -> {
            final ConfirmCallBack confirmCallBack = method.getAnnotation(ConfirmCallBack.class);
            if (confirmCallBack != null) {
                if (Objects.isNull(this.method) && Objects.isNull(this.bean)) {
                    log.debug("validating confirm callback service: {}", confirmCallBack);
                    log.debug("setting confirmCallBack bean");
                    final Class<?>[] clazz = method.getParameterTypes();
                    if (clazz.length != PARAMETER_NUMBER_TYPES_REQUIRED_CONFIRM) {
                        throw new ParameterException("Parameters of @ConfirmCallBack should be (code:int, text:String, message:Event)");
                    } else {
                        if (!Event.class.equals(clazz[PARAMETER_NUMBER_TYPES_REQUIRED_FIRST_INDEX])) {
                            throw new ParameterException("Parameters 1 should be (message:Event)");
                        }
                        this.method = method;
                        this.bean = bean;
                    }
                } else {
                    log.warn("ConfirmCallBack must be definite once");
                }
            }
        });
    }
}