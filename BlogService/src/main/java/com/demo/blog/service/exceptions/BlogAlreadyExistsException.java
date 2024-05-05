package com.demo.blog.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BlogAlreadyExistsException extends RuntimeException {
    public BlogAlreadyExistsException(String message) {
        super(message);
    }
}
