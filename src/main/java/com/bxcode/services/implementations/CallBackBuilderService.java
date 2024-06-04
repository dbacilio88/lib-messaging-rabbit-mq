package com.bxcode.services.implementations;

import com.bxcode.models.implementations.RabbitCallback;
import com.bxcode.services.contracts.ICallBackBuilderService;
import com.bxcode.services.contracts.IEventMapperService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * CallBackBuilderService
 * <p>
 * CallBackBuilderService class.
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
public class CallBackBuilderService implements ICallBackBuilderService {

    private final IEventMapperService mapperService;

    public CallBackBuilderService(final IEventMapperService mapperService) {
        this.mapperService = mapperService;
        log.debug("CallBackBuilderService loaded successfully");
    }

    @Override
    public RabbitTemplate.ReturnsCallback build(Method method, Object object) {
        log.debug("build method {}", method.toString());
        return new RabbitCallback(mapperService, method, object);
    }
}