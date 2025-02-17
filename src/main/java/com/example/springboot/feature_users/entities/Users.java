package com.example.springboot.feature_users.entities;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users") // Specify MongoDB collection name
public class Users {

    @Id // MongoDB uses _id instead of @GeneratedValue
    private String id;

    private String username;

    private String password;

    private List<String> roles; // MongoDB stores lists natively

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}