package com.demo.blog.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BlogAlreadyExistsException.class)
    protected ResponseEntity<CustomErrorResponse> handleUserAlreadyExists(BlogAlreadyExistsException ex, WebRequest request) {
        String message = ex.getMessage();
        CustomErrorResponse response = CustomErrorResponse.builder().message(message).success(false)
                .status(HttpStatus.FORBIDDEN).build();
        return new ResponseEntity<CustomErrorResponse>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BlogNotFoundException.class)
    protected ResponseEntity<CustomErrorResponse> handleUserNotFound(BlogNotFoundException ex, WebRequest request) {
        String message = ex.getMessage();
        CustomErrorResponse response = CustomErrorResponse.builder().message(message).success(false)
                .status(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<CustomErrorResponse>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenException.class)
    protected ResponseEntity<CustomErrorResponse> handleInvalidToken(TokenException ex, WebRequest request) {
        String message = ex.getMessage();
        CustomErrorResponse response = CustomErrorResponse.builder().message(message).success(false)
                .status(HttpStatus.UNAUTHORIZED).build();
        return new ResponseEntity<CustomErrorResponse>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenModificationException.class)
    protected ResponseEntity<CustomErrorResponse> handleInvalidModification(ForbiddenModificationException ex, WebRequest request) {
        String message = ex.getMessage();
        CustomErrorResponse response = CustomErrorResponse.builder().message(message).success(false)
                .status(HttpStatus.FORBIDDEN).build();
        return new ResponseEntity<CustomErrorResponse>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CustomErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        String message = ex.getMessage();
        CustomErrorResponse response = CustomErrorResponse.builder().message(message).success(false)
                .status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        return new ResponseEntity<CustomErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
