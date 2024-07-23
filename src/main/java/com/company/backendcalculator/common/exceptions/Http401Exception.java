package com.company.backendcalculator.common.exceptions;

public class Http401Exception extends RuntimeException {
    public Http401Exception(){
        super("unauthorized");
    }
}
