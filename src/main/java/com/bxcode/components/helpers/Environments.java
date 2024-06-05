package com.bxcode.components.helpers;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Environments
 * <p>
 * Environments class.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 31/05/2024
 */
@Component
public class Environments {
    private final Environment environment;

    public Environments(final Environment environment) {
        this.environment = environment;
    }

    public String get(String value) {
        if (value.startsWith("${") && value.endsWith("}")) {
            return environment.resolvePlaceholders(value);
        }
        return value;
    }
}


