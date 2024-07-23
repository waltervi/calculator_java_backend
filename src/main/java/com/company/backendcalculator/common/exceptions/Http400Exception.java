package com.company.backendcalculator.common.exceptions;

public class Http400Exception extends RuntimeException {
    public Http400Exception(String message){
        super(message);
    }
}
