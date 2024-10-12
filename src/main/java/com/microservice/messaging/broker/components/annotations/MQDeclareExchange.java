package com.microservice.messaging.broker.components.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MQDeclareExchange
 * <p>
 * MQDeclareExchange @interface.
 * <p>
 * This @interface specifies the requirements for the MQDeclareExchange component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 11/10/2024
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MQDeclareExchange {

    /**
     * Cada intercambiador tiene un nombre único dentro del contexto de RabbitMQ, que lo identifica al enviar mensajes.
     *
     * @return String
     */
    String type();

    /**
     * Define cómo se enrutan los mensajes. Los tipos más comunes son:<br>
     * - <b>direct</b>: Envía mensajes a colas específicas basándose en una clave de enrutamiento exacta.<br>
     * - <b>fanout</b>: Envía mensajes a todas las colas vinculadas sin considerar la clave de enrutamiento.<br>
     * - <b>topic</b>: Permite enrutamiento basado en patrones en la clave de enrutamiento, utilizando caracteres comodín como * y #.<br>
     * - <b>headers</b>: Enruta mensajes basándose en atributos en las cabeceras, en lugar de usar una clave de enrutamiento.<br>
     *
     * @return String[]
     */
    String[] exchanges();

    /**
     * Esta opción determina si el intercambiador sobrevivirá a reinicios del servidor RabbitMQ. Si es duradero, se mantendrá después de un reinicio.
     *
     * @return boolean
     */
    boolean durability() default true;

    /**
     * Indica que el intercambiador se eliminará automáticamente cuando no haya colas vinculadas a él.
     *
     * @return boolean
     */
    boolean autoDelete() default false;

    /**
     * En RabbitMQ, el atributo "internal" de un exchange se refiere a una propiedad que indica si el intercambiador es interno o no. Aquí te explico más sobre este concepto.<br>
     * <b>Intercambiador Interno</b>: Un intercambiador interno es aquel que no puede recibir mensajes directamente desde los productores. En cambio, solo puede recibir mensajes de otros intercambiadores. Esto lo hace útil para estructuras de enrutamiento más complejas.<br>
     * <b>Propósito</b>: Se utiliza principalmente para crear rutas intermedias en el enrutamiento de mensajes. Por ejemplo, puedes tener un intercambiador interno que distribuye mensajes a otros intercambiadores, que luego envían esos mensajes a las colas finales.<br>
     * <b>Configuración</b>:Cuando se crea un intercambiador, puedes especificar que sea interno estableciendo su atributo internal como verdadero. Esto es útil para mantener la separación entre las diferentes partes de tu sistema de mensajería
     * <b>Ventajas</b>: Mejora la organización del flujo de mensajes, al permitir que el enrutamiento se realice a través de intercambiadores intermedios sin exposición directa a los productores.
     * Facilita la implementación de patrones de diseño como el publish/subscribe o el router.<br>
     * <b>Ejemplo de Uso</b>: Si tienes un sistema en el que ciertos intercambiadores deben recibir y distribuir mensajes entre otros intercambiadores antes de llegar a las colas finales, puedes utilizar un intercambiador interno para controlar este proceso sin permitir que los productores envíen mensajes directamente a él.
     *
     * @return boolean
     */
    boolean internal() default false;
}
