package com.company.backendcalculator.authorization.dto;

public class RegisterResponse {
    private String userName;
    private Double balance;

    public RegisterResponse() {}

    public RegisterResponse(String userName, Double balance) {
        this.userName = userName;
        this.balance = balance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
