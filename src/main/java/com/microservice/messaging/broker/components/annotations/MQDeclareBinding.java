package com.microservice.messaging.broker.components.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MQDeclareBinding
 * <p>
 * MQDeclareBinding @interface.
 * <p>
 * This @interface specifies the requirements for the MQDeclareBinding component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 11/10/2024
 */


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MQDeclareBinding {

    /**
     * Especifica el intercambiador al que se vincula la cola. Puede ser de tipo direct, topic, fanout, o headers.
     *
     * @return String
     */
    String exchange();

    /**
     * Define la cola que se está vinculando al intercambiador
     *
     * @return String
     */
    String queue();

    /**
     * Es un valor que se utiliza para dirigir el mensaje desde el intercambiador a la cola. La clave de enrutamiento se utiliza de manera diferente dependiendo del tipo de intercambiador.<br>
     * - <b>Direct</b>: La clave de enrutamiento debe coincidir exactamente con la clave utilizada por el productor.<br>
     * - <b>Topic</b>: La clave puede contener patrones que permiten hacer coincidencias más complejas (por ejemplo, usando * para una palabra y # para cero o más palabras).<br>
     * - <b>Fanout</b>: No se utiliza clave de enrutamiento, ya que los mensajes se envían a todas las colas vinculadas.<br>
     * - <b>Headers</b>: Utiliza atributos de cabecera en lugar de una clave de enrutamiento.<br>
     *
     * @return String
     */
    String routingKey();
}
