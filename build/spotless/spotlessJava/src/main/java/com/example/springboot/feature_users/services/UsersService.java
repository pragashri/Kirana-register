package com.example.springboot.feature_users.services;

import com.example.springboot.feature_users.dto.ApiResponse;
import com.example.springboot.feature_users.entities.Users;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface UsersService {

    Users save(Users user);

    String getUserIdByUsername(String username);

    List<String> getUserRolesByUsername(String username);

    ResponseEntity<ApiResponse> login(Users user);

    ResponseEntity<ApiResponse> register(Users user);
}
