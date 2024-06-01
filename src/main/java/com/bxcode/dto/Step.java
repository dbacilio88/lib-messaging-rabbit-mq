package com.bxcode.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

/**
 * Step
 * <p>
 * Step class.
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
public class Step implements Serializable {

    private static final long serialVersionUID = 5764955199526609511L;
    private RoutingKey routingKey;
    private Map<String, String> mapper;
}