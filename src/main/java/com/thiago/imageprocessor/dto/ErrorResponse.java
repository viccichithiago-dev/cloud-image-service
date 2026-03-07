package com.thiago.imageprocessor.dto;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ErrorResponse {
    private String mensaje;
    private int statusCode;
    private String error;
    private LocalDateTime Timestamp;
}
