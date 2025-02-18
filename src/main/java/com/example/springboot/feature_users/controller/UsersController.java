package com.example.springboot.feature_users.controller;

import static com.example.springboot.feature_users.constants.UserLogConstants.*;

import com.example.springboot.feature_users.dto.ApiResponse;
import com.example.springboot.feature_users.entities.Users;
import com.example.springboot.feature_users.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    /**
     * Login as a user
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody Users user) {
        return usersService.login(user);
    }

    /**
     * Register a new user
     *
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody Users user) {
        return usersService.register(user);
    }
}
