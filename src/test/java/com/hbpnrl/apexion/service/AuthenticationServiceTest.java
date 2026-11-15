package com.hbpnrl.apexion.service;

import com.hbpnrl.apexion.dto.request.AuthenticationRequest;
import com.hbpnrl.apexion.dto.request.RegisterRequest;
import com.hbpnrl.apexion.dto.response.AuthenticationResponse;
import com.hbpnrl.apexion.entity.User;
import com.hbpnrl.apexion.repository.UserRepository;
import com.hbpnrl.apexion.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void registerShouldEncodePasswordAndReturnToken() {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .password("plain-password")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("plain-password")).thenReturn("encoded-password");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        AuthenticationResponse response = authenticationService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals("john@example.com", userCaptor.getValue().getEmail());
        assertEquals("encoded-password", userCaptor.getValue().getPassword());
        assertEquals("jwt-token", response.getToken());
    }

    @Test
    void registerShouldRejectExistingEmail() {
        RegisterRequest request = RegisterRequest.builder()
                .email("existing@example.com")
                .password("password")
                .build();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authenticationService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticateShouldDelegateCredentialsAndReturnToken() {
        AuthenticationRequest request = new AuthenticationRequest("john@example.com", "password");
        User user = new User();
        user.setEmail(request.getEmail());
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthenticationResponse response = authenticationService.authenticate(request);

        verify(authenticationManager).authenticate(any());
        assertEquals("jwt-token", response.getToken());
    }
}
