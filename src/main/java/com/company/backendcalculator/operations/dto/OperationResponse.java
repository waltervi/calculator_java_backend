package com.company.backendcalculator.operations.dto;

public class OperationResponse {
    private String result;
    private Double balance;

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
}
