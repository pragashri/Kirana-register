package com.example.springboot.feature_users.services;

import com.example.springboot.feature_users.entities.Users;;
import java.util.List;
import java.util.Optional;

public interface UsersService {
    public Users addUser(Users user);

    public List<Users> getAllUsers();

    public Optional<Users> getUserById(Long id);

    public Users saveUser(Users user);

    public Users updateUser(Long uid, Users updatedUser);

    public Users save(Users user);

    public String getUserIdByUsername(String username);

    public List<String> getUserRolesByUsername(String username);
}