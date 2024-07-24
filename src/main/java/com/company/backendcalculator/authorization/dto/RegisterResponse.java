package com.company.backendcalculator.authorization.dto;

public class RegisterResponse {
    private String userName;
    private Double balance;
    private String token;

    public RegisterResponse() {}

    public RegisterResponse(String userName, Double balance,String token) {
        this.userName = userName;
        this.balance = balance;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
