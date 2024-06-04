package com.bxcode.services.implementations;

import com.bxcode.components.annotations.ReturnCallBack;
import com.bxcode.components.exceptions.ParameterException;
import com.bxcode.dto.Event;
import com.bxcode.services.contracts.ICallBackBuilderService;
import com.bxcode.services.contracts.ICallBackRegisterService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static com.bxcode.constants.ProcessConstant.*;

/**
 * CallBackRegisterService
 * <p>
 * CallBackRegisterService class.
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
public class CallBackRegisterService implements ICallBackRegisterService {

    private final RabbitTemplate rabbitTemplate;
    private final ICallBackBuilderService callBackBuilderService;
    private RabbitTemplate.ReturnsCallback returnsCallback;

    public CallBackRegisterService(final RabbitTemplate rabbitTemplate,
                                   final ICallBackBuilderService callBackBuilderService) {
        this.rabbitTemplate = rabbitTemplate;
        this.callBackBuilderService = callBackBuilderService;
        log.debug("CallBackRegisterService loaded successfully");
    }

    @Override
    public void register(Object bean, String nameBean) {
        final Stream<Method> stream = Arrays.stream(bean.getClass().getDeclaredMethods());

        stream.parallel().forEach(method -> {
            final ReturnCallBack returnCallBack = method.getAnnotation(ReturnCallBack.class);
            if (returnCallBack != null && this.returnsCallback == null) {
                log.debug("validating return rollback service: {}", returnCallBack);
                log.debug("setting returnCallBack bean");
                final Class<?>[] classes = method.getParameterTypes();
                if (classes.length != PARAMETER_NUMBER_TYPES_REQUIRED) {
                    throw new ParameterException("Parameters of @ReturnCallBack should be (code:int, text:String, message:Event)");
                } else {
                    if (!int.class.equals(classes[PARAMETER_NUMBER_TYPES_REQUIRED_FIRST_INDEX])) {
                        throw new ParameterException("Parameters 1 should be (code:int)");
                    }

                    if (!String.class.equals(classes[PARAMETER_NUMBER_TYPES_REQUIRED_SECOND_INDEX])) {
                        throw new ParameterException("Parameters 2 should be (text:String)");
                    }
                    if (!Event.class.equals(classes[PARAMETER_NUMBER_TYPES_REQUIRED_THIRD_INDEX])) {
                        throw new ParameterException("Parameters 3 should be (message:Event)");
                    }
                }
                this.returnsCallback = callBackBuilderService.build(method, bean);
                this.rabbitTemplate.setMandatory(true);
                this.rabbitTemplate.setReturnsCallback(returnsCallback);
            }
        });
    }

    @Override
    public void executeReturn(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.debug("executing return call back");
        if (!Objects.isNull(returnsCallback)) {
            ReturnedMessage returnedMessage = new ReturnedMessage(message, replyCode, replyText, exchange, routingKey);
            returnsCallback.returnedMessage(returnedMessage);
        }
    }
}