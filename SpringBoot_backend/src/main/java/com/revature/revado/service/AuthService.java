package com.revature.revado.service;

import com.revature.revado.dto.AuthResponse;
import com.revature.revado.dto.LoginRequest;
import com.revature.revado.dto.RegisterRequest;
import com.revature.revado.entity.User;
import com.revature.revado.exception.DuplicateResourceException;
import com.revature.revado.exception.ResourceNotFoundException;
import com.revature.revado.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author $ {USER}
 **/
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists: " + request.getUsername());
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }

        // Create new user
        User user = new User();
        user.setFName(request.getName());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        //user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPassword(request.getPassword());
        user.setRole(User.Role.USER);

        user = userRepository.save(user);

        return mapToAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        // Find user by username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid username or password"));

        // Verify password
        if (!request.getPassword().equals( user.getPassword())) {
            throw new ResourceNotFoundException("Invalid username or password");
        }

        return mapToAuthResponse(user);
    }

    //@Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    private AuthResponse mapToAuthResponse(User user) {
        AuthResponse response = new AuthResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setName(user.getFName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setToken("session-token-" + user.getId()); // Simple session token
        return response;
    }


}
