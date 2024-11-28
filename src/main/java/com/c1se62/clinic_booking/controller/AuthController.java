package com.c1se62.clinic_booking.controller;

import com.c1se62.clinic_booking.dto.request.LoginRequest;
import com.c1se62.clinic_booking.dto.request.RegisterRequest;
import com.c1se62.clinic_booking.dto.response.AuthenticationResponse;
import com.c1se62.clinic_booking.dto.response.UserResponse;
import com.c1se62.clinic_booking.service.AuthenticationServices.AuthenticationServices;
import com.c1se62.clinic_booking.service.UserServices.UserServices;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserServices userServices;
    @Autowired
    private AuthenticationServices authenticationServices;

    @PostMapping("/register")
    public UserResponse registerUser(@RequestBody @Valid RegisterRequest request) {
            return userServices.register(request);
    }

    @PostMapping("/login")
    AuthenticationResponse authenticate(@RequestBody @Valid LoginRequest authenticationRequest) {
        AuthenticationResponse result = authenticationServices.authenticated(authenticationRequest);
        return result;
    }
}
