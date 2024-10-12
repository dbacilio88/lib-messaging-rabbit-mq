package com.microservice.messaging.broker.components.properties.validations;


import com.microservice.messaging.broker.components.enums.MQValidation;
import org.springframework.amqp.core.MessageProperties;

import java.util.Objects;
import java.util.function.Function;

/**
 * IRabbitMessagePropertiesValidation
 * <p>
 * IRabbitMessagePropertiesValidation interface.
 * <p>
 * This interface specifies the requirements for the IRabbitMessagePropertiesValidation component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */


@FunctionalInterface
public interface IRabbitMessagePropertiesValidation extends Function<MessageProperties, MQValidation> {
    static IRabbitMessagePropertiesValidation validateReplyTo() {
        return messageProperties -> {
            if (Objects.isNull(messageProperties)
                    || Objects.isNull(messageProperties.getReplyTo())
                    || messageProperties.getReplyTo().trim().isEmpty()) {
                return MQValidation.PROCESS_VALIDATION_ERROR;
            }
            return MQValidation.PROCESS_VALIDATION_SUCCESS;
        };
    }

    static IRabbitMessagePropertiesValidation validateCorrelationId() {
        return messageProperties -> {
            if (Objects.isNull(messageProperties)
                    || Objects.isNull(messageProperties.getCorrelationId())
                    || messageProperties.getCorrelationId().trim().isEmpty()) {
                return MQValidation.PROCESS_VALIDATION_ERROR;
            }
            return MQValidation.PROCESS_VALIDATION_SUCCESS;
        };
    }

    default IRabbitMessagePropertiesValidation and(IRabbitMessagePropertiesValidation otherValidation) {
        return messageProperties -> {
            MQValidation result = this.apply(messageProperties);
            return result.equals(MQValidation.PROCESS_VALIDATION_SUCCESS) ? otherValidation.apply(messageProperties) : result;
        };
    }
}
