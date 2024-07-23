package com.company.backendcalculator.common;

import com.company.backendcalculator.common.exceptions.Http400Exception;
import com.company.backendcalculator.common.exceptions.Http401Exception;
import com.company.backendcalculator.common.exceptions.Http500Exception;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler({ Http400Exception.class, IllegalArgumentException.class })
    public ResponseEntity<Object> handleHttp400Exception(Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(
                "bad request", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({ Http401Exception.class })
    public ResponseEntity<Object> handleHttp401Exception(Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(
                "unathorized", new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler({ Http500Exception.class, Exception.class })
    public ResponseEntity<Object> handleHttp500Exception(Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(
                "internal server error", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
