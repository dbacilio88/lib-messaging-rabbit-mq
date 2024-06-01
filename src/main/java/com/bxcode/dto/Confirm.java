package com.bxcode.dto;

import lombok.*;

import java.io.Serializable;

/**
 * Confirm
 * <p>
 * Confirm class.
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
public class Confirm implements Serializable {


    private static final long serialVersionUID = 2973594615315367637L;
    private String id;
    private boolean confirmed;
    private String reason;
}