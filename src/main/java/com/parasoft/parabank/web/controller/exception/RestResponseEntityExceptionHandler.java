package com.parasoft.parabank.web.controller.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationError(AuthenticationException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        // Prevent IE from incorrectly caching AJAX/JSON results
        // http://www.dashbay.com/2011/05/internet-explorer-caches-ajax/
        headers.add("Expires", "-1");
        return handleExceptionInternal(ex, ex.getMessageBody(), headers, HttpStatus.UNAUTHORIZED, request);
    }
}
