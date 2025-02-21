package com.example.springboot.feature_users.services;

import static com.example.springboot.feature_users.constants.UserLogConstants.*;

import com.example.springboot.feature_users.dao.UsersDao;
import com.example.springboot.feature_users.dto.ApiResponse;
import com.example.springboot.feature_users.entities.Users;
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
import org.springframework.stereotype.Service;

/**
 * Service implementation for handling user-related operations such as authentication, registration,
 * and user role retrieval.
 */
@Service
public class UsersServiceImpl implements UsersService {

    private final UsersDao usersDao;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtil;

    /**
     * Constructor for UsersServiceImpl.
     *
     * @param usersDao The DAO for interacting with the Users repository.
     * @param authenticationManager The authentication manager for handling login authentication.
     * @param jwtUtil Utility for generating JWT tokens.
     */
    @Autowired
    public UsersServiceImpl(
            UsersDao usersDao, AuthenticationManager authenticationManager, JwtUtils jwtUtil) {
        this.usersDao = usersDao;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Retrieves the roles of a user based on their username.
     *
     * @param username The username of the user.
     * @return A list of roles assigned to the user, or null if the user is not found.
     */
    @Override
    public List<String> getUserRolesByUsername(String username) {
        Users user = usersDao.findByUsername(username);
        return (user != null) ? user.getRoles() : null;
    }

    /**
     * Retrieves the user ID based on their username.
     *
     * @param username The username of the user.
     * @return The user ID as a String, or null if the user is not found.
     */
    @Override
    public String getUserIdByUsername(String username) {
        Users user = usersDao.findByUsername(username);
        return (user != null) ? user.getId() : null;
    }

    /**
     * Saves a user entity to the database.
     *
     * @param user The user entity to be saved.
     * @return The saved user entity.
     */
    @Override
    public Users save(Users user) {
        return usersDao.save(user);
    }

    /**
     * Authenticates a user and generates a JWT token upon successful login.
     *
     * @param user The user credentials (username and password).
     * @return A ResponseEntity containing an ApiResponse with the JWT token upon success, or an
     *     error message if authentication fails.
     */
    @Override
    public ResponseEntity<ApiResponse> login(Users user) {
        try {
            String username = user.getUsername();
            List<String> roles = getUserRolesByUsername(username);
            String userId = getUserIdByUsername(username);

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, user.getPassword()));

            String jwtToken = jwtUtil.generateToken(username, roles, userId);

            Map<String, String> tokenResponse = new HashMap<>();
            tokenResponse.put(JWT_TOKEN, jwtToken);

            return ResponseEntity.ok(
                    new ApiResponse(true, null, SUCCESSFUL_LOGIN, null, tokenResponse));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            new ApiResponse(
                                    false,
                                    INVALID_CREDENTIALS,
                                    INVALID_USERNAME_PASSWORD,
                                    null,
                                    null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse(
                                    false,
                                    INTERNAL_SERVER_ERROR,
                                    UNEXPECTED_ERROR,
                                    e.getMessage(),
                                    null));
        }
    }

    /**
     * Registers a new user by encrypting their password and saving them to the database.
     *
     * @param user The user entity containing registration details.
     * @return A ResponseEntity containing an ApiResponse with the registered user details.
     */
    @Override
    public ResponseEntity<ApiResponse> register(Users user) {
        System.out.println(CALLED);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return ResponseEntity.ok(
                new ApiResponse(true, null, SUCCESSFUL_REGISTRATION, null, save(user)));
    }
}
