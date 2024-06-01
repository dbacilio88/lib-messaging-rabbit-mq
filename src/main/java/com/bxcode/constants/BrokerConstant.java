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
public class BrokerConstant {
    public static final String BROKER_GENERAL_EXCHANGE_NAME = "Error in broker configuration the [ %s ] is null or empty";
    public static final String BROKER_GENERAL_EXCHANGE_TYPE = "TOPIC";
    public static final String BROKER_GENERAL_METADATA_QUEUE = "EVENTS_METADATA";
    public static final String BROKER_GENERAL_RECEIVED_QUEUE = "received";
    public static final String BROKER_GENERAL_ROUTING_KEY = "*.*.*.*.*.metadata";
}


