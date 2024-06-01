package com.bxcode.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Queue;

/**
 * Metadata
 * <p>
 * Metadata class.
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
public class Metadata implements Serializable {

    private static final long serialVersionUID = 2519554863153450570L;

    private Queue<Step> steps;
}