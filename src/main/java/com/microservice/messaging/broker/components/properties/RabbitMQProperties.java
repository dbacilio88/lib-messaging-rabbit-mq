package com.microservice.messaging.broker.components.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * RabbitMQProperties
 * <p>
 * RabbitMQProperties class.
 * <p>
 * This class specifies the requirements for the RabbitMQProperties component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 11/10/2024
 */
@Data
@EnableConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "rabbit")
public class RabbitMQProperties {
    private String host;
    private String username;
    private String password;
    private int port;
    private String virtualHost;
    private boolean publisherReturn;
    private long reconnectConsumerTime;
    private String publisherConfirmType;
    private int reconnectConsumerTries;
    private Map<String, Object> queueProperties;
    private boolean sslEnable;
}
