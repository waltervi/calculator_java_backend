package com.company.backendcalculator.authorization_microservice.service;

import com.company.backendcalculator.authorization_microservice.entities.User;
import com.company.backendcalculator.common.dto.UserData;
import jakarta.transaction.Transactional;

/*
* We name it ..MicroService simulating a class that encapsulates a microservice call.
* */
public interface UserMicroservice {
    @Transactional
    void save(UserData userData, User user);

    void rollbackSave(UserData userData, User user);

    @Transactional
    User findById(Long userId);
}
