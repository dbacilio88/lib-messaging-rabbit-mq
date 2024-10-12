package com.microservice.messaging.broker.components.environments;


import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * MQEnvironment
 * <p>
 * MQEnvironment class.
 * <p>
 * This class specifies the requirements for the MQEnvironment component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 11/10/2024
 */

@Component
public class MQEnvironment {

    private final Environment environment;

    public MQEnvironment(final Environment environment) {
        this.environment = environment;
    }

    public String get(String key) {
        if (key.startsWith("${") && key.endsWith("}")) {

            return environment.resolvePlaceholders(key);
        }
        return key;
    }
}
