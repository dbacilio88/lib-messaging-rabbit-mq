package com.bxcode.services.contracts;

import com.bxcode.components.annotations.BrokerDeclareQueue;

/**
 * IQueueRegisterService
 * <p>
 * IQueueRegisterService interface.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 1/06/2024
 */
public interface IQueueRegisterService extends IRegisterService {
    void declare(BrokerDeclareQueue queue);
}
