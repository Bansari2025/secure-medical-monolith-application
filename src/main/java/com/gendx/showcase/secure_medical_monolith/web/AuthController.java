package com.gendx.showcase.secure_medical_monolith.web;

import com.gendx.showcase.secure_medical_monolith.model.LoginRequest;
import com.gendx.showcase.secure_medical_monolith.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/auth/login")
    public Map<String, String> login(@RequestBody LoginRequest loginRequest){

        //This is a mock authentication. In a real app, we'd check a database.
        if ("doctor".equals(loginRequest.username()) &&
        "password123".equals(loginRequest.password())) {
            String token = jwtService.generateToken("doctor_user", "ROLE_DOCTOR");
            return Map.of("token", token);
        }
        if ("researcher".equals(loginRequest.username()) &&
        "password123".equals(loginRequest.password())) {
            String token = jwtService.generateToken("researcher_user", "ROLE_RESEARCHER");
            return Map.of("token", token);
        }
        // A better way would be to throw a proper AuthenticationException
        throw new RuntimeException("Invalid credentials");
    }
}
