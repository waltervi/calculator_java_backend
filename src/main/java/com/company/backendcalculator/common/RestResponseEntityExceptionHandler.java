package com.company.backendcalculator.common;

import com.company.backendcalculator.common.exceptions.Http400Exception;
import com.company.backendcalculator.common.exceptions.Http401Exception;
import com.company.backendcalculator.common.exceptions.Http500Exception;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@CrossOrigin
@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    public static class ErrorMessage {
        private String errorMessage;

        public ErrorMessage() {
        }

        public ErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    @ExceptionHandler({ Http400Exception.class, IllegalArgumentException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorMessage> handleHttp400Exception(Exception ex, WebRequest request) {



        return new ResponseEntity<ErrorMessage>(new ErrorMessage(ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({ Http401Exception.class })
    public ResponseEntity<ErrorMessage> handleHttp401Exception(Exception ex, WebRequest request) {
        return new ResponseEntity<ErrorMessage>(
                new ErrorMessage("unathorized"), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler({ Http500Exception.class, Exception.class })
    public ResponseEntity<ErrorMessage> handleHttp500Exception(Exception ex, WebRequest request) {
        return new ResponseEntity<ErrorMessage>(
                new ErrorMessage(ex.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
