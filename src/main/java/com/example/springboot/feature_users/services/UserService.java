package com.example.springboot.feature_users.services;

import com.example.springboot.feature_users.entities.User;

import java.util.Optional;

public interface UserService {
    User createUser(User user);
    Optional<User> getUserById(String id);
    Optional<User> getUserByPhoneNumber(String phoneNumber);
    void deleteUser(String id);
}
