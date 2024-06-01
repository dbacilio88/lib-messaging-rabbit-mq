package com.bxcode.dto;

import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * BrokerMessage
 * <p>
 * BrokerMessage class.
 * <p>
 * THIS COMPONENT WAS BUILT ACCORDING TO THE DEVELOPMENT STANDARDS
 * AND THE BXCODE APPLICATION DEVELOPMENT PROCEDURE AND IS PROTECTED
 * BY THE LAWS OF INTELLECTUAL PROPERTY AND COPYRIGHT...
 *
 * @author Bxcode
 * @author dbacilio88@outlook.es
 * @since 31/05/2024
 */

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrokerMessage<T> implements Serializable {


    private static final long serialVersionUID = -3971080869869955582L;
    private Map<String, String> eventSequences;
    private Map<String, String> eventLog;
    private transient HashMap<String, Object> headers;
    private transient HashMap<String, Object> metadata;
    private transient T data;
    private Trailer trailer;
}