package com.microservice.messaging.broker.services.factory;


import com.microservice.messaging.broker.components.annotations.MQConfirmCallBack;
import com.microservice.messaging.broker.components.annotations.MQDeclareBinding;
import com.microservice.messaging.broker.components.annotations.MQDeclareExchange;
import com.microservice.messaging.broker.components.annotations.MQDeclareQueue;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * BrokerRegistry
 * <p>
 * BrokerRegistry class.
 * <p>
 * This class specifies the requirements for the BrokerRegistry component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 11/10/2024
 */

@Log4j2
@Component
public class BrokerRegistry {

    private final ApplicationContext applicationContext;

    public BrokerRegistry(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    public void registerExchange(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(MQDeclareExchange.class)) {
            log.info("Registering exchange beanName {}", beanName);
            IMQExchange exchangeRegistry = applicationContext.getBean(IMQExchange.class);
            final MQDeclareExchange annotation = beanClass.getAnnotation(MQDeclareExchange.class);
            for (String t : annotation.exchanges()) {
                exchangeRegistry.register(t, annotation);
            }
            log.info("Registered exchanges successfully");
        }
    }

    public void registerQueue(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(MQDeclareQueue.class)) {
            log.info("Registering queue beanName {}", beanName);
            IMQQueue queueRegistry = applicationContext.getBean(IMQQueue.class);
            final MQDeclareQueue annotation = beanClass.getAnnotation(MQDeclareQueue.class);
            for (String q : annotation.queues()) {
                queueRegistry.register(q, annotation);
            }
            log.info("Registered queue successfully");
        }
    }

    public void registerBinding(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(MQDeclareBinding.class)) {
            log.info("Registering binding beanName {}", beanName);
            IMQBinding bindingRegistry = applicationContext.getBean(IMQBinding.class);
            final MQDeclareBinding annotation = beanClass.getAnnotation(MQDeclareBinding.class);
            bindingRegistry.register(annotation.exchange(), annotation);
            log.info("Registered binding successfully");
        }
    }

    public void registerConfirm(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(MQConfirmCallBack.class)) {
            log.info("Registering confirm callback beanName {}", beanName);
            IMQConfirme confirmeRegistry = applicationContext.getBean(IMQConfirme.class);
            final MQConfirmCallBack annotation = beanClass.getAnnotation(MQConfirmCallBack.class);
            Stream<Method> methodStream = Arrays.stream(beanClass.getDeclaredMethods());
            methodStream.forEach(method -> {
                log.info("method {}", method);
                confirmeRegistry.register(method);

            });
            log.info("Registered confirm successfully");
        }
    }
}
