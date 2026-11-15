package com.hbpnrl.apexion.security;

import com.hbpnrl.apexion.entity.User;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private JwtService jwtService;
    private User user;

    @BeforeEach
    void setUp() {
        JwtConfig config = new JwtConfig();
        String secret = Base64.getEncoder().encodeToString(
                "apexion-test-secret-key-with-at-least-32-bytes".getBytes()
        );
        ReflectionTestUtils.setField(config, "secretKey", secret);
        jwtService = new JwtService(config);

        user = new User();
        user.setEmail("john@example.com");
        user.setPasswordHash("encoded-password");
    }

    @Test
    void generatedTokenShouldContainUserEmailAndBeValid() {
        String token = jwtService.generateToken(user);

        assertEquals(user.getEmail(), jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void tokenShouldNotBeValidForAnotherUser() {
        String token = jwtService.generateToken(user);
        User anotherUser = new User();
        anotherUser.setEmail("another@example.com");

        assertFalse(jwtService.isTokenValid(token, anotherUser));
    }

    @Test
    void malformedTokenShouldBeRejected() {
        assertThrows(JwtException.class, () -> jwtService.extractUsername("not-a-jwt"));
    }
}
