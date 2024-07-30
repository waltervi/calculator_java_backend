package com.company.backendcalculator.authorization_microservice.service;

import com.company.backendcalculator.authorization_microservice.entities.User;

public interface RegistrationService {
    User registerUser(String userName, String password);

    User login(String userName, String password);

    String generateToken(User user);
}
