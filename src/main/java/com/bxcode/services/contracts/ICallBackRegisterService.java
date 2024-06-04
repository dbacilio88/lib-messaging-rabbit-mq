package com.bxcode.services.contracts;

import org.springframework.amqp.core.Message;

/**
 * ICallBackRegisterService
 * <p>
 * ICallBackRegisterService interface.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 1/06/2024
 */
public interface ICallBackRegisterService extends IRegisterService {
    void executeReturn(Message message, int replyCode, String replyText, String exchange, String routingKey);
}
