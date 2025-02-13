package com.example.springboot.feature_users.entities;

import com.example.springboot.feature_users.enums.UserType;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String userName;
    private String userPhoneNumber;
    private UserType userType;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
