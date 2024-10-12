package com.microservice.messaging.broker.components.utils;


import com.microservice.messaging.broker.constants.RabbitMQConstant;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * MQUtility
 * <p>
 * MQUtility class.
 * <p>
 * This class specifies the requirements for the MQUtility component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */


@Log4j2
@UtilityClass
public class MQUtility {

    public static byte[] objectToArrayBytes(Object object) {
        byte[] bytes = new byte[0];
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            ObjectOutputStream oos = new ObjectOutputStream(stream);
            oos.writeObject(object);
            oos.flush();
            bytes = stream.toByteArray();
            return bytes;
        } catch (IOException e) {
            log.error("error in process serializer object to byte[]: {}", e.getMessage());
        }
        return bytes;
    }

    public static String formatMessage(String message, Object... variables) {
        return String.format(message, variables);
    }

    public static String processValidationErrors(final List<String> errors) {
        final StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("validations errors: ");
        for (String currentError : errors) {
            errorMessage.append(String.format(RabbitMQConstant.MESSAGE_VALIDATION_ERROR_FORMAT, currentError));
        }
        return errorMessage.toString();
    }
}

