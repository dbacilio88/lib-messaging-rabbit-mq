package com.microservice.messaging.broker.components.annotations;


import com.microservice.messaging.broker.components.base.MQBase;
import com.microservice.messaging.broker.components.environments.MQEnvironment;
import com.microservice.messaging.broker.components.exceptions.MQBrokerException;
import com.microservice.messaging.broker.dto.MQEvent;
import com.microservice.messaging.broker.services.IProducerService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * MQPublisherAspect
 * <p>
 * MQPublisherAspect class.
 * <p>
 * This class specifies the requirements for the MQPublisherAspect component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */


@Log4j2
@Aspect
@Component
public class MQPublisherAspect extends MQBase {

    private final MQEnvironment environment;
    private final IProducerService producerService;

    public MQPublisherAspect(final MQEnvironment environment,
                             final IProducerService producerService
    ) {
        super(MQPublisherAspect.class.getSimpleName());
        this.environment = environment;
        this.producerService = producerService;
    }

    @SneakyThrows
    @Around("@annotation(com.microservice.messaging.broker.components.annotations.MQBrokerProducer)")
    public Object prepare(final ProceedingJoinPoint joinPoint) {
        log.info("validating method publisher value publisher");
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        log.info("return type: {}", method.getReturnType());
        if (!method.getReturnType().equals(MQEvent.class)) {
            throw new MQBrokerException("return type should be com.novo.utils.messaging.dtos.BrokerMessage Type");
        }
        if (isVoidType(method.getReturnType())) {
            throw new MQBrokerException("return type should be void");
        }
        log.debug("publishing event: {}", joinPoint);
        final var event = (MQEvent<?>) joinPoint.proceed();
        MQBrokerProducer brokerProducer = method.getAnnotation(MQBrokerProducer.class);
        var exchange = this.environment.get(brokerProducer.exchange());
        var confirmed = this.producerService.producer(exchange, event);
        log.info("published confirmed? {}", confirmed);

        return event;
    }

    public boolean isVoidType(Type type) {
        return type.equals(Void.TYPE) || type.equals(Void.class);
    }

}
