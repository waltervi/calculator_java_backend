package com.company.backendcalculator.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "userName cannot be blank")
    @Size(max = 30, message = "Username too long")
    @Size(min = 3, message = "Username too short")
    private String userName;

    @NotBlank(message = "password cannot be blank")
    @Size(max = 50, message = "password too long")
    @Size(min = 3, message = "password too short")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
