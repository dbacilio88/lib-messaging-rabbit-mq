package com.bacsystem.utils.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * MQTrailer
 * <p>
 * MQTrailer class.
 * <p>
 * This class specifies the requirements for the MQTrailer component,
 * developed in accordance with the development standards established by bxcode.
 * Collaboration is encouraged for the enhancement and expansion of the lib-messaging-rabbit-mq.
 *
 * @author bxcode
 * @author dbacilio88@outllok.es
 * @since 12/10/2024
 */

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MQTrailer implements Serializable {
    @Serial
    private static final long serialVersionUID = 7992626894944270651L;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime sent;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime received;
    private String lastAgentId;
    private String initialAgentId;
    private int size;
}
