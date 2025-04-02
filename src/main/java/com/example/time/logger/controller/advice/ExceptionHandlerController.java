package com.example.time.logger.controller.advice;

import com.example.time.logger.model.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto error() {
        return new ErrorDto("The DB is currently unavailable.", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}