package com.revature.revado.controller;

import com.revature.revado.entity.User;
import com.revature.revado.repository.UserRepository;
import com.revature.revado.service.UserService;
import jakarta.transaction.Transactional;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;





import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


/**
 * @author $ {USER}
 **/
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
//@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Clean database before each test
        userRepository.deleteAll();
    }

    @Test
    void getAllUsers_ShouldReturnUserList() throws Exception {
        // Arrange
        User user1 = new User(null, "John", "Doe", "JohnDoe", "pwd123", "john.doe@example.com", User.Role.USER, LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User(null, "Jane", "Doe", "JaneDoe", "pwd", "jane.doe@example.com", User.Role.USER, LocalDateTime.now(), LocalDateTime.now());
        userRepository.save(user1);
        userRepository.save(user2);

        // Act & Assert
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("JohnDoe")))
                .andExpect(jsonPath("$[1].username", is("JaneDoe")));
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() throws Exception {
        // Arrange
        User user = new User(null, "John", "Doe", "JohnDoe", "pwd123", "john.doe@example.com", User.Role.USER, LocalDateTime.now() ,LocalDateTime.now());
        User savedUser = userRepository.save(user);

        // Act & Assert
        mockMvc.perform(get("/api/users/" + savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedUser.getId().intValue())))
                .andExpect(jsonPath("$.fName", is("John")))
                .andExpect(jsonPath("$.lName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }


    @Test
    void createUser_WithValidData_ShouldCreateUser() throws Exception {
        // Arrange
        User user = new User(null, "John", "Doe", "JohnDoe", "pwd123", "john.doe@example.com", User.Role.USER, LocalDateTime.now(), LocalDateTime.now());
        //userRepository.save(user);
        // Act & Assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fName", is("John")))
                .andExpect(jsonPath("$.lName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    void createUser_WithDuplicateEmail_ShouldReturnError() throws Exception {
        // Arrange
        User existingUser = new User(null, "John", "Doe", "JohnDoe", "pwd123", "john.doe@example.com", User.Role.USER, LocalDateTime.now(), LocalDateTime.now());
        userRepository.save(existingUser);

        User newUser = new User(null, "John", "Doe", "JohnDoe", "pwd123", "john.doe@example.com", User.Role.USER, LocalDateTime.now(), LocalDateTime.now());

        // Act & Assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isConflict());

    }

    @Test
    void createUser_withNameAndEmail_ShouldCreateUser() throws Exception {
        User user1 = new User(null, null, null, "johndoe", "pwd", "john.doe@example.com", User.Role.USER, LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User(null, null, null, "abc", "pwd2", "abc@example.com", User.Role.USER, LocalDateTime.now(), LocalDateTime.now());


        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateUser_WithValidData_ShouldUpdateUser() throws Exception {
        User existingUser = new User(null, "John", "Doe", "JohnDoe", "pwd123", "john.doe@example.com", User.Role.USER, LocalDateTime.now(), LocalDateTime.now());
        userRepository.save(existingUser);

        User updatedUser = new User();
        updatedUser.setPassword("newPwd");
        updatedUser.setEmail("john.doe@gmail.com");

        mockMvc.perform(put("/api/users/" + existingUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password", is("newPwd")))
                .andExpect(jsonPath("$.email", is("john.doe@gmail.com")));
    }

    @Test
    void deleteUser_shouldDeleteUser() throws Exception {
        User user = new User(null, "John", "Doe", "JohnDoe", "pwd123", "john.doe@example.com", User.Role.USER, LocalDateTime.now(), LocalDateTime.now());
        User savedUser = userRepository.save(user);
        mockMvc.perform(delete("/api/users/" + savedUser.getId()))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/users/" + savedUser.getId()))
                .andExpect(status().isNotFound());
    }










}
