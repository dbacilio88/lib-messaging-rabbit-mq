package com.bxcode.components.helpers;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Serializer
 * <p>
 * Serializer class.
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
public class Serializer {
    public static byte[] objectToArrayBytes(Object object) {
        byte[] bytes = new byte[0];
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            ObjectOutputStream oos = new ObjectOutputStream(stream);
            oos.writeObject(object);
            oos.flush();
            bytes = stream.toByteArray();
            return bytes;
        } catch (IOException e) {
            log.error("error in process serializer object to byte[]: {}", e.getMessage());
        }
        return bytes;
    }
}


