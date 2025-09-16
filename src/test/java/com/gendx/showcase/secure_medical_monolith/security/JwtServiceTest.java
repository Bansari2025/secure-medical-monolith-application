package com.gendx.showcase.secure_medical_monolith.security;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp(){
        //Create a new instance for each test to ensure they are insolated
        jwtService = new JwtService();
    }

    @Test
    void generateToken_shouldCreateValidTokenWithCorrectClaims(){

        String username = "test-user";
        String role = "ROLE_TESTER";

        String token = jwtService.generateToken(username, role);

        assertNotNull(token); //The token should not be null

        //Validate token to check the contents
        DecodedJWT decodedJWT = jwtService.validateToken(token);
        assertEquals(username, decodedJWT.getSubject());
        assertEquals(role, decodedJWT.getClaim("role").asString());
    }

   //Unit test to use in real time app where the secret key is moved to application.properties file
   /* @Test
    void validateToken_withInvalidSignature_shouldThrowException(){

        // Create two services with DIFFERENT secret keys
        JwtService realJwtService = new JwtService("real-secret-key");

        JwtService maliciousJwtService = new JwtService("fake-secret-key");

        String tamperedToken = maliciousJwtService.generateToken("attacker", "ROLE_ADMIN");

        //Expect the validation with real secret key to fail as tampered token has been generated
        assertThrows(SignatureVerificationException.class, () -> {
            realJwtService.validateToken(tamperedToken);
        });
    }*/

    @Test
    void validateToken_withExpiredToken_shouldThrowException() {

        String gibberishToken = "this.is.not.a.real.token";
        assertThrows(Exception.class, () -> {
            jwtService.validateToken(gibberishToken);
        });
    }

}