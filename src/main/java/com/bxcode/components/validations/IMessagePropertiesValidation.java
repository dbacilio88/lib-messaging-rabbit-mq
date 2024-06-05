package com.bxcode.components.validations;

import com.bxcode.components.enums.PropertiesValidationResult;
import org.springframework.amqp.core.MessageProperties;

import java.util.Objects;
import java.util.function.Function;

/**
 * IMessagePropertiesValidation
 * <p>
 * IMessagePropertiesValidation interface.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 5/06/2024
 */

@FunctionalInterface
public interface IMessagePropertiesValidation extends Function<MessageProperties, PropertiesValidationResult> {

    static IMessagePropertiesValidation validateReplyTo() {
        return messageProperties -> {
            if (Objects.isNull(messageProperties) || Objects.isNull(messageProperties.getReplyTo()) || messageProperties.getReplyTo().trim().isEmpty()) {
                return PropertiesValidationResult.PROCESS_VALIDATION_FAIL;
            }
            return PropertiesValidationResult.PROCESS_VALIDATION_SUCCESS;
        };
    }

    static IMessagePropertiesValidation validateCorrelationId() {
        return messageProperties -> {
            if (Objects.isNull(messageProperties) || Objects.isNull(messageProperties.getCorrelationId()) || messageProperties.getCorrelationId().trim().isEmpty()) {
                return PropertiesValidationResult.PROCESS_VALIDATION_FAIL;
            }
            return PropertiesValidationResult.PROCESS_VALIDATION_SUCCESS;
        };
    }

    default IMessagePropertiesValidation and(IMessagePropertiesValidation other) {
        return messageProperties -> {
            PropertiesValidationResult propertiesValidationResult = this.apply(messageProperties);
            return propertiesValidationResult.equals(PropertiesValidationResult.PROCESS_VALIDATION_SUCCESS) ? other.apply(messageProperties) : propertiesValidationResult;
        };
    }
}
