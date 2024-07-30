package com.company.backendcalculator.authorization_microservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "userName cannot be blank")
    @Size(max = 30, message = "Username too long")
    @Size(min = 3, message = "Username too short")
    private String username;

    @NotBlank(message = "password cannot be blank")
    @Size(max = 50, message = "password too long")
    @Size(min = 3, message = "password too short")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
