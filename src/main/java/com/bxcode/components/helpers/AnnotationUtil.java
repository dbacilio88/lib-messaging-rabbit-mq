package com.bxcode.components.helpers;

import lombok.experimental.UtilityClass;

/**
 * AnnotationUtil
 * <p>
 * AnnotationUtil class.
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
public class AnnotationUtil {

    public static <T> T findAnnotationBean(Object bean, T annotation) {
        return annotation;
    }
}


