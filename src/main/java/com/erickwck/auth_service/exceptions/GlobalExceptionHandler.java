package com.erickwck.auth_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AirlineCompanyNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String airlineCompanyNotFound(AirlineCompanyNotFound ex) {
        return ex.getMessage();
    }

}
