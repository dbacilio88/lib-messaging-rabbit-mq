package com.bxcode.dto;

import com.bxcode.components.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Optional;

import static com.bxcode.constants.ProcessConstant.SIMPLE_DOT_FORMAT;
import static com.bxcode.constants.ProcessConstant.SIMPLE_EMPTY_FORMAT;

/**
 * RoutingKey
 * <p>
 * RoutingKey class.
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoutingKey implements Serializable {

    private static final long serialVersionUID = -599193854254492387L;

    private EventType eventType;
    private String origin;
    private String destiny;
    private String domain;
    private String command;
    private String additional;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(eventType.name());
        sb.append(SIMPLE_DOT_FORMAT);
        sb.append(origin);
        sb.append(SIMPLE_DOT_FORMAT);
        sb.append(destiny);
        sb.append(SIMPLE_DOT_FORMAT);
        sb.append(domain);
        sb.append(SIMPLE_DOT_FORMAT);
        sb.append(command);
        String add = Optional.ofNullable(additional).orElse(SIMPLE_EMPTY_FORMAT);
        if (!add.trim().isEmpty()) {
            sb.append(SIMPLE_DOT_FORMAT);
            sb.append(add);
        }
        return sb.toString();
    }
}