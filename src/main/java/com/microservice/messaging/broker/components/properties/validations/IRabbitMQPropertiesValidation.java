package com.microservice.messaging.broker.components.properties.validations;


import com.microservice.messaging.broker.components.enums.MQValidation;
import com.microservice.messaging.broker.components.properties.RabbitMQProperties;
import com.microservice.messaging.broker.components.utils.MQUtility;
import com.microservice.messaging.broker.constants.RabbitMQConstant;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;

/**
 * IRabbitMQPropertiesValidation
 * <p>
 * IRabbitMQPropertiesValidation interface.
 * <p>
 * This interface specifies the requirements for the IRabbitMQPropertiesValidation component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */


@FunctionalInterface
public interface IRabbitMQPropertiesValidation extends Function<RabbitMQProperties, RabbitMQPropertiesResult> {

    static IRabbitMQPropertiesValidation validationProperties() {
        return currentProperties -> {

            RabbitMQPropertiesResult result = RabbitMQPropertiesResult.builder()
                    .validation(MQValidation.PROCESS_VALIDATION_ERROR)
                    .errors(new ArrayList<>())
                    .build();

            if (Objects.isNull(currentProperties)) {
                result.getErrors().add("RabbitMQProperties is null");
                return result;
            }

            if (Boolean.TRUE.equals(isInvalid(currentProperties.getHost()))) {
                result.getErrors().add(getErrorMessageByParameter("host"));
            }
            if (currentProperties.getPort() < 0) {
                result.getErrors().add(getErrorMessageByParameter("port"));
            }
            if (Boolean.TRUE.equals(isInvalid(currentProperties.getVirtualHost()))) {
                result.getErrors().add(getErrorMessageByParameter("virtual-host"));
            }
            if (Boolean.TRUE.equals(isInvalid(currentProperties.getUsername()))) {
                result.getErrors().add(getErrorMessageByParameter("username"));
            }
            if (Boolean.TRUE.equals(isInvalid(currentProperties.getPassword()))) {
                result.getErrors().add(getErrorMessageByParameter("password"));
            }
            if (result.getErrors().isEmpty()) {
                result.setValidation(MQValidation.PROCESS_VALIDATION_SUCCESS);
            }
            return result;
        };
    }

    private static Boolean isInvalid(final String properties) {
        return Objects.isNull(properties) || Strings.isEmpty(properties);
    }

    private static String getErrorMessageByParameter(final String properties) {
        return MQUtility.formatMessage(RabbitMQConstant.MESSAGE_ERROR_CONFIGURATION_FORMAT, properties);
    }

    default IRabbitMQPropertiesValidation and(final IRabbitMQPropertiesValidation otherValidation) {
        return currentProperties -> {
            RabbitMQPropertiesResult result = this.apply(currentProperties);
            return result.getValidation().equals(MQValidation.PROCESS_VALIDATION_SUCCESS) ? otherValidation.apply(currentProperties) : result;
        };
    }
}
