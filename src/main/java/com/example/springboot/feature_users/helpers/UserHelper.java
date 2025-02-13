package com.example.springboot.feature_users.helpers;

import com.example.springboot.feature_users.entities.User;
import com.example.springboot.feature_users.enums.UserType;
import org.springframework.stereotype.Service;

@Service
public class UserHelper {
    public boolean isOwner(User user) {
        return user != null && user.getUserType() == UserType.OWNER;
    }

    public boolean isEmployee(User user) {
        return user != null && user.getUserType() == UserType.EMPLOYEE;
    }
}
