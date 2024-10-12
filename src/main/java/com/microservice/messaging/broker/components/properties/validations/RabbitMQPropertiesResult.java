package com.microservice.messaging.broker.components.properties.validations;


import com.microservice.messaging.broker.components.enums.MQValidation;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * RabbitMQPropertiesResult
 * <p>
 * RabbitMQPropertiesResult class.
 * <p>
 * This class specifies the requirements for the RabbitMQPropertiesResult component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RabbitMQPropertiesResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -6221463236377451558L;
    private MQValidation validation;
    private String message;
    private List<String> errors;
}
