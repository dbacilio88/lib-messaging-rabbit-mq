package com.bacsystem.utils.services.dispatch;


import com.bacsystem.utils.components.base.MQBase;
import com.bacsystem.utils.dto.MQEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * MQConsumerDispatch
 * <p>
 * MQConsumerDispatch class.
 * <p>
 * This class specifies the requirements for the MQConsumerDispatch component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

@Log4j2
@Service
public class MQConsumerDispatch extends MQBase implements IMQConsumerDispatch {
    public MQConsumerDispatch() {
        super(MQConsumerDispatch.class.getSimpleName());
    }

    @Override
    public void callBack(Object object, Method method, MQEvent<?> message) throws InvocationTargetException, IllegalAccessException {
        log.debug("dispatching callback consumer");
        method.invoke(object, message);
    }
}
