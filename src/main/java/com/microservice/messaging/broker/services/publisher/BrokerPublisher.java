package com.microservice.messaging.broker.services.publisher;


import com.microservice.messaging.broker.components.base.MQBase;
import com.microservice.messaging.broker.dto.MQEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * BrokerPublisher
 * <p>
 * BrokerPublisher class.
 * <p>
 * This class specifies the requirements for the BrokerPublisher component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

@Log4j2
@Service
public class BrokerPublisher extends MQBase implements IBrokerPublisher {
    public BrokerPublisher() {
        super(BrokerPublisher.class.getSimpleName());
    }

    @Override
    public void failedHandle() {
        log.info("failedHandle");
    }

    @Override
    public void raiseEvent(MQEvent<?> event) {
        log.info("raiseEvent");
    }
}
