package com.bacsystem.utils.components.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MQBrokerProducer
 * <p>
 * MQBrokerProducer @interface.
 * <p>
 * This @interface specifies the requirements for the MQBrokerProducer component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MQBrokerProducer {

    String routingKey() default "";

    String exchange();

    MQDeclareExchange[] declareExchanges() default {};
}