package com.revature.revado.controller;

import com.revature.revado.dto.AuthResponse;
import com.revature.revado.dto.LoginRequest;
import com.revature.revado.dto.RegisterRequest;
import com.revature.revado.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author $ {USER}
 **/
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @Test
    void register_ShouldReturnCreated() {
        RegisterRequest request = new RegisterRequest();
        request.setName("user1");
        request.setUsername("user1-abc");
        request.setEmail("user1@example.com");
        request.setPassword("password");

        ResponseEntity<AuthResponse> response =
                authController.register(request);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("user1-abc", response.getBody().getUsername());
    }

    @Test
    void login_ShouldReturnOk() {
        // First register user
        RegisterRequest register = new RegisterRequest();
        register.setName("user1");
        register.setUsername("user1-abc");
        register.setEmail("user1@example.com");
        register.setPassword("password");

        ResponseEntity<AuthResponse> register_response =
                authController.register(register);

        LoginRequest login = new LoginRequest();
        login.setUsername("user1-abc");
        login.setPassword("password");

        ResponseEntity<AuthResponse> response =
                authController.login(login);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("user1-abc", response.getBody().getUsername());
        assertEquals("session-token-" + register_response.getBody().getUserId(), response.getBody().getToken());
    }

    @Test
    void logout_ShouldReturnOk() {
        ResponseEntity<Void> response = authController.logout();
        assertEquals(200, response.getStatusCode().value());
    }
}