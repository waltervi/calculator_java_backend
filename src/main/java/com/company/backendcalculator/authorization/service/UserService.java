package com.company.backendcalculator.authorization.service;

import com.company.backendcalculator.authorization.entities.User;
import com.company.backendcalculator.authorization.repository.UserRepository;
import com.company.backendcalculator.common.dto.UserData;
import com.company.backendcalculator.common.exceptions.Http400Exception;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Transactional
    public void save(UserData userData, User user){
        userRepository.save(user);
    }

    @Transactional
    public User findById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new Http400Exception("User not found") );
    }

}
