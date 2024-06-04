package com.bxcode.components.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * MessageConfiguration
 * <p>
 * MessageConfiguration class.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 1/06/2024
 */

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "rabbit")
@ComponentScan("com.bxcode")
@ComponentScan("com.bxcode.services")
@ComponentScan("com.bxcode.services.implementations")
@ComponentScan("com.bxcode.components")
@ComponentScan("com.bxcode.components.annotations")
public class MessageConfiguration {
    private String host;
    private String username;
    private String password;
    private int port;
    private String virtualHost;
    private boolean publisherReturn;
    private long reconnectConsumerTime;
    private String reconnectConsumerType;
    private int reconnectConsumerTries;
    private Map<String, Object> queueProperties;
    private boolean sslEnable;
}