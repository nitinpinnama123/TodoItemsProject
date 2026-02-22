package com.revature.revado.repository;

import com.revature.revado.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author $ {USER}
 **/
@SpringBootTest
public class UserRepoTest {

    @Autowired
    private UserRepository userRepository;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        user1 = new User();
        user1.setFName("John");
        user1.setLName("Doe");
        user1.setEmail("john.doe@example.com");

        user2 = new User();
        user2.setFName("Jane");
        user2.setLName("Smith");
        user2.setEmail("jane.smith@example.com");
    }
    @Test
    void findById_WhenUserExists_ShouldReturnUser()
    {
        User savedUser = userRepository.save(user1);
        Optional<User> userFound = userRepository.findById(savedUser.getId());
        assertTrue(userFound.isPresent());
        assertEquals("John", userFound.get().getFName());
    }

    @Test
    void findById_WhenUserDoesNotExist_ShouldReturnEmpty(){
        Optional<User> userFound = userRepository.findById(999L);
        assertFalse(userFound.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllUsers(){
        /*User user1 = new User();
        user1.setFName("John");
        user1.setLName("Doe");

        User user2 = new User();
        user2.setFName("Jane");
        user2.setLName("Smith");*/

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();

        assertEquals(2, users.size());
    }

    @Test
    void findByEmail_WhenEmailExists_ShouldReturnUser() {
        /*User user = new User();
        user.setFName("John");
        user.setLName("Doe");
        user.setEmail("john.doe@example.com");*/
        userRepository.save(user1);

        Optional<User> userFound = userRepository.findByEmail("john.doe@example.com");
        assertTrue(userFound.isPresent());
        assertEquals("John", userFound.get().getFName());
        assertEquals("Doe", userFound.get().getLName());
    }

    @Test
    void findByEmail_WhenEmailDoesNotExist_ShouldReturnEmpty() {
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertFalse(foundUser.isPresent());
    }

    @Test
    void deleteUser_ShouldRemoveUser(){
        /*User user = new User();
        user.setFName("John");
        user.setLName("Doe");
        user.setEmail("john.doe@example.com");*/
        User savedUser = userRepository.save(user1);
        Long userId = savedUser.getId();

        userRepository.delete(savedUser);

        Optional<User> deletedUser = userRepository.findById(userId);
        assertFalse(deletedUser.isPresent());
    }

    @Test
    void updateUser_ShouldModifyUser()
    {
        User savedUser = userRepository.save(user1);
        Long userId = savedUser.getId();

        savedUser.setFName("John");
        savedUser.setLName("Updated");
        savedUser.setEmail("john.updated@example.com");
        userRepository.save(savedUser);

        Optional<User> updatedUser = userRepository.findById(userId);
        assertTrue(updatedUser.isPresent());
        assertEquals("John", updatedUser.get().getFName());
        assertEquals("Updated", updatedUser.get().getLName());
        assertEquals("john.updated@example.com", updatedUser.get().getEmail());
    }

    @Test
    void deleteById_ShouldRemoveUser() {
        User savedUser = userRepository.save(user1);
        Long userId = savedUser.getId();

        userRepository.deleteById(userId);

        assertFalse(userRepository.findById(userId).isPresent());
    }

    @Test
    void count_ShouldReturnNumberOfUsers(){
        userRepository.save(user1);
        userRepository.save(user2);

        long count = userRepository.count();
        assertEquals(2, count);
    }

    @Test
    void findByEmail_WithDifferentCase_ShouldWork() {
        userRepository.save(user1);

        Optional<User> userFound = userRepository.findByEmail("john.doe@example.com");
        assertTrue(userFound.isPresent());
        assertEquals("John", userFound.get().getFName());
        assertEquals("Doe", userFound.get().getLName());
    }
}


