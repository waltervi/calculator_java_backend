package com.company.backendcalculator.authorization.service;

import com.company.backendcalculator.authorization.entities.User;
import com.company.backendcalculator.common.dto.UserData;
import jakarta.transaction.Transactional;

public interface UserService {
    @Transactional
    void save(UserData userData, User user);

    @Transactional
    User findById(Long userId);
}
