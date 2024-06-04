package com.bxcode.services.implementations;

import com.bxcode.components.configurations.MessageConfiguration;
import com.bxcode.components.exceptions.ParameterException;
import com.bxcode.components.helpers.StringFormat;
import com.bxcode.models.implementations.RabbitConsumer;
import com.bxcode.services.contracts.IConsumerBuilderService;
import com.bxcode.services.contracts.IConsumerDispatchService;
import com.bxcode.services.contracts.IEventMapperService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;

import static com.bxcode.constants.ProcessConstant.*;

/**
 * ConsumerBuilderService
 * <p>
 * ConsumerBuilderService class.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 4/06/2024
 */
@Log4j2
@Service
public class ConsumerBuilderService implements IConsumerBuilderService {

    private final MessageConfiguration messageConfiguration;

    public ConsumerBuilderService(final MessageConfiguration messageConfiguration) {
        this.messageConfiguration = messageConfiguration;
        log.debug("ConsumerBuilderService loaded successfully");
    }

    @Override
    public Consumer build(Channel channel, Method method, Object bean, Type type, String queue, boolean automaticAck, IEventMapperService mapper, IConsumerDispatchService consumer) {
        validateChannel(channel);
        validateMethod(method);
        validateBean(bean);
        validateType(type);
        validateQueue(queue);

        return new RabbitConsumer(channel, method, bean, type, queue, automaticAck, mapper, consumer, this.messageConfiguration);
    }

    @Override
    public Channel validateChannel(Channel channel) {
        return Optional.ofNullable(channel)
                .orElseThrow(() -> new ParameterException(StringFormat.formatMessage(PARAM_MESSAGE_NULL_EXCEPTION, PARAMETER_CHANNEL)));
    }

    @Override
    public Object validateBean(Object bean) {
        return Optional.ofNullable(bean)
                .orElseThrow(() -> new ParameterException(StringFormat.formatMessage(PARAM_MESSAGE_NULL_EXCEPTION, PARAMETER_BEAN)));
    }

    @Override
    public String validateQueue(String queue) {
        return Optional.ofNullable(queue)
                .orElseThrow(() -> new ParameterException(StringFormat.formatMessage(PARAM_MESSAGE_NULL_EXCEPTION, PARAMETER_QUEUE_NAME)));
    }

    @Override
    public Type validateType(Type type) {
        return Optional.ofNullable(type)
                .orElse(Object.class);
    }

    @Override
    public Method validateMethod(Method method) {
        return Optional.ofNullable(method)
                .orElseThrow(() -> new ParameterException(StringFormat.formatMessage(PARAM_MESSAGE_NULL_EXCEPTION, PARAMETER_METHOD)));
    }
}