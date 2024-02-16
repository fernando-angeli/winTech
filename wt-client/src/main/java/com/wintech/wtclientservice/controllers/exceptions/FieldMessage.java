package com.wintech.wtclientservice.controllers.exceptions;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldMessage {

    private String fieldName;
    private String message;
}
