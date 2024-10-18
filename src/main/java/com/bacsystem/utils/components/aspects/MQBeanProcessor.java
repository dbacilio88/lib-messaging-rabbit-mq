package com.bacsystem.utils.components.aspects;


import com.bacsystem.utils.services.factory.BrokerRegistry;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * MQBeanProcessor
 * <p>
 * MQBeanProcessor class.
 * <p>
 * This class specifies the requirements for the MQBeanProcessor component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 11/10/2024
 */

@Log4j2
@Component
public class MQBeanProcessor implements BeanPostProcessor {

    private final BrokerRegistry brokerRegistry;

    public MQBeanProcessor(BrokerRegistry brokerRegistry) {
        this.brokerRegistry = brokerRegistry;
    }


    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        log.debug("Processing bean: {}, name: {}", bean, beanName);
        brokerRegistry.registerCallback(bean, beanName);
        brokerRegistry.registerExchange(bean, beanName);
        brokerRegistry.registerConsumer(bean, beanName);
        brokerRegistry.registerQueue(bean, beanName);
        brokerRegistry.registerBinding(bean, beanName);
        brokerRegistry.registerConfirm(bean, beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        return bean;
    }
}
