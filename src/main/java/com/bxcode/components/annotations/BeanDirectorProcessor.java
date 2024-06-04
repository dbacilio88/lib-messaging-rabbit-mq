package com.bxcode.components.annotations;

import com.bxcode.services.contracts.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * BeanDirectorProcessor
 * <p>
 * BeanDirectorProcessor class.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 31/05/2024
 */
@Log4j2
@Component
public class BeanDirectorProcessor implements BeanPostProcessor {

    private final ICallBackRegisterService callBackRegisterService;
    private final IConsumerRegisterService consumerRegisterService;
    private final IExchangeRegisterService exchangeRegisterService;
    private final IQueueRegisterService queueRegisterService;
    private final IBindingRegisterService bindingRegisterService;
    private final IConfirmRegisterService confirmRegisterService;

    public BeanDirectorProcessor(
            final ICallBackRegisterService callBackRegisterService,
            final IConsumerRegisterService consumerRegisterService,
            final IExchangeRegisterService exchangeRegisterService,
            final IQueueRegisterService queueRegisterService,
            final IBindingRegisterService bindingRegisterService,
            final IConfirmRegisterService confirmRegisterService) {
        this.callBackRegisterService = callBackRegisterService;
        this.consumerRegisterService = consumerRegisterService;
        this.exchangeRegisterService = exchangeRegisterService;
        this.queueRegisterService = queueRegisterService;
        this.bindingRegisterService = bindingRegisterService;
        this.confirmRegisterService = confirmRegisterService;
        log.debug("BeanDirectorProcessor loaded successfully");
    }

    @Override
    @Nullable
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        log.info("postProcessBeforeInitialization bean name {}", bean);
        callBackRegisterService.register(bean, beanName);
        consumerRegisterService.register(bean, beanName);
        exchangeRegisterService.register(bean, beanName);
        queueRegisterService.register(bean, beanName);
        bindingRegisterService.register(bean, beanName);
        confirmRegisterService.register(bean, beanName);
        return bean;
    }

    @Override
    @Nullable
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("postProcessAfterInitialization bean name {}", bean);
        return bean;
    }
}


