package com.bacsystem.utils.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Queue;

/**
 * MQMetadata
 * <p>
 * MQMetadata class.
 * <p>
 * This class specifies the requirements for the MQMetadata component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MQMetadata {
    private Queue<MQStep> steps;
}