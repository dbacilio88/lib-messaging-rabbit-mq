package com.bxcode.components.helpers;

import com.bxcode.constants.MessageConstant;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * BrokerException
 * <p>
 * BrokerException class.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 31/05/2024
 */

@Log4j2
@UtilityClass
public class BrokerException {

    public static String processValidationError(String errorFormat, String value) {
        return StringFormat.formatMessage(errorFormat, value);
    }

    public static String processValidationError(String errorFormat, String name, String value) {
        return StringFormat.formatMessage(errorFormat, name, value);
    }

    public static String processValidationError(String errorFormat, Object... values) {
        return StringFormat.formatMessage(errorFormat, values);
    }

    public static String processValidationError(final List<String> errors) {
        final StringBuilder sb = new StringBuilder();
        sb.append("validations error: ");
        for (String m : errors) {
            sb.append(StringFormat.formatMessage(MessageConstant.MESSAGE_ERROR_VALIDATION_FORMAT, m));
        }
        return sb.toString();
    }
}