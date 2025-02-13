package com.example.springboot.feature_users.services;

import com.example.springboot.feature_users.dao.UserDao;
import com.example.springboot.feature_users.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User createUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userDao.saveUser(user);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userDao.findUserById(id);
    }

    @Override
    public Optional<User> getUserByPhoneNumber(String phoneNumber) {
        return userDao.findUserByPhoneNumber(phoneNumber);
    }

    @Override
    public void deleteUser(String id) {
        userDao.deleteUser(id);
    }
}
