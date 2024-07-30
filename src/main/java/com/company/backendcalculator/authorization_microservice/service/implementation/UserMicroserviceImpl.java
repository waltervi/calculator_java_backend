package com.company.backendcalculator.authorization_microservice.service.implementation;

import com.company.backendcalculator.authorization_microservice.entities.User;
import com.company.backendcalculator.authorization_microservice.repository.UserRepository;
import com.company.backendcalculator.common.service.TokenService;
import com.company.backendcalculator.authorization_microservice.service.UserMicroservice;
import com.company.backendcalculator.common.dto.UserData;
import com.company.backendcalculator.common.exceptions.Http400Exception;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * We name it ..MicroService simulating a class that encapsulates a microservice call.
 * */
@Service
public class UserMicroserviceImpl implements UserMicroservice {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    @Transactional
    public void save(UserData userData, User user){
        userRepository.save(user);
    }

    @Override
    public void rollbackSave(UserData userData, User user) {
        //code that would simulate a transaction rollback called by a client
        // ( in this case Operations Microservice )
    }

    @Override
    @Transactional
    public User findById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new Http400Exception("User not found") );
    }

}
