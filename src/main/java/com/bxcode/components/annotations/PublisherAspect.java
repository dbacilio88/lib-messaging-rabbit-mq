package com.bxcode.components.annotations;

import com.bxcode.components.exceptions.ParameterException;
import com.bxcode.components.helpers.Environments;
import com.bxcode.dto.Event;
import com.bxcode.services.contracts.IPublisherService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * PublisherAspect
 * <p>
 * PublisherAspect class.
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
@Aspect
@Component
@Order(1)
public class PublisherAspect {
    @Value("${spring.application.name}")
    private String appName;
    private final Environments environments;
    private final IPublisherService publisherService;

    public PublisherAspect(final Environments environments, final IPublisherService publisherService) {
        this.environments = environments;
        this.publisherService = publisherService;
        log.debug("PublisherAspect loaded successfully");
    }

    @SneakyThrows
    @Around("@annotation(com.bxcode.components.annotations.BrokerPublisher)")
    public Object prepare(ProceedingJoinPoint joinPoint) {

        log.debug("validating method publisher value publisher");
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Method method = methodSignature.getMethod();

        log.debug("return type: {}", method.getReturnType());

        if (!method.getReturnType().equals(Event.class)) {
            throw new ParameterException("return type should be com.novo.utils.messaging.dtos.BrokerMessage Type");
        }

        if (isVoidType(method.getReturnType())) {
            throw new ParameterException("return type should not be void or Void Type");
        }

        log.debug("publishing event: {}", joinPoint);
        final var event = (Event<?>) joinPoint.proceed();
        final BrokerPublisher annotation = method.getAnnotation(BrokerPublisher.class);
        final String exchangeName = environments.get(annotation.exchange());

        var confirmed = publisherService.publish(exchangeName, event);
        log.info("published confirmed? {}", confirmed);
        return event;
    }

    public boolean isVoidType(Type type) {
        return type.equals(Void.TYPE) || type.equals(Void.class);
    }
}