package com.microservice.messaging.broker.services.builder;


import com.microservice.messaging.broker.components.base.MQBase;
import com.microservice.messaging.broker.components.events.IMQEventMessageService;
import com.microservice.messaging.broker.components.events.messages.MQMessagingConsumer;
import com.microservice.messaging.broker.components.exceptions.MQBrokerException;
import com.microservice.messaging.broker.components.properties.RabbitMQProperties;
import com.microservice.messaging.broker.components.utils.MQUtility;
import com.microservice.messaging.broker.services.dispatch.IMQConsumerDispatch;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;

import static com.microservice.messaging.broker.constants.RabbitMQConstant.*;

/**
 * MQConsumerBuilder
 * <p>
 * MQConsumerBuilder class.
 * <p>
 * This class specifies the requirements for the MQConsumerBuilder component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */


@Service
public class MQConsumerBuilder extends MQBase implements IMQConsumerBuilder {

    private final RabbitMQProperties rabbitMQProperties;

    public MQConsumerBuilder(final RabbitMQProperties rabbitMQProperties) {
        super(MQConsumerBuilder.class.getSimpleName());
        this.rabbitMQProperties = rabbitMQProperties;
    }

    @Override
    public Type validateType(Type type) {
        return Optional.ofNullable(type).orElse(Object.class);
    }

    @Override
    public Method validateMethod(Method method) {
        return Optional.ofNullable(method).orElseThrow(() -> new MQBrokerException(MQUtility.formatMessage(PARAM_NULL_MSG_EXCEPTION, PARAMETER_METHOD)));

    }

    @Override
    public String validateQueueName(String queueName) {
        return Optional.ofNullable(queueName).orElseThrow(() -> new MQBrokerException(MQUtility.formatMessage(PARAM_NULL_MSG_EXCEPTION, PARAMETER_QUEUE_NAME)));

    }

    @Override
    public Object validateBean(Object bean) {
        return Optional.ofNullable(bean).orElseThrow(() -> new MQBrokerException(MQUtility.formatMessage(PARAM_NULL_MSG_EXCEPTION, PARAMETER_BEAN)));

    }

    @Override
    public Channel validateChannel(Channel channel) {
        return Optional.ofNullable(channel).orElseThrow(() -> new MQBrokerException(MQUtility.formatMessage(PARAM_NULL_MSG_EXCEPTION, PARAMETER_CHANNEL)));

    }

    @Override
    public Consumer build(Channel channel, Method method, Object bean, Type type, String queueName, boolean automaticAck, IMQEventMessageService eventMessageService, IMQConsumerDispatch consumerDispatch) {
        validateBean(bean);
        validateMethod(method);
        validateQueueName(queueName);
        validateChannel(channel);
        validateType(type);
        return new MQMessagingConsumer(
                channel, method, bean, type, queueName, automaticAck, rabbitMQProperties, eventMessageService, consumerDispatch
        );
    }
}
