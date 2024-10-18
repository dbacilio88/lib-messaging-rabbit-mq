package com.bacsystem.utils.services.factory;


import com.bacsystem.utils.components.annotations.*;
import com.bacsystem.utils.services.IConsumerService;
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

    public void registerCallback(Object bean, String beanName) {
        final Stream<Method> methods = Arrays.stream(bean.getClass().getDeclaredMethods());
        methods.forEach(method -> {
            if (method.isAnnotationPresent(MQReturnCallBack.class)) {
                IMQCallBack callBackRegister = applicationContext.getBean(IMQCallBack.class);
                log.debug("Registering callback method: {}", method.getName());
                callBackRegister.register(method, bean);
                log.debug("Registered return callback successfully {}", beanName);
            }
        });
    }

    public void registerExchange(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(MQDeclareExchange.class)) {
            IMQExchange exchangeRegistry = applicationContext.getBean(IMQExchange.class);
            final MQDeclareExchange annotation = beanClass.getAnnotation(MQDeclareExchange.class);
            for (String t : annotation.exchanges()) {
                exchangeRegistry.register(t, annotation);
            }
            log.debug("Registered exchanges successfully {}", beanName);
        }
    }

    public void registerBinding(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(MQDeclareBinding.class)) {
            IMQBinding bindingRegistry = applicationContext.getBean(IMQBinding.class);
            final MQDeclareBinding annotation = beanClass.getAnnotation(MQDeclareBinding.class);
            bindingRegistry.register(annotation);
            log.debug("Registered binding successfully {}", beanName);
        }
    }


    public void registerQueue(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(MQDeclareQueue.class)) {
            IMQQueue queueRegistry = applicationContext.getBean(IMQQueue.class);
            final MQDeclareQueue annotation = beanClass.getAnnotation(MQDeclareQueue.class);
            for (String q : annotation.queues()) {
                queueRegistry.register(q, annotation);
            }
            log.debug("Registered queue successfully {}", beanName);
        }
    }


    public void registerConfirm(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();

        final Stream<Method> methodStream = Arrays.stream(bean.getClass().getDeclaredMethods());
        methodStream.forEach(method -> {
            if (method.isAnnotationPresent(MQConfirmCallBack.class)) {
                log.info("beanClass {}", beanClass.getName());
                IMQConfirme confirmeRegistry = applicationContext.getBean(IMQConfirme.class);
                confirmeRegistry.register(method, bean);
                log.debug("Registered confirm successfully {}", beanName);
            }

        });

    }

    public void registerConsumer(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        final Method[] methods = beanClass.getDeclaredMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(MQBrokerConsumer.class)) {
                IConsumerService consumerService = applicationContext.getBean(IConsumerService.class);
                consumerService.register(m, bean);
                log.debug("Registered consumer successfully {}", beanName);
            }
        }
    }
}
