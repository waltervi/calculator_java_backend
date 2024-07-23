package com.company.backendcalculator.operations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class OperationRequest {

    @NotBlank(message = "operation cannot be blank")
    @Size(max = 15, message = "invalid operation value")
    private String operation;

    @Size(max = 20, message = "invalid operandOne value")
    private String operandOne;

    @Size(max = 20, message = "invalid operandTwo value")
    private String operandTwo;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOperandOne() {
        return operandOne;
    }

    public void setOperandOne(String operandOne) {
        this.operandOne = operandOne;
    }

    public String getOperandTwo() {
        return operandTwo;
    }

    public void setOperandTwo(String operandTwo) {
        this.operandTwo = operandTwo;
    }
}
