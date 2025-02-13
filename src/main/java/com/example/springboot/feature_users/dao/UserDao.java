package com.example.springboot.feature_users.dao;

import com.example.springboot.feature_users.entities.User;
import com.example.springboot.feature_users.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDao {
    private final UserRepo userRepo;

    @Autowired
    public UserDao(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User saveUser(User user) {
        return userRepo.save(user);
    }

    public Optional<User> findUserById(String id) {
        return userRepo.findById(id);
    }

    public Optional<User> findUserByPhoneNumber(String phoneNumber) {
        return userRepo.findByUserPhoneNumber(phoneNumber);
    }

    public void deleteUser(String id) {
        userRepo.deleteById(id);
    }
}
