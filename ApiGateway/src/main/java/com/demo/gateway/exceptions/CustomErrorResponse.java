package com.demo.gateway.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomErrorResponse {
    private String message;
    private boolean success;
    private HttpStatus status;

}