package com.company.backendcalculator.operations_microservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class OperationRequest {

    @NotBlank(message = "operation cannot be blank")
    @Size(max = 15, message = "invalid operation value")
    private String operation;

    @Size(max = 20, message = "invalid operandOne value")
    private String operand1;

    @Size(max = 20, message = "invalid operandTwo value")
    private String operand2;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOperand1() {
        return operand1;
    }

    public void setOperand1(String operand1) {
        this.operand1 = operand1;
    }

    public String getOperand2() {
        return operand2;
    }

    public void setOperand2(String operand2) {
        this.operand2 = operand2;
    }
}
