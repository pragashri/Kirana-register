//package com.example.springboot.feature_users.controller;
//
//import com.example.springboot.feature_users.entities.Users;
//import com.example.springboot.feature_users.jwt.JwtUtils;
//import com.example.springboot.feature_users.repo.UsersRepo;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    private final UsersRepo usersRepo;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtUtils jwtUtils;
//
//    public AuthController(UsersRepo usersRepo, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
//        this.usersRepo = usersRepo;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtUtils = jwtUtils;
//    }
//
//    @PostMapping("/register")
//    public String register(@RequestBody User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        usersRepo.save(user);
//        return "User registered successfully";
//    }
//
//    @PostMapping("/login")
//    public String login(@RequestBody User user) {
//        Optional<User> dbUser = usersRepo.findByUserName(user.getUserName());
//        if (dbUser.isPresent() && passwordEncoder.matches(user.getPassword(), dbUser.get().getPassword())) {
//            return jwtUtils.generateToken(user.getUserName(), dbUser.get().getUserType().name());
//        }
//        return "Invalid credentials";
//    }
//}
