package com.bxcode.components.helpers;

import lombok.experimental.UtilityClass;

/**
 * StringFormat
 * <p>
 * StringFormat class.
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
public class StringFormat {
    public static String formatMessage(String message, Object... variables) {
        return String.format(message, variables);
    }
}


