package com.example.springboot.feature_users.repo;

import com.example.springboot.feature_users.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<User, String> {
    Optional<User> findByUserPhoneNumber(String userPhoneNumber);
}
