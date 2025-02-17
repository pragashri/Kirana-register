package com.example.springboot.feature_users.controller;

import com.example.springboot.feature_users.dao.UsersDao;
import com.example.springboot.feature_users.dto.ApiResponse;
import com.example.springboot.feature_users.entities.Users;
import com.example.springboot.feature_users.services.UsersService;
import com.example.springboot.feature_users.services.UsersServiceImpl;

import com.example.springboot.feature_users.jwt.JwtUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users") // Base path for user-related APIs
public class UsersController {

    private final UsersService usersService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtil;
    private final UsersDao usersDao;

    @Autowired
    public UsersController(
            UsersService usersService,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtil,
            UsersDao usersDao) {
        this.usersService = usersService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usersDao = usersDao;
    }

    /**
     * login as a user
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody Users user) {
        try {
            String username = user.getUsername();
            List<String> roles = usersService.getUserRolesByUsername(username);
            String userId = usersService.getUserIdByUsername(username);

            // 🔹 Authenticate user credentials
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, user.getPassword()));

            // 🔹 Generate JWT token
            String jwtToken = jwtUtil.generateToken(username, roles, userId);

            // 🔹 Create response map with JWT token
            Map<String, String> tokenResponse = new HashMap<>();
            tokenResponse.put("Jwt Token", jwtToken);

            // 🔹 Return API response
            return ResponseEntity.ok(
                    new ApiResponse(true, null, "Login Successful", null, tokenResponse));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            new ApiResponse(
                                    false,
                                    "INVALID_CREDENTIALS",
                                    "Invalid username or password",
                                    null,
                                    null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse(
                                    false,
                                    "INTERNAL_ERROR",
                                    "An unexpected error occurred",
                                    e.getMessage(),
                                    null));
        }
    }

    /**
     * register as a user
     *
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody Users user) {
        System.out.println("called");
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return ResponseEntity.ok(
                new ApiResponse(
                        true, null, "Registration Successful", null, usersService.save(user)));
    }
}