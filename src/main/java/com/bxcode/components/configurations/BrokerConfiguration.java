package com.bxcode.components.configurations;

import com.bxcode.components.exceptions.BrokerConfigurationException;
import com.bxcode.constants.ProcessConstant;
import com.bxcode.models.implementations.RabbitConverter;
import com.bxcode.services.contracts.IEventMapperService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;


/**
 * BrokerConfiguration
 * <p>
 * BrokerConfiguration class.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 1/06/2024
 */

@Log4j2
@Component
public class BrokerConfiguration {
    private final IEventMapperService mapperService;
    private final MessageConfiguration messageConfiguration;

    public BrokerConfiguration(final IEventMapperService mapperService,
                               final MessageConfiguration messageConfiguration) {
        this.mapperService = mapperService;
        this.messageConfiguration = messageConfiguration;
    }

    public ConnectionFactory connectionFactory() {
        try {

            final CachingConnectionFactory factory = new CachingConnectionFactory();
            factory.setHost(messageConfiguration.getHost());
            factory.setPort(messageConfiguration.getPort());
            factory.setUsername(messageConfiguration.getUsername());
            factory.setPassword(messageConfiguration.getPassword());
            factory.setVirtualHost(messageConfiguration.getVirtualHost());
            factory.setPublisherReturns(messageConfiguration.isPublisherReturn());
            factory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.valueOf(messageConfiguration.getReconnectConsumerType()));

            if (isSslConfigurationEnable()) {
                final SSLContext context = SSLContext.getInstance("TSL");
                context.init(null, null, null);
                factory.getRabbitConnectionFactory().useSslProtocol(context);
            }
            return factory;

        } catch (Exception e) {
            log.error("error in process connection, the SSL context configuration failed, error: {}", e.getMessage());
            throw new BrokerConfigurationException(e.getMessage());
        }
    }

    private boolean isSslConfigurationEnable() {
        return Boolean.TRUE.equals(messageConfiguration.isSslEnable());
    }

    @Bean(name = "rabbitTemplate")
    public RabbitTemplate rabbitTemplate() {
        log.debug("creating rabbit template");
        final ConnectionFactory factory = connectionFactory();
        final RabbitTemplate template = new RabbitTemplate(factory);
        final MessageConverter converter = new RabbitConverter(mapperService);
        template.setMessageConverter(converter);
        template.setDefaultReceiveQueue(ProcessConstant.PARAMETER_DEFAULT_RECEIVED_QUEUE);
        return template;
    }
}


