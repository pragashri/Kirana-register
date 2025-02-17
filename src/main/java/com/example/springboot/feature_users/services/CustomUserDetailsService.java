package com.example.springboot.feature_users.services;

import com.example.springboot.feature_users.entities.Users;

import java.util.List;
import java.util.Optional;

import com.example.springboot.feature_users.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepo usersRepo;

    @Autowired
    public CustomUserDetailsService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Searching for username: " + username);

        Optional<Users> userOptional =
                Optional.ofNullable(usersRepo.findByUsername(username));

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        Users user = userOptional.get();
        List<String> roleNames = user.getRoles();

        System.out.println("User found: " + user);
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(roleNames.toArray(new String[0]))
                .build();
    }
}
