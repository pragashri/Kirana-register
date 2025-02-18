package com.example.springboot.feature_users.services;

import static com.example.springboot.feature_users.constants.UserLogConstants.*;

import com.example.springboot.feature_users.entities.Users;
import com.example.springboot.feature_users.repo.UsersRepo;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepo usersRepo;

    @Autowired
    public CustomUserDetailsService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    /**
     * Loads a user by their username and returns the user details for authentication. This method
     * queries the {@link UsersRepo} to find the user with the given username, then constructs a
     * {@link UserDetails} object with the user's credentials and roles.
     *
     * @param username The username of the user to be loaded.
     * @return A {@link UserDetails} object containing the user's credentials and roles.
     * @throws UsernameNotFoundException If no user is found with the given username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(SEARCH_USERNAME + username);

        Optional<Users> userOptional = Optional.ofNullable(usersRepo.findByUsername(username));

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }

        Users user = userOptional.get();
        List<String> roleNames = user.getRoles();

        log.info(USER_FOUND + user);
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(roleNames.toArray(new String[0]))
                .build();
    }
}
