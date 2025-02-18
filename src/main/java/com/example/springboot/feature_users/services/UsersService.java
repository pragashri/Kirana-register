package com.example.springboot.feature_users.services;

import com.example.springboot.feature_users.entities.Users;
import java.util.List;

public interface UsersService {

    /**
     * save user interface
     *
     * @param user
     * @return
     */
    Users save(Users user);

    /**
     * get user id by username interface
     *
     * @param username
     * @return
     */
    String getUserIdByUsername(String username);

    /**
     * get user roles by username interface
     *
     * @param username
     * @return
     */
    List<String> getUserRolesByUsername(String username);
}
