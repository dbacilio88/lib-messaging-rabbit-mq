package com.bxcode.constants;

import lombok.experimental.UtilityClass;

/**
 * ProcessConstant
 * <p>
 * ProcessConstant class.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 31/05/2024
 */
@UtilityClass
public class MessageConstant {
    public static final String MESSAGE_ERROR_CONFIGURATION_FORMAT = "Error in broker configuration the [ %s ] is null or empty";
    public static final String MESSAGE_ERROR_VALIDATION_FORMAT = " [ %s] ";
    public static final String MESSAGE_ERROR_FIELD_FORMAT = " [ %s] ";
    public static final String MESSAGE_ERROR_FIELD_NOT_ALLOW_LOG_FORMAT = "The field {} is no allow access in class {}";
    public static final String MESSAGE_ERROR_ALL_FIELDS_REQUIRED_IN_INSTANCE_LOG_FORMAT = "error the next fields {} are required in {}";
}


