package com.microservice.messaging.broker.components.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MQDeclareQueue
 * <p>
 * MQDeclareQueue @interface.
 * <p>
 * This @interface specifies the requirements for the MQDeclareQueue component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 11/10/2024
 */


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MQDeclareQueue {


    /**
     * Cada cola tiene un nombre único que la identifica dentro del sistema. Este nombre es utilizado por los productores y consumidores para enviar y recibir mensajes
     *
     * @return String[]
     */
    String[] queues();


    /**
     * Esta propiedad determina si la cola debe sobrevivir a reinicios de RabbitMQ. Una cola duradera garantiza que sus mensajes no se pierdan si el servidor se apaga.
     *
     * @return boolean
     */
    boolean durability() default true;


    /**
     * Indica que la cola se eliminará automáticamente cuando ya no haya consumidores conectados a ella. Esto es útil para colas temporales que solo son necesarias durante la vida de una sesión.
     *
     * @return boolean
     */
    boolean autoDelete() default false;


    /**
     * Una cola exclusiva es aquella que solo puede ser utilizada por la conexión que la creó. Se eliminará automáticamente cuando esa conexión se cierre
     *
     * @return boolean
     */
    boolean exclusive() default false;


    /**
     * autoAck en RabbitMQ permite que los mensajes se consideren automáticamente confirmados al ser entregados a un consumidor, eliminando la necesidad de enviar un ACK manualmente.
     *
     * @return boolean
     */
    boolean autoAck() default true;

    QueueProperties[] properties() default {};

    @interface QueueProperties {
        String name();

        String value();
    }
}
