package com.example.springboot.feature_users.dao;

import com.example.springboot.feature_users.entities.Users;
import com.example.springboot.feature_users.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsersDao {

    private final UsersRepo usersRepo;

    @Autowired
    public UsersDao(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    /**
     * saves a user
     *
     * @param user
     * @return
     */
    public Users save(Users user) {
        return usersRepo.save(user);
    }

    /**
     * finds user by username
     *
     * @param username
     * @return
     */
    public Users findByUsername(String username) {
        return usersRepo.findByUsername(username);
    }
}