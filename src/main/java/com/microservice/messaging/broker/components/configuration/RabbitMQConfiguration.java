package com.microservice.messaging.broker.components.configuration;


import com.microservice.messaging.broker.components.base.MQBase;
import com.microservice.messaging.broker.components.enums.MQValidation;
import com.microservice.messaging.broker.components.events.IMQEventMessageService;
import com.microservice.messaging.broker.components.events.messages.MQMessagingConverter;
import com.microservice.messaging.broker.components.exceptions.MQBrokerException;
import com.microservice.messaging.broker.components.properties.RabbitMQProperties;
import com.microservice.messaging.broker.components.properties.validations.IRabbitMQPropertiesValidation;
import com.microservice.messaging.broker.components.properties.validations.RabbitMQPropertiesResult;
import com.microservice.messaging.broker.components.utils.MQUtility;
import com.microservice.messaging.broker.constants.RabbitMQConstant;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * RabbitMQConfiguration
 * <p>
 * RabbitMQConfiguration class.
 * <p>
 * This class specifies the requirements for the RabbitMQConfiguration component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 11/10/2024
 */
@Log4j2
@Component
public class RabbitMQConfiguration extends MQBase {

    private final RabbitMQProperties rabbitMQProperties;
    private final IMQEventMessageService eventMessageService;

    public RabbitMQConfiguration(RabbitMQProperties rabbitMQProperties, IMQEventMessageService eventMessageService) {
        super(RabbitMQConfiguration.class.getSimpleName());
        this.rabbitMQProperties = rabbitMQProperties;
        this.eventMessageService = eventMessageService;
    }

    @Bean("rabbitTemplate")
    public RabbitTemplate rabbitTemplate() {
        log.info("creating rabbit template");
        final ConnectionFactory connectionFactory = getConnectionFactory();
        final RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new MQMessagingConverter(eventMessageService));
        template.setDefaultReceiveQueue(RabbitMQConstant.PARAMETER_DEFAULT_RECEIVED_QUEUE);
        log.info("created rabbit template");
        return template;
    }

    public ConnectionFactory getConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        final RabbitMQPropertiesResult result = IRabbitMQPropertiesValidation.validationProperties().apply(rabbitMQProperties);

        if (MQValidation.PROCESS_VALIDATION_ERROR.equals(result.getValidation())) {
            throw new MQBrokerException(MQUtility.processValidationErrors(result.getErrors()));
        }
        connectionFactory.setHost(rabbitMQProperties.getHost());
        connectionFactory.setPort(rabbitMQProperties.getPort());
        connectionFactory.setUsername(rabbitMQProperties.getUsername());
        connectionFactory.setPassword(rabbitMQProperties.getPassword());
        connectionFactory.setVirtualHost(rabbitMQProperties.getVirtualHost());
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.valueOf(rabbitMQProperties.getPublisherConfirmType()));
        if (Boolean.TRUE.equals(rabbitMQProperties.isSslEnable())) {
            SSLContext context = null;
            try {
                context = SSLContext.getInstance("TSL");
                context.init(null, null, null);
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                throw new MQBrokerException(e);
            }
            connectionFactory.getRabbitConnectionFactory().useSslProtocol(context);
        }
        return connectionFactory;
    }
}
