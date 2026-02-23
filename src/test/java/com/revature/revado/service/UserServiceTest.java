package com.revature.revado.service;

import com.revature.revado.entity.User;
import com.revature.revado.repository.UserRepository;
import org.h2.engine.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author $ {USER}
 **/
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;


    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFName("John");
        user.setLName("Doe");
        user.setPassword("pwd");
        user.setUsername("johndoe");
        user.setEmail("john.doe@example.com");
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(user, new User(2L, "Jane", "Doe", "janedoe", "pwd", "jane.doe@example.com", User.Role.USER, null, null));
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getUsers();

        // Assert
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getUserById(1L));
        assertTrue(exception.getMessage().contains("User not found"));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void createUser_WhenEmailDoesNotExist_ShouldCreateUser() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.createUser(user);

        // Assert
        assertNotNull(result);
        assertEquals("johndoe", result.getUsername());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_WhenEmailExists_ShouldThrowException() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.createUser(user));
        assertTrue(exception.getMessage().contains("Email already exists"));
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_WhenUserExists_ShouldUpdateUser() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setUsername("JohnUpdated");
        updatedUser.setEmail("john.updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(updatedUser.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(updatedUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.updateUser(1L, updatedUser);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_WhenEmailAlreadyInUse_ShouldThrowException() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setUsername("JohnUpdated");
        updatedUser.setEmail("existing@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(user));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateUser(1L, updatedUser));
        assertTrue(exception.getMessage().contains("Email already in use"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1L);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).deleteById(1L);
    }

}
