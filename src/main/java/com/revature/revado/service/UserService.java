package com.revature.revado.service;

import com.revature.revado.entity.User;
import com.revature.revado.exception.DuplicateResourceException;
import com.revature.revado.exception.ResourceNotFoundException;
import com.revature.revado.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author $ {USER}
 **/
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;

    public User createUser(User user) {
        if (userRepo.findByUsername(user.getUsername()).isPresent())
        {
            throw new DuplicateResourceException("Username already exists");
        }
        if (userRepo.findByEmail(user.getEmail()).isPresent())
        {
            throw new DuplicateResourceException("Email already exists");
        }

        return userRepo.save(user);
    }

    public void deleteUser(Long userId) {

        if (!userRepo.findById(userId).isPresent()) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        userRepo.deleteById(userId);
    }

    public User updateUser(Long userId,
                           User user) {

        User existingUser = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        existingUser.setPassword(user.getPassword());
        existingUser.setFName(user.getFName());
        existingUser.setLName(user.getLName());
        if (!existingUser.getEmail().equals(user.getEmail())
                && userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already in use: " + user.getEmail());
        }
        existingUser.setEmail(user.getEmail());

        if (!existingUser.getUsername().equals(user.getUsername())
                && userRepo.findByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username already in use: " + user.getEmail());
        }
        existingUser.setUsername(user.getUsername());
        return userRepo.save(existingUser);
    }

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public User getUserById(Long userID)
    {
       /*return userRepo.findById(userID)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + userID));*/
        Optional<User> possibleUser = userRepo.findById(userID);
        if (possibleUser.isPresent()) {
            return possibleUser.get();
        } else {
            throw new ResourceNotFoundException("User not found with id: " + userID);
        }
    }

}
