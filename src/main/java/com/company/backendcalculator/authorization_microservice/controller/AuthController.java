package com.company.backendcalculator.authorization_microservice.controller;

import com.company.backendcalculator.authorization_microservice.dto.RegisterRequest;
import com.company.backendcalculator.authorization_microservice.dto.RegisterResponse;
import com.company.backendcalculator.authorization_microservice.entities.User;
import com.company.backendcalculator.authorization_microservice.service.RegistrationService;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin( originPatterns = "*,", allowCredentials = "true")
@RestController
public class AuthController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping(value = "/v1/auth/register" , consumes = {"application/json"})
    public RegisterResponse register(@Valid @RequestBody RegisterRequest registerRequest){
        User user = registrationService.registerUser(registerRequest.getUsername(),registerRequest.getPassword());
        String token = registrationService.generateToken(user);

        RegisterResponse response = new RegisterResponse(user.getUserName(),user.getCurrentBalance(),token);

        return response;
    }

    @PostMapping("/v1/auth/login")
    public RegisterResponse login(@Valid @RequestBody RegisterRequest registerRequest){
        User user = registrationService.login(registerRequest.getUsername(),registerRequest.getPassword());

        String token = registrationService.generateToken(user);
        //String tokenCookie = getTokenCookie(token);

        RegisterResponse response = new RegisterResponse(user.getUserName(),user.getCurrentBalance(),token);

        return response;
    }

    @PostMapping("/logout")
    public ResponseEntity logout(){
        Cookie jwtTokenCookie = new Cookie("token", "");
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtTokenCookie.toString()).build();
    }

//    private String getTokenCookie(String token){
//        ResponseCookie resCookie = ResponseCookie.from("token", token)
//                .httpOnly(true)
//                .sameSite("none")
//                //.secure(true)
//                .path("/")
//                .maxAge(Duration.ofDays(30))
//                .build();
//
//        /*
//        NOTE:
//        The remaining cookie data is meant to be configured later if it is needed.
//        For the moment, and the sake of this demo, I will keep this data commented out.
//        */
//        //jwtTokenCookie.setMaxAge(86400);
//        //jwtTokenCookie.setSecure(true);
//        //jwtTokenCookie.setPath("/user/");
//        //jwtTokenCookie.setDomain("example.com");
//
//        String s = resCookie.toString();
//        System.out.println(s);
//        return s;
//    }

}
