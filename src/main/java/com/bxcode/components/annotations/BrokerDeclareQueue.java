package com.bxcode.components.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * BrokerDeclareQueue
 * <p>
 * BrokerDeclareQueue interface annotation.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 31/05/2024
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BrokerDeclareQueue {

    String[] queues();

    boolean autoAck() default true;

    boolean durable() default true;

    boolean exclusive() default false;

    boolean autoDelete() default false;

    QueueProperty[] queueProperties() default {};

}
