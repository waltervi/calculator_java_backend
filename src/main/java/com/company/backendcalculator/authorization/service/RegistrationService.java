package com.company.backendcalculator.authorization.service;

import com.company.backendcalculator.authorization.entities.User;

public interface RegistrationService {
    User registerUser(String userName, String password);

    User login(String userName, String password);

    String generateToken(User user);
}
