package com.example.springboot.feature_users.services;

import com.example.springboot.feature_users.dao.UsersDao;
import com.example.springboot.feature_users.entities.Users;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersDao usersDao;

    @Autowired
    public UsersServiceImpl(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    /**
     * returns user roles by username
     *
     * @param username
     * @return
     */
    public List<String> getUserRolesByUsername(String username) {
        Users user = usersDao.findByUsername(username);
        return (user != null) ? user.getRoles() : null;
    }

    /**
     * get user id by username
     *
     * @param username
     * @return
     */
    public String getUserIdByUsername(String username) {
        Users user = usersDao.findByUsername(username);
        return (user != null) ? user.getId() : null;
    }

    /**
     * save user through dao
     *
     * @param user
     * @return
     */
    public Users save(Users user) {
        return usersDao.save(user);
    }
}
