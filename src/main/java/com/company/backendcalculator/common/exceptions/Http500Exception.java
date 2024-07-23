package com.company.backendcalculator.common.exceptions;

public class Http500Exception extends RuntimeException {
    public Http500Exception(String message){
        super(message);
    }
}
