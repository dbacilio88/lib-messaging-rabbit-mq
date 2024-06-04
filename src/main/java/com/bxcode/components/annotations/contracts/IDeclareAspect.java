package com.bxcode.components.annotations.contracts;

import com.bxcode.components.annotations.BrokerDeclareExchange;
import com.bxcode.components.annotations.BrokerDeclareQueue;
import org.aspectj.lang.JoinPoint;

/**
 * IDeclareAspect
 * <p>
 * IDeclareAspect interface.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 4/06/2024
 */
public interface IDeclareAspect {
    void validateExchangeAnnotation(JoinPoint joinPoint);

    void validateQueueAnnotation(JoinPoint joinPoint);

    void declareQueue(BrokerDeclareQueue annotation);

    void declareExchange(BrokerDeclareExchange annotation);
}
