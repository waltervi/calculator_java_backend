package com.company.backendcalculator.authorization.service;

import com.company.backendcalculator.authorization.entities.User;
import com.company.backendcalculator.authorization.repository.UserRepository;
import com.company.backendcalculator.common.dto.UserData;
import com.company.backendcalculator.common.exceptions.Http400Exception;
import com.company.backendcalculator.common.exceptions.Http401Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class RegistrationService {
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

    public String registerUser(String userName, String password){
        User user = new User();
        user.setUserName(userName);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setCurrentBalance(defaultBalance);
        user.setStatus(1);

        userRepository.save(user);

        String token = generateToken(user);
        return token;
    }

    public String login(String userName, String password){
        User user = userRepository.findByUserName(userName);
        if ( user == null){
            throw new Http401Exception();
        }

        boolean passwordMatches = bCryptPasswordEncoder.matches(password,user.getPassword());
        if(!passwordMatches){
            throw new Http401Exception();
        }

        String token = generateToken(user);
        return token;
    }

    private String generateToken(User user ){
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(defaultDurationInDays);
        long millis = localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();

        UserData userData = new UserData(user.getId(), millis,tokenKey);

        String token = tokenService.encryptToken(userData);
        return token;
    }
}
