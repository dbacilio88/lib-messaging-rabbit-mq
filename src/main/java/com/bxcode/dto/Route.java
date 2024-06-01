package com.bxcode.dto;

import lombok.*;

import java.io.Serializable;

/**
 * Route
 * <p>
 * Route class.
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
public class Route implements Serializable {
    private static final long serialVersionUID = 1890225113473237933L;
    private String step;
    private String mapping;
}