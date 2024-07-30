package com.company.backendcalculator.operations_microservice.dto;

public class OperationResponse {
    private String result;
    private Double balance;
    private String errorMessage;//only used in case of errors. Don't remove

    public OperationResponse() {}

    public OperationResponse(String result, Double balance) {
        this.result = result;
        this.balance = balance;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
