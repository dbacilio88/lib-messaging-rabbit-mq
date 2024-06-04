package com.bxcode.services.implementations;

import com.bxcode.dto.Event;
import com.bxcode.services.contracts.IConsumerDispatchService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ConsumerDispatchService
 * <p>
 * ConsumerDispatchService class.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 4/06/2024
 */

@Log4j2
@Service
public class ConsumerDispatchService implements IConsumerDispatchService {

    public ConsumerDispatchService() {
        log.debug("ConsumerDispatchService loaded successfully");
    }

    @Override
    public void callBack(Object object, Method method, Event<?> event) throws InvocationTargetException, IllegalAccessException {
        log.debug("dispatching callback consumer");
        method.invoke(object, event);
    }
}


