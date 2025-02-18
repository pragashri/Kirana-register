package com.example.springboot.feature_users.repo;

import com.example.springboot.feature_users.entities.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepo extends MongoRepository<Users, String> {
    Users findByUsername(String username);
}
