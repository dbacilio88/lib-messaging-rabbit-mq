package com.bxcode.services.contracts;

import com.bxcode.components.annotations.BrokerDeclareBinding;
import com.bxcode.components.annotations.BrokerDeclareQueue;
import com.rabbitmq.client.AMQP;

import java.util.List;

/**
 * IConsumerRegisterService
 * <p>
 * IConsumerRegisterService interface.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 1/06/2024
 */
public interface IConsumerRegisterService extends IRegisterService {
    List<AMQP.Queue.BindOk> executeBindings(String queue, BrokerDeclareBinding[] bindings);
}
