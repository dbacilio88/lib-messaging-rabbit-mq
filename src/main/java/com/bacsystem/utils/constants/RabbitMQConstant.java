package com.bacsystem.utils.constants;


import lombok.experimental.UtilityClass;

/**
 * RabbitMQConstant
 * <p>
 * RabbitMQConstant class.
 * <p>
 * This class specifies the requirements for the RabbitMQConstant component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */


@UtilityClass
public class RabbitMQConstant {
    public static final String SIMPLE_EMPTY_FORMAT = "";
    public static final String SIMPLE_DOT_FORMAT = ".";
    public static final String SIMPLE_DATE_FORMAT = "yyyyMMddHHmmssSS";
    public static final String PARAMETER_CHANNEL = "channel";
    public static final String PARAMETER_METHOD = "method";
    public static final String PARAMETER_BEAN = "bean";
    public static final String PARAMETER_QUEUE_NAME = "queueName";
    public static final String PARAMETER_DEFAULT_RECEIVED_QUEUE = "received";
    public static final int PARAMETER_NUMBER_TYPES_REQUIRED = 3;
    public static final int PARAMETER_NUMBER_TYPES_REQUIRED_CONFIRM = 1;
    public static final int PARAMETER_NUMBER_TYPES_REQUIRED_FIRST_INDEX = 0;
    public static final int PARAMETER_NUMBER_TYPES_REQUIRED_SECOND_INDEX = 1;
    public static final int PARAMETER_NUMBER_TYPES_REQUIRED_THIRD_INDEX = 2;
    public static final int PARAMETER_NUMBER_REPLAY_CODE = 500;
    public static final String MESSAGE_ERROR_CONFIGURATION_FORMAT = "Error in broker configuration the [ %s ] is null or empty";
    public static final String MESSAGE_VALIDATION_ERROR_FORMAT = " [ %s ] ";
    public static final String MESSAGE_FIELD_ERROR_FORMAT = " [ %s ] ";
    public static final String MESSAGE_FIELD_ERROR_NOT_ALLOW_LOG_FORMAT = "the field {} is no allow access in class {}";
    public static final String MESSAGE_ALL_FIELDS_ERROR_REQUIRED_IN_INSTANCE_LOG_FORMAT = "error the next fields {} are required in {}";
    public static final int NUMBER_PARAMETER_TYPES_REQUIRED = 3;
    public static final int NUMBER_PARAMETER_TYPES_REQUIRED_CONFIRM = 1;
    public static final int PARAMETER_TYPES_REQUIRED_FIRST_INDEX = 0;
    public static final int PARAMETER_TYPES_REQUIRED_SECOND_INDEX = 1;
    public static final int PARAMETER_TYPES_REQUIRED_THIRD_INDEX = 2;
    public static final int GENERAL_REPLY_CODE = 500;
    public static final String BROKER_GENERAL_EXCHANGE_NAME = "${broker-common-configuration.general-exchange:NP_GENERAL_EXCHANGE_RANDOM}";
    public static final String BROKER_GENERAL_EXCHANGE_TYPE = "${broker-common-configuration.general-exchange-type:TOPIC}";
    public static final String BROKER_GENERAL_GENERAL_METADATA_QUEUE = "${broker-common-configuration.general-metadata-queue:EVENTS-METADATA}";
    public static final String BROKER_GENERAL_GENERAL_RECEIVED_QUEUE = "${broker-common-configuration.general-received-queue:received}";
    public static final String BROKER_GENERAL_GENERAL_ROUTING_KEY = "${broker-common-configuration.general-routing-key:*.*.*.*.*.metadata}";
    public static final String BROKER_GENERAL_METADATA_QUEUE = "EVENTS_METADATA";
    public static final String BROKER_GENERAL_RECEIVED_QUEUE = "received";
    public static final String BROKER_GENERAL_ROUTING_KEY = "*.*.*.*.*.metadata";

    public static final String MESSAGE_ERROR_VALIDATION_FORMAT = " [ %s] ";
    public static final String MESSAGE_ERROR_FIELD_FORMAT = " [ %s] ";
    public static final String MESSAGE_ERROR_FIELD_NOT_ALLOW_LOG_FORMAT = "The field {} is no allow access in class {}";
    public static final String MESSAGE_ERROR_ALL_FIELDS_REQUIRED_IN_INSTANCE_LOG_FORMAT = "error the next fields {} are required in {}";

}

