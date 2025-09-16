package com.gendx.showcase.secure_medical_monolith.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Service responsible for all JWT-related operations: generation and validation.
*/

@Service
public class JwtService {
    //Important - In a real time app, this secret key would be loaded from a secure configuration file
    private final String SECRET_KEY = "secret-key-that-is-long-and-secure";
    private static final long EXPIRATION_TIME_MS = 900_000;

    /* The secret key injected from application.properties
    public JwtService(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }*/

    //This method builds and signs a new JWT. It uses a "builder pattern", chaining method calls together
    public String generateToken(String username, String role) {
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .sign(Algorithm.HMAC256(SECRET_KEY));

    }

    //This method inspects an incoming token to see if it's authentic and valid
    public DecodedJWT validateToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token);
    }

}
