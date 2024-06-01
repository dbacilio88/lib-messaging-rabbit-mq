package com.bxcode.services.contracts;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * IConsumerBuilderService
 * <p>
 * IConsumerBuilderService interface.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 1/06/2024
 */
public interface IConsumerBuilderService {

    String PARAM_MESSAGE_NULL_EXCEPTION = "parameter %s is required, please set value to this field";

    Consumer build(Channel channel, Method method, Object bean, Type type, String queue, boolean automaticAck, IEventMapperService mapper, IConsumerDispatchService consumer);

    Channel validateChannel(Channel channel);

    Object validateBean(Object bean);

    String validateQueue(Object queue);

    Type validateType(Type type);

    Method validateMethod(Method method);
}
