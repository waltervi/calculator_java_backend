package com.company.backendcalculator.authorization.controller;

import com.company.backendcalculator.authorization.dto.RegisterRequest;
import com.company.backendcalculator.authorization.service.RegistrationService;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping(value = "/v1/auth/register" , consumes = {"application/json"})
    public ResponseEntity register(@Valid @RequestBody RegisterRequest registerRequest){
        String token = registrationService.registerUser(registerRequest.getUserName(),registerRequest.getPassword());

        String tokenCookie = getTokenCookie(token);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, tokenCookie).build();
    }

    @PostMapping("/v1/auth/login")
    public ResponseEntity login(@Valid @RequestBody RegisterRequest registerRequest){
        String token = registrationService.login(registerRequest.getUserName(),registerRequest.getPassword());

        String tokenCookie = getTokenCookie(token);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, tokenCookie).build();
    }

    @PostMapping("/logout")
    public ResponseEntity logout(){
        Cookie jwtTokenCookie = new Cookie("token", "");
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtTokenCookie.toString()).build();
    }

    private String getTokenCookie(String token){
        Cookie jwtTokenCookie = new Cookie("token", token);
        jwtTokenCookie.setHttpOnly(true);
        /*
        NOTE:
        The remaining cookie data is meant to be configured later if it is needed.
        For the moment, and the sake of this demo, I will keep this data commented out.
        */
        //jwtTokenCookie.setMaxAge(86400);
        //jwtTokenCookie.setSecure(true);
        //jwtTokenCookie.setPath("/user/");
        //jwtTokenCookie.setDomain("example.com");


        return jwtTokenCookie.getName() + "=" + jwtTokenCookie.getValue();
    }

}
