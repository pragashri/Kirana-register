package com.example.springboot.feature_users.services;

import com.example.springboot.feature_users.dto.ApiResponse;
import com.example.springboot.feature_users.entities.Users;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface UsersService {

    Users save(Users user);

    String getUserIdByUsername(String username);

    List<String> getUserRolesByUsername(String username);

    ResponseEntity<ApiResponse> login(Users user);

    ResponseEntity<ApiResponse> register(Users user);
}
