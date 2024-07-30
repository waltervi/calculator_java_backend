package com.company.backendcalculator.authorization_microservice.service.implementation;

import com.company.backendcalculator.authorization_microservice.entities.User;
import com.company.backendcalculator.authorization_microservice.repository.UserRepository;
import com.company.backendcalculator.authorization_microservice.service.RegistrationService;
import com.company.backendcalculator.common.service.TokenService;
import com.company.backendcalculator.common.dto.UserData;
import com.company.backendcalculator.common.exceptions.Http401Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    @Value("${balance.defaultValue}")
    private Double defaultBalance;

    @Value("${token.key}")
    private String tokenKey;

    @Value("${token.defaultDurationInDays}")
    private Long defaultDurationInDays;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public User registerUser(String userName, String password){
        User user = new User();
        user.setUserName(userName);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setCurrentBalance(defaultBalance);
        user.setStatus(1);

        userRepository.save(user);

        return user;
    }

    @Override
    public User login(String userName, String password){
        User user = userRepository.findByUserName(userName);
        if ( user == null){
            throw new Http401Exception();
        }

        String justEncoded= bCryptPasswordEncoder.encode(password);
        String fromDb = user.getPassword();

        System.out.println("justEncoded.equals(fromDb): " + justEncoded.equals(fromDb));
//$2a$10$8veebQJofxK2DDC94R9VbuGqO9YB/tBa3bYGrM0AYayhg6g1Hnoni
//$2a$10$9R7Y5mSLsKKr9zqtv6DKU.mIHUEPoTee4Vr5xOHwGZxkoIWZqVgHe
        boolean passwordMatches = bCryptPasswordEncoder.matches(password,user.getPassword());
        if(!passwordMatches){
            throw new Http401Exception();
        }

        return user;
    }

    @Override
    public String generateToken(User user){
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(defaultDurationInDays);
        long millis = localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();

        UserData userData = new UserData(user.getId(), millis,tokenKey);

        String token = tokenService.encryptToken(userData);
        return token;
    }
}
